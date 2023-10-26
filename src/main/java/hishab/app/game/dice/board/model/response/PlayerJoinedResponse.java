package hishab.app.game.dice.board.model.response;

import hishab.app.game.dice.board.entity.GameBoard;
import hishab.app.game.dice.board.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerJoinedResponse {
    private GameBoardResponse board;

    private PlayerResponse player;

    public static PlayerJoinedResponse prepare(GameBoard board, Player player) {
        return new PlayerJoinedResponse(GameBoardResponse.build(board), PlayerResponse.build(player));
    }
}
