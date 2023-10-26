package hishab.app.game.dice.board.model.request;

import hishab.app.game.dice.board.entity.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {
    private String boardId;

    private String name;

    private Integer age;

    public Player getPlayer() {
        return Player.builder()
                .name(name).age(age)
                .build();
    }
}
