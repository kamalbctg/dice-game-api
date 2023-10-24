package com.game.dice.board.controller;

import com.game.dice.board.entity.GameBoard;
import com.game.dice.board.entity.Player;
import com.game.dice.board.model.request.JoinRequest;
import com.game.dice.board.model.response.ApiResponse;
import com.game.dice.board.model.response.PlayerJoinedResponse;
import com.game.dice.board.service.GameBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

    public static final String BASE_ROUTE = "/api/player";
    public static final String JOIN_ROUTE = BASE_ROUTE + "/join";
    private GameBoardService gameBoardService;

    @Autowired
    public PlayerController(GameBoardService gameBoardService) {
        this.gameBoardService = gameBoardService;
    }

    @PostMapping(JOIN_ROUTE)
    public ApiResponse<PlayerJoinedResponse> join(@RequestBody JoinRequest joinRequest) {
        GameBoard board = gameBoardService.getGameBoard(joinRequest.getBoardId());
        Player player = gameBoardService.join(joinRequest.getBoardId(), joinRequest.getPlayer());
        return ApiResponse.successWithNoMessage(PlayerJoinedResponse.prepare(board, player));
    }
}
