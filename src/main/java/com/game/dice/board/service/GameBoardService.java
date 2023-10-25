package com.game.dice.board.service;

import com.game.dice.board.entity.GameBoard;
import com.game.dice.board.entity.Player;
import com.game.dice.board.model.request.CreateBoardRequest;

/**
 * The interface Game board service.
 */
public interface GameBoardService {
    /**
     * Join player.
     *
     * @param boardId the board id
     * @param player  the player
     * @return the player
     */
    Player join(String boardId, Player player);

    /**
     * Create board game board.
     *
     * @param createBoardRequest the create board request
     * @return the game board
     */
    GameBoard createBoard(CreateBoardRequest createBoardRequest);

    /**
     * Reset board game board.
     *
     * @param boardId the board id
     * @return the game board
     */
    GameBoard resetBoard(String boardId);

    /**
     * Gets game board.
     *
     * @param boardId the board id
     * @return the game board
     */
    GameBoard getGameBoard(String boardId);
}
