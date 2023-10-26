package hishab.app.game.dice.board.service.impl;

import hishab.app.game.dice.board.exception.ApiException;
import hishab.app.game.dice.board.exception.ErrorDefinition;
import hishab.app.game.dice.board.model.response.DiceRollResult;
import hishab.app.game.dice.board.service.ScorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ScorerServiceImpl implements ScorerService {


    private String rollDiceApi;
    private RestTemplate restTemplate;

    @Autowired
    public ScorerServiceImpl(RestTemplate restTemplate, @Value("${roll.dice.api}") String rollDiceApi) {
        this.restTemplate = restTemplate;
        this.rollDiceApi = rollDiceApi;
    }

    @Override
    public int roll() {
        DiceRollResult result = null;
        try {
            result = restTemplate.getForObject(rollDiceApi, DiceRollResult.class);
        } catch (Exception ex) {
            throw new ApiException(ErrorDefinition.INTERNAL_SERVER_ERROR);
        }
        return (result != null) ? result.getScore() : 0;
    }
}