package hishab.app.game.dice.board.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RollApiRetryListener extends RetryListenerSupport {

    @Value("${roll.dice.api}")
    private String rollDiceApiUrl;
    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable e) {
        log.error("failed to connect to: {}", rollDiceApiUrl,e);
    }
}
