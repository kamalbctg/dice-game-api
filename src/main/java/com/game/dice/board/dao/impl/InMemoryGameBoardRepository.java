package com.game.dice.board.dao.impl;

import com.game.dice.board.dao.GameBoardRepository;
import com.game.dice.board.entity.GameBoard;
import com.game.dice.board.entity.Player;
import com.game.dice.board.exception.ApiException;
import com.game.dice.board.exception.ErrorDefinition;
import com.game.dice.board.support.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Repository
public class InMemoryGameBoardRepository implements GameBoardRepository {

    private Map<String, GameBoard> map = new ConcurrentHashMap<>();
    private Map<String, Lock> keyLocks = new ConcurrentHashMap<>();

    @Override
    public GameBoard joinPlayer(String boardId, Player player) {
        GameBoard gameBoard = getGameBoard(boardId);
        Lock lock = lock(boardId);
        lock.lock();
        try {
            if (gameBoard.numberOfPlayers() == 4) {
                log.warn("board {} was full", boardId);
                throw new ApiException(ErrorDefinition.BOARD_IS_FULL);
            }
            gameBoard.add(player);
        } finally {
            lock.unlock();
            log.info("unlock board {}", boardId);
        }
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
        GameBoard gameBoard = getGameBoard(boardId);
        return gameBoard.getPlayers().stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Player> getPlayers(String boardId) {
        return Collections.unmodifiableList(map.get(boardId).getPlayers());
    }

    @Override
    public GameBoard resetBoard(String boardId) {
        GameBoard board = getGameBoard(boardId);
        Lock lock = lock(boardId);
        lock.lock();
        try {
            for (Player player : board.getPlayers()) {
                player.setScore(0);
            }
        } finally {
            lock.unlock();
        }
        return board;
    }

    private Lock lock(String boardId) {
        log.info("create lock for {}", boardId);
        return keyLocks.computeIfAbsent(boardId, k -> new ReentrantLock());
    }
}
