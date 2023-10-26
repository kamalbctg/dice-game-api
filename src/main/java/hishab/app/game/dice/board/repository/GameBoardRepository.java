package hishab.app.game.dice.board.repository;

import hishab.app.game.dice.board.entity.GameBoard;
import hishab.app.game.dice.board.entity.Player;

import java.util.List;

public interface GameBoardRepository {
    GameBoard joinPlayer(String boardId, Player player);

    GameBoard createGameBoard(String name);

    GameBoard getGameBoard(String boardId);

    Player getPlayer(String boardId, String playerId);

    List<Player> getPlayers(String boardId);

    GameBoard resetBoard(String boardId);
}
