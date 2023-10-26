package hishab.app.game.dice.board.service.impl;

import hishab.app.game.dice.board.repository.GameBoardRepository;
import hishab.app.game.dice.board.entity.GameBoard;
import hishab.app.game.dice.board.entity.Player;
import hishab.app.game.dice.board.exception.ApiException;
import hishab.app.game.dice.board.exception.ErrorDefinition;
import hishab.app.game.dice.board.model.request.CreateBoardRequest;
import hishab.app.game.dice.board.service.GameBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameBoardServiceImpl implements GameBoardService {
    private GameBoardRepository gameBoardRepository;


    @Autowired
    public GameBoardServiceImpl(GameBoardRepository gameBoardRepository) {
        this.gameBoardRepository = gameBoardRepository;
    }

    @Override
    public Player join(String boardId, Player player) {
        GameBoard board = gameBoardRepository.getGameBoard(boardId);
        if (board == null) {
            throw new ApiException(ErrorDefinition.BOARD_NOT_FOUND);
        }

        if (player == null || player.getName().isBlank() || player.getAge() <= 0) {
            throw new ApiException(ErrorDefinition.INSUFFICIENT_PLAYER_DATA);
        }
        gameBoardRepository.joinPlayer(boardId, player);
        return player;
    }


    @Override
    public GameBoard createBoard(CreateBoardRequest createBoardRequest) {
        if (createBoardRequest == null || createBoardRequest.getName().isBlank()) {
            throw new ApiException(ErrorDefinition.INSUFFICIENT_BOARD_DATA);
        }
        return gameBoardRepository.createGameBoard(createBoardRequest.getName());
    }

    @Override
    public GameBoard resetBoard(String boardId) {
        GameBoard board = gameBoardRepository.getGameBoard(boardId);
        if (board == null) {
            throw new ApiException(ErrorDefinition.BOARD_NOT_FOUND);
        }

        return gameBoardRepository.resetBoard(boardId);
    }

    @Override
    public GameBoard getGameBoard(String boardId) {
        if (boardId == null || boardId.isBlank()) {
            throw new ApiException(ErrorDefinition.INSUFFICIENT_BOARD_DATA);
        }
        return gameBoardRepository.getGameBoard(boardId);
    }
}
