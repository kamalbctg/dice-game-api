package hishab.app.game.dice.board.controller.impl;

import hishab.app.game.dice.board.config.GameBoardConf;
import hishab.app.game.dice.board.controller.GameBoardController;
import hishab.app.game.dice.board.entity.GameBoard;
import hishab.app.game.dice.board.entity.PlayStatus;
import hishab.app.game.dice.board.exception.ApiException;
import hishab.app.game.dice.board.exception.ErrorDefinition;
import hishab.app.game.dice.board.model.request.CreateBoardRequest;
import hishab.app.game.dice.board.model.response.ApiResponse;
import hishab.app.game.dice.board.model.response.GameBoardDetailsResponse;
import hishab.app.game.dice.board.model.response.GameBoardResponse;
import hishab.app.game.dice.board.model.response.PlayerResponse;
import hishab.app.game.dice.board.service.GameBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/game/board")
public class GameBoardControllerImpl implements GameBoardController {

    private GameBoardService gameBoardService;

    private GameBoardConf gameBoardConf;

    @Autowired
    public GameBoardControllerImpl(GameBoardService gameBoardService, GameBoardConf gameBoardConf) {
        this.gameBoardService = gameBoardService;
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
        gameBoardService.play(boardId);
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
