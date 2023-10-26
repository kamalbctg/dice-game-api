package hishab.app.game.dice.board.service;

import hishab.app.game.dice.board.exception.ApiException;
import hishab.app.game.dice.board.model.response.DiceRollResult;
import hishab.app.game.dice.board.service.impl.RollServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RollServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @Test
    void testRollApiCallWithSuccess() {
        RollService rollService = new RollServiceImpl(restTemplate, "url");
        when(restTemplate.getForObject(anyString(), any())).thenReturn(new DiceRollResult(20));
        int score = rollService.roll();
        assertEquals(20, score);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(DiceRollResult.class));
    }

    @Test
    void testRollApiCallWithException() {
        RollService rollService = new RollServiceImpl(restTemplate, "url");
        when(restTemplate.getForObject(anyString(), any())).thenThrow(RestClientException.class);
        assertThrows(ApiException.class, () -> {
            rollService.roll();
        });
    }

    @Test
    void testRollApiCallWithResultNull() {
        RollService rollService = new RollServiceImpl(restTemplate, "url");
        when(restTemplate.getForObject(anyString(), any())).thenReturn(null);
        int score = rollService.roll();
        assertEquals(0, score);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(DiceRollResult.class));
    }
}