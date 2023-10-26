package hishab.app.game.dice.board.repository.impl;

import hishab.app.game.dice.board.entity.GameBoard;
import hishab.app.game.dice.board.entity.PlayStatus;
import hishab.app.game.dice.board.entity.Player;
import hishab.app.game.dice.board.exception.ApiException;
import hishab.app.game.dice.board.support.UtilsTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryGameBoardRepositoryTest {

    @Test
    void testJoinPlayer() {
        InMemoryGameBoardRepository repository = new InMemoryGameBoardRepository();

        GameBoard newBoard = repository.createGameBoard("test=board");
        Player player = new Player("player-1", 20);

        GameBoard board = repository.joinPlayer(newBoard.getId(), player);

        assertNotNull(board);
        assertNotNull(board.getPlayers());
        assertEquals(1,board.numberOfPlayers());
        Assertions.assertTrue(UtilsTest.isValidUuid(board.getPlayers().get(0).getId()));

    }

    @Test
    void testJoinWhenBoardIsFull() {
        InMemoryGameBoardRepository repository = new InMemoryGameBoardRepository();

        GameBoard newBoard = repository.createGameBoard("test=board");
        newBoard.add(new Player("player-1", 20));
        newBoard.add(new Player("player-2", 20));
        newBoard.add(new Player("player-3", 20));
        newBoard.add(new Player("player-4", 20));
        assertThrows(ApiException.class, () -> {
            repository.joinPlayer(newBoard.getId(), new Player("player-5", 20));
        });
    }

    @Test
    void createGameBoard() {
        String boardName = "test=board";
        InMemoryGameBoardRepository repository = new InMemoryGameBoardRepository();

        GameBoard board = repository.createGameBoard("test=board");

        assertNotNull(board);
        assertNotNull(board.getId());
        assertNotNull(board.getName());
        assertEquals(boardName,board.getName());
        assertEquals(0,board.numberOfPlayers());
        Assertions.assertEquals(PlayStatus.OFF.getId(), board.getPlayStatus());
        assertTrue(UtilsTest.isValidUuid(board.getId()));
    }

    @Test
    void testCreateBoard() {
        InMemoryGameBoardRepository repository = new InMemoryGameBoardRepository();
        GameBoard board = repository.createGameBoard("test=board");

        assertNotNull(board);
        assertNotNull(board.getId());
        assertNotNull(board.getName());
        assertTrue(UtilsTest.isValidUuid(board.getId()));
    }

    @Test
    void testGetPlayer() {
        String boardName = "test=board";
        String playerName = "test-1";
        int age = 20;
        InMemoryGameBoardRepository repository = new InMemoryGameBoardRepository();
        GameBoard board = repository.createGameBoard(boardName);
        board.add(new Player(playerName, age));

        Player player = repository.getPlayer(board.getId(), board.getPlayers().get(0).getId());

        assertNotNull(board);
        assertNotNull(board.getId());
        assertNotNull(board.getName());
        assertNotNull(player);
        assertNotNull(player.getName());
        assertTrue(UtilsTest.isValidUuid(board.getId()));
        assertTrue(UtilsTest.isValidUuid(player.getId()));
        assertEquals(playerName, player.getName());
        assertEquals(age, player.getAge());


    }

    @Test
    void testGetPlayers() {
        InMemoryGameBoardRepository repository = new InMemoryGameBoardRepository();
        Player one = new Player("player-1", 20);
        one.setScore(2);
        Player two = new Player("player-2", 20);
        one.setScore(25);
        GameBoard nweBoard = repository.createGameBoard("test=board");
        nweBoard.add(one);
        nweBoard.add(two);

        List<Player> players = repository.getPlayers(nweBoard.getId());


        assertAll("Players should not be null",
                () -> assertNotNull(players.get(0)),
                () -> assertNotNull(players.get(1))
        );
    }

    @Test
    void testResetBoard() {
        InMemoryGameBoardRepository repository = new InMemoryGameBoardRepository();
        Player one = new Player("player-1", 20);
        one.setScore(2);
        Player two = new Player("player-1", 20);
        one.setScore(25);
        GameBoard board = repository.createGameBoard("test=board");
        board.add(one);
        board.add(two);

        repository.resetBoard(board.getId());


        assertAll("Score should reset to zero",
                () -> assertEquals(0, board.getPlayers().get(0).getScore()),
                () -> assertEquals(0, board.getPlayers().get(1).getScore())
        );
    }
}