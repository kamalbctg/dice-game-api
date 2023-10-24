package com.game.dice.board.service;

import com.game.dice.board.entity.GameBoard;

/**
 * The interface Roll service.
 */
public interface RollService {
    /**
     * Roll and score.
     *
     * @param gameBoard the game board
     */
    void rollAndScore(GameBoard gameBoard);
}
