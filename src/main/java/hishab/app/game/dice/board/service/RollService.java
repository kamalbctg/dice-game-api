package hishab.app.game.dice.board.service;


/**
 * The interface Roll service.
 */
public interface RollService {
    /**
     * Roll and score.
     *
     * @param boardId the board id
     */
    void rollAndScore(String boardId);
}
