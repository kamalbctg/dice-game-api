package com.game.dice.board.model.response;

import com.game.dice.board.entity.GameBoard;
import com.game.dice.board.entity.PlayStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameBoardResponse {
    private String id;

    private String name;

    private PlayStatus play;

    private int numberOfPlayers;

    public static GameBoardResponse build(GameBoard gameBoard) {
        return new GameBoardResponse(gameBoard.getId(), gameBoard.getName(), PlayStatus.findById(gameBoard.getPlayStatus()), gameBoard.getPlayers().size());
    }
}
