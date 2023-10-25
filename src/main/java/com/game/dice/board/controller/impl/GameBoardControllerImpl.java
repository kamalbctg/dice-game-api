package com.game.dice.board.controller.impl;

import com.game.dice.board.config.GameBoardConf;
import com.game.dice.board.controller.GameBoardController;
import com.game.dice.board.entity.GameBoard;
import com.game.dice.board.entity.PlayStatus;
import com.game.dice.board.exception.ApiException;
import com.game.dice.board.exception.ErrorDefinition;
import com.game.dice.board.model.request.CreateBoardRequest;
import com.game.dice.board.model.response.ApiResponse;
import com.game.dice.board.model.response.GameBoardDetailsResponse;
import com.game.dice.board.model.response.GameBoardResponse;
import com.game.dice.board.model.response.PlayerResponse;
import com.game.dice.board.service.GameBoardService;
import com.game.dice.board.service.RollService;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/game/board")
public class GameBoardControllerImpl implements GameBoardController {

    private GameBoardService gameBoardService;

    private RollService rollService;

    private GameBoardConf gameBoardConf;

    public GameBoardControllerImpl(GameBoardService gameBoardService, RollService rollService, GameBoardConf gameBoardConf) {
        this.gameBoardService = gameBoardService;
        this.rollService = rollService;
        this.gameBoardConf = gameBoardConf;
    }

    @PostMapping(path = "initiate", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Override
    public ApiResponse<GameBoardResponse> initiate(@RequestBody CreateBoardRequest createBoardRequest) {
        GameBoard gameBoard = gameBoardService.createBoard(createBoardRequest);
        return ApiResponse.successWithNoMessage(GameBoardResponse.build(gameBoard));
    }

    @GetMapping(path = "/start/{boardId}", produces = APPLICATION_JSON_VALUE)
    @Override
    public ApiResponse<GameBoardResponse> start(@PathVariable String boardId) {
        GameBoard gameBoard = gameBoardService.getGameBoard(boardId);
        if (gameBoard == null) {
            throw new ApiException(ErrorDefinition.BOARD_NOT_FOUND);
        }

        if (!gameBoard.hasRequiredPlayers()) {
            throw new ApiException(ErrorDefinition.BOARD_PLAY_INPROGRESS);
        }

        if (gameBoard.isPlayOn()) {
            throw new ApiException(ErrorDefinition.BOARD_PLAY_INPROGRESS);
        }
        gameBoardService.resetBoard(boardId);
        rollService.rollAndScore(boardId);
        return ApiResponse.successWithNoMessage(GameBoardResponse.build(gameBoard));
    }

    @GetMapping(path = "/status/{boardId}", produces = APPLICATION_JSON_VALUE)
    @Override
    public ApiResponse<GameBoardDetailsResponse> status(@PathVariable String boardId) {
        GameBoard gameBoard = gameBoardService.getGameBoard(boardId);
        if (gameBoard == null) {
            throw new ApiException(ErrorDefinition.BOARD_NOT_FOUND);
        }
        return ApiResponse.successWithNoMessage(GameBoardDetailsResponse.builder()
                .id(gameBoard.getId())
                .name(gameBoard.getName())
                .winningScore(gameBoardConf.getWinningScore())
                .penaltyScore(gameBoardConf.getPenaltyScore())
                .minPlayerRequired(GameBoardConf.MIN_PLAYER_REQUIRED)
                .maxPlayerAllowed(GameBoardConf.MAX_PLAYER_ALLOWED)
                .players(gameBoard.getPlayers().stream().map(PlayerResponse::build).collect(Collectors.toList()))
                .play(PlayStatus.findById(gameBoard.getPlayStatus()))
                .build());
    }
}
