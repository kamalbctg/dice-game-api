package hishab.app.game.dice.board.model.response;

import hishab.app.game.dice.board.entity.PlayStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameBoardDetailsResponse {
    private String id;

    private String name;

    private int minPlayerRequired;

    private int maxPlayerAllowed;

    private int winningScore;

    private int penaltyScore;

    private PlayStatus play;

    private List<PlayerResponse> players;
}
