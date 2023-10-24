package com.game.dice.board.dao.impl;

import com.game.dice.board.dao.GameBoardRepository;
import com.game.dice.board.entity.GameBoard;
import com.game.dice.board.entity.Player;
import com.game.dice.board.support.Utils;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryGameBoardRepository implements GameBoardRepository {

    private Map<String, GameBoard> map = new ConcurrentHashMap<>();

    @Override
    public GameBoard joinPlayer(String boardId, Player player) {
        GameBoard gameBoard = getGameBoard(boardId);
        map.get(boardId).getPlayers().add(player);
        return gameBoard;
    }

    @Override
    public GameBoard createGameBoard(String name) {
        GameBoard newBoard = new GameBoard(name, Utils.uuid());
        map.put(newBoard.getId(), newBoard);
        return newBoard;
    }

    @Override
    public GameBoard getGameBoard(String boardId) {
        return map.get(boardId);
    }

    @Override
    public Player getPlayer(String boardId, String playerId) {
        return map.get(boardId).getPlayers().stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Player> getPlayers(String boardId) {
        return Collections.unmodifiableList(map.get(boardId).getPlayers());
    }
}
