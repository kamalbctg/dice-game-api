package com.game.dice.board.service.impl;

import com.game.dice.board.config.GameBoardConf;
import com.game.dice.board.entity.GameBoard;
import com.game.dice.board.entity.Player;
import com.game.dice.board.service.RollService;
import com.game.dice.board.service.ScorerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RollServiceImpl implements RollService {

    private GameBoardConf gameBoardConf;
    private ScorerService scorerService;

    public RollServiceImpl(GameBoardConf gameBoardConf, ScorerService scorerService) {
        this.gameBoardConf = gameBoardConf;
        this.scorerService = scorerService;
    }

    private int nextIndex(GameBoard gameBoard, int idx) {
        return (idx + 1) % gameBoard.getPlayers().size();
    }

    @Override
    @Async
    public void rollAndScore(GameBoard gameBoard) {
        try {
            int index = 0;
            gameBoard.playOn();
            log.info("Stat Playing game");
            while (gameBoard.isPlayOn()) {
                Player player = gameBoard.getPlayers().get(index);
                int currentScore = scorerService.roll(null);
                if (currentScore == gameBoardConf.getPenaltyScore()) {
                    if (player.isStartRolling()) {
                        player.setScore(Math.max(0, player.getScore() - gameBoardConf.getPenaltyScore()));
                        player.setStartRolling(player.getScore() > 0);
                    }
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
                }
            }
            log.info("End Playing game");
        } catch (Exception e) {
            log.error("Exception while paling game", e);
        } finally {
            gameBoard.playOff();
        }
    }
}
