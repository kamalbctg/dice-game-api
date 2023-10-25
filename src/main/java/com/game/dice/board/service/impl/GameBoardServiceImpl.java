package com.game.dice.board.service.impl;

import com.game.dice.board.dao.GameBoardRepository;
import com.game.dice.board.entity.GameBoard;
import com.game.dice.board.entity.Player;
import com.game.dice.board.exception.ApiException;
import com.game.dice.board.exception.ErrorDefinition;
import com.game.dice.board.model.request.CreateBoardRequest;
import com.game.dice.board.model.request.RollRequest;
import com.game.dice.board.service.GameBoardService;
import com.game.dice.board.service.ScorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameBoardServiceImpl implements GameBoardService {
    private GameBoardRepository gameBoardRepository;

    private ScorerService scorerService;

    @Autowired
    public GameBoardServiceImpl(GameBoardRepository gameBoardRepository, ScorerService scorerService) {
        this.gameBoardRepository = gameBoardRepository;
        this.scorerService = scorerService;
    }

    @Override
    public Player join(String boardId, Player player) {
        GameBoard board = gameBoardRepository.getGameBoard(boardId);
        if (board == null) {
            throw new ApiException(ErrorDefinition.BOARD_NOT_FOUND);
        }

        if (player == null || player.getName().isBlank()) {
            throw new ApiException(ErrorDefinition.INSUFFICIENT_PLAYER_DATA);
        }
        gameBoardRepository.joinPlayer(boardId, player);
        return player;
    }


    @Override
    public GameBoard createBoard(CreateBoardRequest createBoardRequest) {
        return gameBoardRepository.createGameBoard(createBoardRequest.getName());
    }

    @Override
    public GameBoard resetBoard(String boardId) {
        return gameBoardRepository.resetBoard(boardId);
    }

    @Override
    public GameBoard roll(RollRequest rollRequest) {
        GameBoard board = gameBoardRepository.getGameBoard(rollRequest.getBoardId());
        Player player = gameBoardRepository.getPlayer(rollRequest.getBoardId(), rollRequest.getPlayerId());
        if (player == null) {
            throw new ApiException(ErrorDefinition.PLAYER_NOT_FOUND);
        }
        scorerService.roll();
        return board;
    }

    @Override
    public GameBoard getGameBoard(String boardId) {
        return gameBoardRepository.getGameBoard(boardId);
    }
}
