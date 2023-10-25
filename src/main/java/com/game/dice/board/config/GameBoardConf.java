package com.game.dice.board.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class GameBoardConf {
    public static final int MIN_PLAYER_REQUIRED = 2;

    public static final int MAX_PLAYER_ALLOWED = 4;

    @Value("${app.board.score.start:6}")
    private int startScore;

    @Value("${app.board.score.penalty:4}")
    private int penaltyScore;

    @Value("${app.board.score.wining:26}")
    private int winningScore;
}
