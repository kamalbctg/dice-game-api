package com.game.dice.board.controller;

import com.game.dice.board.entity.GameBoard;
import com.game.dice.board.entity.Player;
import com.game.dice.board.service.GameBoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlayerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    GameBoardService gameBoardService;

    @Test
    void testPlayerJoin() throws Exception {
        GameBoard board = new GameBoard("test", "1ed764d5-72b5-4905-be78-de2a4422bd0f");
        Player player = new Player("test", 35);

        String requestBody = "{\"boardId\": \""+board.getId()+"\", \"name\": \""+player.getName()+"\", \"age\": \""+player.getAge()+"\"}";

        when(gameBoardService.getGameBoard(anyString())).thenReturn(board);
        when(gameBoardService.join(anyString(),any(Player.class) )).thenReturn(player);

        mockMvc.perform(MockMvcRequestBuilders.post(PlayerController.JOIN_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));;
    }
}