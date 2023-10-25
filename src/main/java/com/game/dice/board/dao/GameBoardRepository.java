package com.game.dice.board.dao;

import com.game.dice.board.entity.GameBoard;
import com.game.dice.board.entity.Player;

import java.util.List;

public interface GameBoardRepository {
    GameBoard joinPlayer(String boardId, Player player);

    GameBoard createGameBoard(String name);

    GameBoard getGameBoard(String boardId);

    Player getPlayer(String boardId, String playerId);

    List<Player> getPlayers(String boardId);

    GameBoard resetBoard(String boardId);
}
