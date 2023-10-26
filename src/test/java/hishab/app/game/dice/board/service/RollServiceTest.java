package hishab.app.game.dice.board.service;

import hishab.app.game.dice.board.config.GameBoardConf;
import hishab.app.game.dice.board.repository.GameBoardRepository;
import hishab.app.game.dice.board.entity.GameBoard;
import hishab.app.game.dice.board.entity.Player;
import hishab.app.game.dice.board.exception.ApiException;
import hishab.app.game.dice.board.service.impl.RollServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class RollServiceTest {

    @Mock
    GameBoardConf gameBoardConf;
    @Mock
    ScorerService scorerService;
    @Mock
    GameBoardRepository gameBoardRepository;

    @Test
    void rollAndScoreWithInvalidBoard() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";

        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(null);
        RollService rollService = new RollServiceImpl(gameBoardConf, scorerService, gameBoardRepository);

        assertThrows(ApiException.class, () -> {
            rollService.rollAndScore(boardId);
        });
        Mockito.verify(gameBoardRepository, Mockito.times(1)).getGameBoard(ArgumentMatchers.anyString());
    }

    @Test
    void rollAndScoreWithPlayerSizeZero() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";

        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(new GameBoard(boardName, boardId));
        RollService rollService = new RollServiceImpl(gameBoardConf, scorerService, gameBoardRepository);
        assertThrows(ApiException.class, () -> {
            rollService.rollAndScore(boardId);
        });
        Mockito.verify(gameBoardRepository, Mockito.times(1)).getGameBoard(ArgumentMatchers.anyString());
    }

    @Test
    void rollAndScoreWithPlayerSizeMoreThanFour() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        GameBoard board = new GameBoard(boardName, boardId);
        board.add(new Player("test-1", 20));
        board.add(new Player("test-2", 20));
        board.add(new Player("test-3", 20));
        board.add(new Player("test-4", 20));
        board.add(new Player("test-5", 20));

        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(board);
        RollService rollService = new RollServiceImpl(gameBoardConf, scorerService, gameBoardRepository);
        assertThrows(ApiException.class, () -> {
            rollService.rollAndScore(boardId);
        });
        Mockito.verify(gameBoardRepository, Mockito.times(1)).getGameBoard(ArgumentMatchers.anyString());
    }


    @Test
    void rollAndScoreWithScore6() {
        String boardName = "board name";
        String boardId = "e692c4a7-b588-4d6f-a587-4c767909e594";
        GameBoard board = new GameBoard(boardName, boardId);
        board.add(new Player("test-1", 20));
        board.add(new Player("test-2", 20));
        board.add(new Player("test-3", 20));
        board.add(new Player("test-4", 20));


        Mockito.when(gameBoardRepository.getGameBoard(ArgumentMatchers.anyString())).thenReturn(board);
        Mockito.when(scorerService.roll()).thenReturn(6);
        Mockito.when(gameBoardConf.getWinningScore()).thenReturn(25);
        Mockito.when(gameBoardConf.getStartScore()).thenReturn(6);
        Mockito.when(gameBoardConf.getPenaltyScore()).thenReturn(4);

        RollService rollService = new RollServiceImpl(gameBoardConf, scorerService, gameBoardRepository);
        rollService.rollAndScore(boardId);
        assertTrue(board.isPlayOff());
        assertEquals(30,board.getPlayers().get(0).getScore());
        Mockito.verify(gameBoardRepository, Mockito.times(1)).getGameBoard(ArgumentMatchers.anyString());
        Mockito.verify(scorerService, Mockito.times(7)).roll();
    }

    @Test
    void rollAndScoreWithScoreSequence_46244_46544() {

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
        .when(scorerService).roll();
        // @formatter:on

        Mockito.when(gameBoardConf.getWinningScore()).thenReturn(7);
        Mockito.when(gameBoardConf.getStartScore()).thenReturn(6);
        Mockito.when(gameBoardConf.getPenaltyScore()).thenReturn(4);


        RollService rollService = new RollServiceImpl(gameBoardConf, scorerService, gameBoardRepository);
        rollService.rollAndScore(boardId);
        assertTrue(board.isPlayOff());
        assertEquals(0,board.getPlayers().get(0).getScore());
        assertEquals(7, board.getPlayers().get(1).getScore());
        assertEquals(0, board.getPlayers().get(2).getScore());
        assertEquals(0,board.getPlayers().get(3).getScore());
        Mockito.verify(gameBoardRepository, Mockito.times(1)).getGameBoard(ArgumentMatchers.anyString());
        Mockito.verify(scorerService, Mockito.times(9)).roll();
    }


    @Test
    void rollAndScoreWithScoreSequence_46444_46544_46544() {

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
         .when(scorerService).roll();
        // @formatter:on

        Mockito.when(gameBoardConf.getWinningScore()).thenReturn(7);
        Mockito.when(gameBoardConf.getStartScore()).thenReturn(6);
        Mockito.when(gameBoardConf.getPenaltyScore()).thenReturn(4);


        RollService rollService = new RollServiceImpl(gameBoardConf, scorerService, gameBoardRepository);
        rollService.rollAndScore(boardId);
        assertTrue(board.isPlayOff());
        assertEquals(0,board.getPlayers().get(0).getScore());
        assertEquals(10, board.getPlayers().get(1).getScore());
        assertEquals(0,board.getPlayers().get(2).getScore());
        assertEquals(0,board.getPlayers().get(3).getScore());
        Mockito.verify(gameBoardRepository, Mockito.times(1)).getGameBoard(ArgumentMatchers.anyString());
        Mockito.verify(scorerService, Mockito.times(14)).roll();
    }


    @Test
    void rollAndScoreWithScoreSequence_46444_46544_46544s() {

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
        .when(scorerService).roll();
        // @formatter:on

        Mockito.when(gameBoardConf.getWinningScore()).thenReturn(10);
        Mockito.when(gameBoardConf.getStartScore()).thenReturn(6);
        Mockito.when(gameBoardConf.getPenaltyScore()).thenReturn(4);


        RollService rollService = new RollServiceImpl(gameBoardConf, scorerService, gameBoardRepository);
        rollService.rollAndScore(boardId);
        assertTrue(board.isPlayOff());
        assertEquals(0,board.getPlayers().get(0).getScore());
        assertEquals(10, board.getPlayers().get(1).getScore());
        assertEquals(0,board.getPlayers().get(2).getScore());
        assertEquals(0,board.getPlayers().get(3).getScore());
        Mockito.verify(gameBoardRepository, Mockito.times(1)).getGameBoard(ArgumentMatchers.anyString());
        Mockito.verify(scorerService, Mockito.times(15)).roll();
    }
}