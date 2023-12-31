package hishab.app.game.dice.board.service;

import hishab.app.game.dice.board.entity.GameBoard;
import hishab.app.game.dice.board.entity.Player;
import hishab.app.game.dice.board.model.request.CreateBoardRequest;

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

    /**
     * Roll and score.
     *
     * @param boardId the board id
     */
    void play(String boardId);
}
