package hishab.app.game.dice.board.support;


import java.util.UUID;


public class Utils {
    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

}
