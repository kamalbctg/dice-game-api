package hishab.app.game.dice.board.service.impl;

import hishab.app.game.dice.board.config.GameBoardConf;
import hishab.app.game.dice.board.repository.GameBoardRepository;
import hishab.app.game.dice.board.entity.GameBoard;
import hishab.app.game.dice.board.entity.Player;
import hishab.app.game.dice.board.exception.ApiException;
import hishab.app.game.dice.board.exception.ErrorDefinition;
import hishab.app.game.dice.board.model.request.CreateBoardRequest;
import hishab.app.game.dice.board.service.GameBoardService;
import hishab.app.game.dice.board.service.RollService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GameBoardServiceImpl implements GameBoardService {
    private GameBoardRepository gameBoardRepository;

    private GameBoardConf gameBoardConf;
    private RollService rollService;

    @Autowired
    public GameBoardServiceImpl(GameBoardRepository gameBoardRepository, GameBoardConf gameBoardConf, RollService rollService) {
        this.gameBoardRepository = gameBoardRepository;
        this.gameBoardConf = gameBoardConf;
        this.rollService = rollService;
    }


    @Override
    public Player join(String boardId, Player player) {
        GameBoard board = gameBoardRepository.getGameBoard(boardId);
        if (board == null) {
            throw new ApiException(ErrorDefinition.BOARD_NOT_FOUND);
        }

        if (player == null || player.getName().isBlank() || player.getAge() <= 0) {
            throw new ApiException(ErrorDefinition.INSUFFICIENT_PLAYER_DATA);
        }
        gameBoardRepository.joinPlayer(boardId, player);
        return player;
    }


    @Override
    public GameBoard createBoard(CreateBoardRequest createBoardRequest) {
        if (createBoardRequest == null || createBoardRequest.getName().isBlank()) {
            throw new ApiException(ErrorDefinition.INSUFFICIENT_BOARD_DATA);
        }
        return gameBoardRepository.createGameBoard(createBoardRequest.getName());
    }

    @Override
    public GameBoard resetBoard(String boardId) {
        GameBoard board = gameBoardRepository.getGameBoard(boardId);
        if (board == null) {
            throw new ApiException(ErrorDefinition.BOARD_NOT_FOUND);
        }

        return gameBoardRepository.resetBoard(boardId);
    }

    @Override
    public GameBoard getGameBoard(String boardId) {
        if (boardId == null || boardId.isBlank()) {
            throw new ApiException(ErrorDefinition.INSUFFICIENT_BOARD_DATA);
        }
        return gameBoardRepository.getGameBoard(boardId);
    }

    @Override
    @Async
    public void play(String boardId) {
        GameBoard gameBoard = gameBoardRepository.getGameBoard(boardId);
        if (gameBoard == null) {
            log.error(" Board of id: [{}] not found", boardId);
            throw new ApiException(ErrorDefinition.BOARD_NOT_FOUND);
        }

        if (gameBoard.getPlayers() == null || gameBoard.numberOfPlayers() < GameBoardConf.MIN_PLAYER_REQUIRED ||
                gameBoard.numberOfPlayers() > GameBoardConf.MAX_PLAYER_ALLOWED) {
            log.error(" Board of id: [{}], player requirement miss match  to start, number of players {}", boardId, gameBoard.numberOfPlayers());
            throw new ApiException(ErrorDefinition.BOARD_PLAYER_REQUIREMENT);
        }

        resetBoard(boardId);
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
                int currentScore = rollService.roll();
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

    private static int nextIndex(int index, int upperBound) {
        return (++index) % upperBound;
    }
}
