package com.game.dice.board.service.impl;

import com.game.dice.board.config.GameBoardConf;
import com.game.dice.board.dao.GameBoardRepository;
import com.game.dice.board.entity.GameBoard;
import com.game.dice.board.entity.Player;
import com.game.dice.board.exception.ApiException;
import com.game.dice.board.exception.ErrorDefinition;
import com.game.dice.board.service.RollService;
import com.game.dice.board.service.ScorerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.game.dice.board.config.GameBoardConf.MIN_PLAYER_REQUIRED;

@Slf4j
@Service
public class RollServiceImpl implements RollService {
    private GameBoardConf gameBoardConf;
    private ScorerService scorerService;
    private GameBoardRepository gameBoardRepository;

    public RollServiceImpl(GameBoardConf gameBoardConf, ScorerService scorerService, GameBoardRepository gameBoardRepository) {
        this.gameBoardConf = gameBoardConf;
        this.scorerService = scorerService;
        this.gameBoardRepository = gameBoardRepository;
    }

    private static int nextIndex(int index, int upperBound) {
        return (++index) % upperBound;
    }

    @Override
    @Async
    public void rollAndScore(String boardId) {
        GameBoard gameBoard = gameBoardRepository.getGameBoard(boardId);
        if (gameBoard == null) {
            log.error(" Board of id: [{}] not found", boardId);
            throw new ApiException(ErrorDefinition.BOARD_NOT_FOUND);
        }

        if (gameBoard.getPlayers() == null || gameBoard.numberOfPlayers() < MIN_PLAYER_REQUIRED ||
                gameBoard.numberOfPlayers() > GameBoardConf.MAX_PLAYER_ALLOWED) {
            log.error(" Board of id: [{}], player requirement miss match  to start, number of players {}", boardId, gameBoard.numberOfPlayers());
            throw new ApiException(ErrorDefinition.BOARD_PLAYER_REQUIREMENT);
        }
        rollAndPlay(gameBoard);
    }

    private void rollAndPlay(GameBoard gameBoard) {
        try {
            int index = 0;
            int upperBound = gameBoard.numberOfPlayers();
            int maxScore = Integer.MIN_VALUE;
            gameBoard.playOn();
            List<Player> players = gameBoard.getPlayers();
            log.info("Stat Playing game");
            while (gameBoard.isPlayOn()) {
                int currentScore = scorerService.roll();
                Player player = players.get(index);
                if (index == 1)
                    log.info("Index : [{}] , current score:[{}]", index, currentScore);
                if (maxScore >= gameBoardConf.getWinningScore()) {
                    break;
                }
                if (currentScore == gameBoardConf.getPenaltyScore()) {
                    if (!player.isStartRolling()) {
                        player.setScore(Math.max(0, player.getScore() - gameBoardConf.getPenaltyScore()));
                    }
                    log.info("Player : [{}] get penalty, current score:[{}]", index, player.getScore());
                    player.setStartRolling(false);
                    index = nextIndex(index, upperBound);
                } else if (currentScore == gameBoardConf.getStartScore() && !player.isStartRolling()) {
                    player.setStartRolling(true);
                    log.info("Player : [{}] start rolling, current score:[{}]", index, player.getScore());
                } else if (currentScore == gameBoardConf.getStartScore()) {
                    player.setScore(player.getScore() + currentScore);
                    log.info("Player : [{}] hit 6, current score:[{}]", index, player.getScore());
                } else {
                    player.setScore(player.getScore() + currentScore);
                    log.info(" Player : [{}] , current score:[{}]", index, player.getScore());
                    player.setStartRolling(false);
                    index = nextIndex(index, upperBound);
                }
                maxScore = Math.max(maxScore, player.getScore());
            }
            log.info("End Playing game");
        } catch (Exception e) {
            log.error("Exception while paling game", e);
        } finally {
            gameBoard.playOff();
        }
    }

}
