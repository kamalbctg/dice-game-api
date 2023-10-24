package com.game.dice.board.model.response;

import com.game.dice.board.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerResponse {
    private String id;

    private String name;

    private int age;

    private int score;

    public static PlayerResponse build(Player player) {
        return new PlayerResponse(player.getId(), player.getName(), player.getAge(), player.getScore());
    }
}
