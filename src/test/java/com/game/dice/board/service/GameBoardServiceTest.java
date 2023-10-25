package com.game.dice.board.service;

import com.game.dice.board.dao.GameBoardRepository;
import com.game.dice.board.entity.GameBoard;
import com.game.dice.board.entity.Player;
import com.game.dice.board.exception.ApiException;
import com.game.dice.board.model.request.CreateBoardRequest;
import com.game.dice.board.service.impl.GameBoardServiceImpl;
import com.game.dice.board.support.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameBoardServiceTest {

    @Mock
    private GameBoardRepository gameBoardRepository;

    @Mock
    private ScorerService scorerService;

    @Test
    void testPlayerJoinWithNullPlayer() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(new GameBoard(boardName, Utils.uuid()));

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository);

        assertThrows(ApiException.class, () -> {
            gameBoardService.join(boardId, null);
        });
    }

    @Test
    void testPlayerJoinWithEmptyName() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        Player testPlayer = new Player("", 20);
        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(new GameBoard(boardName, Utils.uuid()));

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository);

        assertThrows(ApiException.class, () -> {
            gameBoardService.join(boardId, testPlayer);
        });
    }

    @Test
    void testPlayerJoinWithWithAgeZero() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        Player testPlayer = new Player("", 0);
        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(new GameBoard(boardName, Utils.uuid()));

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository);

        assertThrows(ApiException.class, () -> {
            gameBoardService.join(boardId, testPlayer);
        });
    }

    @Test
    void testPlayerJoinWithWithNegativeAge() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        Player testPlayer = new Player("", -1);
        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(new GameBoard(boardName, Utils.uuid()));

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository);

        assertThrows(ApiException.class, () -> {
            gameBoardService.join(boardId, testPlayer);
        });
    }

    @Test
    void testPlayerJoinWhenBoardNotExist() {
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        Player testPlayer = new Player("test-1", 20);
        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(null);

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository);

        assertThrows(ApiException.class, () -> {
            gameBoardService.join(boardId, testPlayer);
        });
    }

    @Test
    void testPlayerJoin() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        Player testPlayer = new Player("test-1", 20);
        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(new GameBoard(boardName, Utils.uuid()));

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository);
        Player joinedPlayer = gameBoardService.join(boardId, testPlayer);

        assertNotNull(joinedPlayer);
        assertNotNull(joinedPlayer.getName());
        assertNotNull(joinedPlayer.getId());
        assertEquals(testPlayer.getName(), joinedPlayer.getName());
        assertEquals(testPlayer.getAge(), joinedPlayer.getAge());
        assertEquals(0, joinedPlayer.getScore());
    }

    @Test
    void testCreateBoard() {
        String boardName = "board name";

        CreateBoardRequest createBoardRequest = new CreateBoardRequest();
        createBoardRequest.setName(boardName);
        Mockito.when(gameBoardRepository.createGameBoard(ArgumentMatchers.anyString())).thenReturn(new GameBoard(boardName, Utils.uuid()));

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository);
        GameBoard board = gameBoardService.createBoard(createBoardRequest);

        assertNotNull(board);
        assertNotNull(board.getName());
        assertNotNull(board.getId());
        assertEquals(boardName, board.getName());
    }

    @Test
    void testCreateBoardWithEmptyNameInRequest() {
        String boardName = "";
        CreateBoardRequest createBoardRequest = new CreateBoardRequest();
        createBoardRequest.setName(boardName);
        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository);
        assertThrows(ApiException.class, () -> {
            gameBoardService.createBoard(createBoardRequest);
        });
    }

    @Test
    void testCreateBoardWithNullRequest() {
        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository);
        assertThrows(ApiException.class, () -> {
            gameBoardService.createBoard(null);
        });
    }

    @Test
    void testResetBoard() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";

        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(new GameBoard(boardName, boardId));
        Mockito.when(gameBoardRepository.resetBoard(ArgumentMatchers.anyString())).thenReturn(new GameBoard(boardName, boardId));

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository);
        GameBoard board = gameBoardService.resetBoard(boardId);

        assertNotNull(board);
        Mockito.verify(gameBoardRepository, Mockito.times(1)).getGameBoard(ArgumentMatchers.anyString());
        Mockito.verify(gameBoardRepository, Mockito.times(1)).resetBoard(ArgumentMatchers.anyString());
    }

    @Test
    void testResetBoardWhenDoesNotExist() {
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(null);

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository);
        assertThrows(ApiException.class, () -> {
            gameBoardService.resetBoard(boardId);
        });
    }

    @Test
    void testGetBoard() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";

        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(new GameBoard(boardName, boardId));

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository);
        GameBoard board = gameBoardService.getGameBoard(boardId);

        assertNotNull(board);
        assertNotNull(board.getName());
        assertNotNull(board.getId());
        assertEquals(boardName, board.getName());
        Mockito.verify(gameBoardRepository, Mockito.times(1)).getGameBoard(ArgumentMatchers.anyString());
    }

    @Test
    void testBoardIdParamNull() {
        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository);
        assertThrows(ApiException.class, () -> {
            gameBoardService.getGameBoard(null);
        });
    }

    @Test
    void testBoardIdParamEmpty() {
        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository);
        assertThrows(ApiException.class, () -> {
            gameBoardService.getGameBoard("");
        });
    }
}