package com.game.dice.board.service.impl;

import com.game.dice.board.entity.Player;
import com.game.dice.board.exception.ApiException;
import com.game.dice.board.exception.ErrorDefinition;
import com.game.dice.board.model.response.DiceRollResult;
import com.game.dice.board.service.ScorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ScorerServiceImpl implements ScorerService {

    @Value("${roll.dice.api}")
    private String rollDiceApi;
    private RestTemplate restTemplate;

    @Autowired
    public ScorerServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public int roll(Player player) {
        DiceRollResult result = null;
        try {
            result = restTemplate.getForObject(this.rollDiceApi, DiceRollResult.class);
        } catch (Exception ex) {
            throw new ApiException(ErrorDefinition.INTERNAL_SERVER_ERROR);
        }
        return (result != null) ? result.getScore() : 0;
    }
}
