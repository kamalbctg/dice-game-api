package com.game.dice.board.controller;

import com.game.dice.board.controller.impl.PlayerControllerImpl;
import com.game.dice.board.entity.GameBoard;
import com.game.dice.board.entity.Player;
import com.game.dice.board.exception.ErrorDefinition;
import com.game.dice.board.service.GameBoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PlayerControllerImplTest {
    @MockBean
    GameBoardService gameBoardService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPlayerJoin() throws Exception {
        GameBoard board = new GameBoard("test", "1ed764d5-72b5-4905-be78-de2a4422bd0f");
        Player player = new Player("test", 35);

        String requestBody = "{\"boardId\": \"" + board.getId() + "\", \"name\": \"" + player.getName() + "\", \"age\": \"" + player.getAge() + "\"}";

        when(gameBoardService.getGameBoard(anyString())).thenReturn(board);
        when(gameBoardService.join(anyString(), any(Player.class))).thenReturn(player);

        mockMvc.perform(post(PlayerControllerImpl.JOIN_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        ;
    }

    @Test
    void testMethodNotAllowed() throws Exception {
        mockMvc.perform(get(PlayerControllerImpl.JOIN_ROUTE))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.data.code", equalTo(ErrorDefinition.METHOD_NOT_ALLOWED.getCode())))
                .andExpect(jsonPath("$.data.message", equalTo(ErrorDefinition.METHOD_NOT_ALLOWED.getMessage())));
    }

    @Test
    void testMethodContentTypeHeader() throws Exception {
        mockMvc.perform(post(PlayerControllerImpl.JOIN_ROUTE).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().is(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())))
                .andExpect(jsonPath("$.data.code", equalTo(ErrorDefinition.UNSUPPORTED_MEDIA_TYPE.getCode())))
                .andExpect(jsonPath("$.data.message", equalTo(ErrorDefinition.UNSUPPORTED_MEDIA_TYPE.getMessage())));
    }
}