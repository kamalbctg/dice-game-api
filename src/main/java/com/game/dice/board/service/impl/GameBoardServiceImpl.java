package com.game.dice.board.service.impl;

import com.game.dice.board.dao.GameBoardRepository;
import com.game.dice.board.entity.GameBoard;
import com.game.dice.board.entity.Player;
import com.game.dice.board.exception.ApiException;
import com.game.dice.board.exception.ErrorDefinition;
import com.game.dice.board.model.request.CreateBoardRequest;
import com.game.dice.board.service.GameBoardService;
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
