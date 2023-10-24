package com.game.dice.board.entity;

import com.game.dice.board.config.GameBoardConf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameBoard {
    private volatile int playStatus = PlayStatus.OFF.getId();

    private String id;

    private String name;

    private List<Player> players = new ArrayList<>();

    public GameBoard(String name, String id) {
        this.id = id;
        this.name = name;
    }

    public void playOn() {
        playStatus = PlayStatus.ON.getId();
    }

    public void playOff() {
        playStatus = PlayStatus.OFF.getId();
    }

    public boolean isPlayOn() {
        return playStatus == PlayStatus.ON.getId();
    }

    public boolean isPlayOff() {
        return playStatus == PlayStatus.OFF.getId();
    }

    public boolean hasRequiredPlayers() {
        return players.size() >= GameBoardConf.MIN_PLAYER_REQUIRED;
    }

    public boolean isOpenToAddPlayer() {
        return players.size() == GameBoardConf.MAX_PLAYER_ALLOWED;
    }

    public void add(Player player) {
        players.add(player);
    }
}
