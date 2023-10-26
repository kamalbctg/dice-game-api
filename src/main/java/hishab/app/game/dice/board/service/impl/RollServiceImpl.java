package hishab.app.game.dice.board.service.impl;

import hishab.app.game.dice.board.exception.ApiException;
import hishab.app.game.dice.board.exception.ErrorDefinition;
import hishab.app.game.dice.board.model.response.DiceRollResult;
import hishab.app.game.dice.board.service.RollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RollServiceImpl implements RollService {

    private String rollDiceApiUrl;
    private RestTemplate restTemplate;

    @Autowired
    public RollServiceImpl(RestTemplate restTemplate, @Value("${roll.dice.api}") String rollDiceApiUrl) {
        this.restTemplate = restTemplate;
        this.rollDiceApiUrl = rollDiceApiUrl;
    }

    @Override
    @Retryable(value = { ApiException.class }, maxAttempts = 3,
            backoff = @Backoff(delay = 100, multiplier = 2), listeners = "rollApiRetryListener")
    public int roll() {
        DiceRollResult result;
        try {
            result = restTemplate.getForObject(rollDiceApiUrl, DiceRollResult.class);
        } catch (Exception ex) {
            throw new ApiException(ErrorDefinition.INTERNAL_SERVER_ERROR);
        }
        return (result != null) ? result.getScore() : 0;
    }
}
