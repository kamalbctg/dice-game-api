package com.game.dice.board.service;

import com.game.dice.board.entity.Player;

/**
 * The interface Scorer service.
 */
public interface ScorerService {
    /**
     * Roll int.
     *
     * @param player the player
     * @return the int
     */
    int roll(Player player);
}
