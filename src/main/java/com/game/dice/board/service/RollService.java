package com.game.dice.board.service;


/**
 * The interface Roll service.
 */
public interface RollService {
    /**
     * Roll and score.
     *
     * @param gameBoard the game board
     */
    void rollAndScore(String boardId);
}
