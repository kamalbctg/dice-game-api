package hishab.app.game.dice.board.controller.impl;

import hishab.app.game.dice.board.controller.PlayerController;
import hishab.app.game.dice.board.entity.GameBoard;
import hishab.app.game.dice.board.entity.Player;
import hishab.app.game.dice.board.model.request.JoinRequest;
import hishab.app.game.dice.board.model.response.ApiResponse;
import hishab.app.game.dice.board.model.response.PlayerJoinedResponse;
import hishab.app.game.dice.board.service.GameBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class PlayerControllerImpl implements PlayerController {

    public static final String BASE_ROUTE = "/api/player";
    public static final String JOIN_ROUTE = BASE_ROUTE + "/join";
    private GameBoardService gameBoardService;

    @Autowired
    public PlayerControllerImpl(GameBoardService gameBoardService) {
        this.gameBoardService = gameBoardService;
    }

    @PostMapping(path = JOIN_ROUTE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ApiResponse<PlayerJoinedResponse> join(@RequestBody JoinRequest joinRequest) {
        GameBoard board = gameBoardService.getGameBoard(joinRequest.getBoardId());
        Player player = gameBoardService.join(joinRequest.getBoardId(), joinRequest.getPlayer());
        return ApiResponse.successWithNoMessage(PlayerJoinedResponse.prepare(board, player));
    }
}
