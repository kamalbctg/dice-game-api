package com.game.dice.board.service.impl;

import com.game.dice.board.config.GameBoardConf;
import com.game.dice.board.dao.GameBoardRepository;
import com.game.dice.board.entity.GameBoard;
import com.game.dice.board.entity.Player;
import com.game.dice.board.service.GameBoardService;
import com.game.dice.board.service.RollService;
import com.game.dice.board.service.ScorerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private int nextIndex(GameBoard gameBoard, int idx) {
        return (idx + 1) % gameBoard.getPlayers().size();
    }

    @Override
    @Async
    public void rollAndScore(String boardId) {
        GameBoard gameBoard = gameBoardRepository.getGameBoard(boardId);
        if(gameBoard == null){
            log.error(" Board of id: [{}] not found", boardId);
            return;
        }

        if(gameBoard.getPlayers() == null && gameBoard.getPlayers().size() < GameBoardConf.MIN_PLAYER_REQUIRED){
            log.error(" Board of id: [{}], do not have enough player to start", boardId);
            return;
        }
        rollAndPlay(gameBoard);
    }

    private void rollAndPlay(GameBoard gameBoard) {
        try {
            int index = 0;
            gameBoard.playOn();
            List<Player> players = gameBoardRepository.getPlayers(gameBoard.getId());
            log.info("Stat Playing game");
            while (gameBoard.isPlayOn()) {
                int currentScore = scorerService.roll();
                Player player = players.get(index);
                if (currentScore == gameBoardConf.getPenaltyScore()) {
                    setPenaltyScore(player);
                    log.info(" Player : [{}] get penalty, current score:[{}]", player.getName(), player.getScore());
                    index = nextIndex(gameBoard, index);
                    continue;
                }

                if (currentScore == gameBoardConf.getStartScore() && !player.isStartRolling()) {
                    player.setStartRolling(true);
                    continue;
                }

                if (currentScore == gameBoardConf.getStartScore()) {
                    player.setScore(player.getScore() + currentScore);
                    continue;
                }
                player.setScore(player.getScore() + ((player.isStartRolling()) ? currentScore : 0));
                index = nextIndex(gameBoard, index);
                log.info(" Player : [{}] , current score:[{}]", player.getName(), player.getScore());
                log.info(player.getName() + "");
                if (player.getScore() >= gameBoardConf.getWinningScore()) {
                    gameBoard.playOff();
                    log.info(" Winner Player : [{}] get penalty, current score:[{}]", player.getName(), player.getScore());
                    break;
                }
            }
            log.info("End Playing game");
        } catch (Exception e) {
            log.error("Exception while paling game", e);
        } finally {
            gameBoard.playOff();
        }
    }

    private void setPenaltyScore(Player player) {
        if (player.isStartRolling()) {
            player.setScore(Math.max(0, player.getScore() - gameBoardConf.getPenaltyScore()));
            player.setStartRolling(player.getScore() > 0);
        }
    }
}
