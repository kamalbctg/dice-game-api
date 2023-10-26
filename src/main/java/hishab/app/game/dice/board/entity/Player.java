package hishab.app.game.dice.board.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Player {
    private String id;
    private String name;
    private int age;
    private int score;
    private boolean startRolling;

    @Builder
    public Player(String name, Integer age) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.age = age;
    }

    public boolean isStartRolling() {
        return startRolling;
    }
}
