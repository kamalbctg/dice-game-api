package com.game.dice.board.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RollRequest {
    private String boardId;

    private String playerId;
}
