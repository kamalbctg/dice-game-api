package hishab.app.game.dice.board.entity;

import java.util.Arrays;

public enum PlayStatus {
    ON(1), OFF(2);
    private int id;

    PlayStatus(int id) {
        this.id = id;
    }

    public static PlayStatus findById(int status) {
        return Arrays.stream(PlayStatus.values()).filter(s -> s.id == status).findFirst().orElse(null);
    }

    public int getId() {
        return id;
    }
}
