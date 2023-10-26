package hishab.app.game.dice.board.service;

import hishab.app.game.dice.board.config.GameBoardConf;
import hishab.app.game.dice.board.repository.GameBoardRepository;
import hishab.app.game.dice.board.entity.GameBoard;
import hishab.app.game.dice.board.entity.Player;
import hishab.app.game.dice.board.exception.ApiException;
import hishab.app.game.dice.board.model.request.CreateBoardRequest;
import hishab.app.game.dice.board.service.impl.GameBoardServiceImpl;
import hishab.app.game.dice.board.support.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class GameBoardServiceTest {

    @Mock
    private GameBoardRepository gameBoardRepository;

    @Mock
    private RollService rollService;
    @Mock
    private GameBoardConf gameBoardConf;

    @Test
    void testPlayerJoinWithNullPlayer() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(new GameBoard(boardName, Utils.uuid()));

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);

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

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);

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

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);

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

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);

        assertThrows(ApiException.class, () -> {
            gameBoardService.join(boardId, testPlayer);
        });
    }

    @Test
    void testPlayerJoinWhenBoardNotExist() {
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        Player testPlayer = new Player("test-1", 20);
        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(null);

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);

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

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);
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

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);
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
        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);
        assertThrows(ApiException.class, () -> {
            gameBoardService.createBoard(createBoardRequest);
        });
    }

    @Test
    void testCreateBoardWithNullRequest() {
        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);
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

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);
        GameBoard board = gameBoardService.resetBoard(boardId);

        assertNotNull(board);
        Mockito.verify(gameBoardRepository, Mockito.times(1)).getGameBoard(ArgumentMatchers.anyString());
        Mockito.verify(gameBoardRepository, Mockito.times(1)).resetBoard(ArgumentMatchers.anyString());
    }

    @Test
    void testResetBoardWhenDoesNotExist() {
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(null);

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);
        assertThrows(ApiException.class, () -> {
            gameBoardService.resetBoard(boardId);
        });
    }

    @Test
    void testGetBoard() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";

        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(new GameBoard(boardName, boardId));

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);
        GameBoard board = gameBoardService.getGameBoard(boardId);

        assertNotNull(board);
        assertNotNull(board.getName());
        assertNotNull(board.getId());
        assertEquals(boardName, board.getName());
        Mockito.verify(gameBoardRepository, Mockito.times(1)).getGameBoard(ArgumentMatchers.anyString());
    }

    @Test
    void testBoardIdParamNull() {
        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);
        assertThrows(ApiException.class, () -> {
            gameBoardService.getGameBoard(null);
        });
    }

    @Test
    void testBoardIdParamEmpty() {
        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);
        assertThrows(ApiException.class, () -> {
            gameBoardService.getGameBoard("");
        });
    }

    @Test
    void testPlayWithInvalidBoard() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";

        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(null);
        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);

        assertThrows(ApiException.class, () -> {
            gameBoardService.play(boardId);
        });
        Mockito.verify(gameBoardRepository, Mockito.times(1)).getGameBoard(ArgumentMatchers.anyString());
    }

    @Test
    void testPlayWithPlayerSizeZero() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";

        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(new GameBoard(boardName, boardId));
        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);
        assertThrows(ApiException.class, () -> {
            gameBoardService.play(boardId);
        });
        Mockito.verify(gameBoardRepository, Mockito.times(1)).getGameBoard(ArgumentMatchers.anyString());
    }

    @Test
    void testPlayWithPlayerSizeMoreThanFour() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        GameBoard board = new GameBoard(boardName, boardId);
        board.add(new Player("test-1", 20));
        board.add(new Player("test-2", 20));
        board.add(new Player("test-3", 20));
        board.add(new Player("test-4", 20));
        board.add(new Player("test-5", 20));

        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(board);
        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);
        assertThrows(ApiException.class, () -> {
            gameBoardService.play(boardId);
        });
        Mockito.verify(gameBoardRepository, Mockito.times(1)).getGameBoard(ArgumentMatchers.anyString());
    }


    @Test
    void testPlayWithScore6() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        GameBoard board = new GameBoard(boardName, boardId);
        board.add(new Player("test-1", 20));
        board.add(new Player("test-2", 20));
        board.add(new Player("test-3", 20));
        board.add(new Player("test-4", 20));


        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(board);
        Mockito.when(rollService.roll()).thenReturn(6);
        Mockito.when(gameBoardConf.getWinningScore()).thenReturn(25);
        Mockito.when(gameBoardConf.getStartScore()).thenReturn(6);
        Mockito.when(gameBoardConf.getPenaltyScore()).thenReturn(4);

        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);
        gameBoardService.play(boardId);
        assertTrue(board.isPlayOff());
        assertEquals(30,board.getPlayers().get(0).getScore());
        Mockito.verify(gameBoardRepository, Mockito.times(2)).getGameBoard(ArgumentMatchers.anyString());
        Mockito.verify(rollService, Mockito.times(7)).roll();
    }

    @Test
    void testPlayWithScoreSequence_46244_46544() {

        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        GameBoard board = new GameBoard(boardName, boardId);
        board.add(new Player("test-1", 20));
        board.add(new Player("test-2", 20));
        board.add(new Player("test-3", 20));
        board.add(new Player("test-4", 20));


        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(board);
        // @formatter:off
        //player-sequence: 0,1,1,2,3,0,1,1,2,3
        doReturn(4)
        .doReturn(6)
           .doReturn(2)
           .doReturn(4)
        .doReturn(4)
        .doReturn(4)
        .doReturn(6)
          .doReturn(5)
        .doReturn(4)
        .doReturn(4)
        .when(rollService).roll();
        // @formatter:on

        Mockito.when(gameBoardConf.getWinningScore()).thenReturn(7);
        Mockito.when(gameBoardConf.getStartScore()).thenReturn(6);
        Mockito.when(gameBoardConf.getPenaltyScore()).thenReturn(4);


        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);
        gameBoardService.play(boardId);
        assertTrue(board.isPlayOff());
        assertEquals(0,board.getPlayers().get(0).getScore());
        assertEquals(7, board.getPlayers().get(1).getScore());
        assertEquals(0, board.getPlayers().get(2).getScore());
        assertEquals(0,board.getPlayers().get(3).getScore());
        Mockito.verify(gameBoardRepository, Mockito.times(2)).getGameBoard(ArgumentMatchers.anyString());
        Mockito.verify(rollService, Mockito.times(9)).roll();
    }


    @Test
    void testPlayWithScoreSequence_46444_46544_46544() {

        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        GameBoard board = new GameBoard(boardName, boardId);
        board.add(new Player("test-1", 20));
        board.add(new Player("test-2", 20));
        board.add(new Player("test-3", 20));
        board.add(new Player("test-4", 20));


        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(board);
        // @formatter:off
        //player-sequence: 0,1,1,2,3,0,1,1,2,3,0,1,1,2,3
        doReturn(4)
        .doReturn(6)
           .doReturn(4)
           .doReturn(4)
        .doReturn(4)

        .doReturn(4)
        .doReturn(6)
          .doReturn(5)
        .doReturn(4)
        .doReturn(4)

        .doReturn(4)
        .doReturn(6)
          .doReturn(5)
        .doReturn(4)
          .doReturn(4)
        .when(rollService).roll();
        // @formatter:on

        Mockito.when(gameBoardConf.getWinningScore()).thenReturn(7);
        Mockito.when(gameBoardConf.getStartScore()).thenReturn(6);
        Mockito.when(gameBoardConf.getPenaltyScore()).thenReturn(4);


        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);
        gameBoardService.play(boardId);
        assertTrue(board.isPlayOff());
        assertEquals(0,board.getPlayers().get(0).getScore());
        assertEquals(10, board.getPlayers().get(1).getScore());
        assertEquals(0,board.getPlayers().get(2).getScore());
        assertEquals(0,board.getPlayers().get(3).getScore());
        Mockito.verify(gameBoardRepository, Mockito.times(2)).getGameBoard(ArgumentMatchers.anyString());
        Mockito.verify(rollService, Mockito.times(14)).roll();
    }


    @Test
    void testPlayWithScoreSequence_46444_46544_46544s() {

        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        GameBoard board = new GameBoard(boardName, boardId);
        board.add(new Player("test-1", 20));
        board.add(new Player("test-2", 20));
        board.add(new Player("test-3", 20));
        board.add(new Player("test-4", 20));


        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(board);
        // @formatter:off
        //player-sequence: 0,1,1,2,3,0,1,1,2,3,0,1,1,2,3
        doReturn(4)
        .doReturn(6)
          .doReturn(6)
          .doReturn(1)
        .doReturn(4)
        .doReturn(4)

        .doReturn(4)
        .doReturn(6)
           .doReturn(4)
        .doReturn(4)
           .doReturn(4)

        .doReturn(4)
        .doReturn(6)
           .doReturn(3)
        .doReturn(4)
        .doReturn(4)
        .when(rollService).roll();
        // @formatter:on

        Mockito.when(gameBoardConf.getWinningScore()).thenReturn(10);
        Mockito.when(gameBoardConf.getStartScore()).thenReturn(6);
        Mockito.when(gameBoardConf.getPenaltyScore()).thenReturn(4);


        GameBoardService gameBoardService = new GameBoardServiceImpl(gameBoardRepository, gameBoardConf, rollService);
        gameBoardService.play(boardId);
        assertTrue(board.isPlayOff());
        assertEquals(0,board.getPlayers().get(0).getScore());
        assertEquals(10, board.getPlayers().get(1).getScore());
        assertEquals(0,board.getPlayers().get(2).getScore());
        assertEquals(0,board.getPlayers().get(3).getScore());
        Mockito.verify(gameBoardRepository, Mockito.times(2)).getGameBoard(ArgumentMatchers.anyString());
        Mockito.verify(rollService, Mockito.times(15)).roll();
    }
}