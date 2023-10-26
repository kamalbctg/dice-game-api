package hishab.app.game.dice.board.controller;

import hishab.app.game.dice.board.model.request.JoinRequest;
import hishab.app.game.dice.board.model.response.ApiResponse;
import hishab.app.game.dice.board.model.response.PlayerJoinedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.RequestBody;

public interface PlayerController {
    @Operation(summary = "User can join a specific board",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "user have to provide a name for the board",
                    content = {@Content(schema = @Schema(example = "{\n" +
                            "    \"boardId\":\"706b020f-6e1e-4e9b-a688-1451497719ac\",\n" +
                            "    \"name\": \"user4\",\n" +
                            "    \"age\": 20\n" +
                            "}"))}
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            content = {@Content(schema = @Schema(example = "{\n" +
                                    "    \"status\": 200,\n" +
                                    "    \"data\": {\n" +
                                    "        \"board\": {\n" +
                                    "            \"id\": \"706b020f-6e1e-4e9b-a688-1451497719ac\",\n" +
                                    "            \"name\": \"board-a\",\n" +
                                    "            \"play\": \"OFF\",\n" +
                                    "            \"numberOfPlayers\": 4\n" +
                                    "        },\n" +
                                    "        \"player\": {\n" +
                                    "            \"id\": \"673b55e1-78db-4201-8d87-a428e4ae02f1\",\n" +
                                    "            \"name\": \"user4\",\n" +
                                    "            \"age\": 20,\n" +
                                    "            \"score\": 0\n" +
                                    "        }\n" +
                                    "    }\n" +
                                    "}"))}
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "4XX",
                            content = {@Content(schema = @Schema(example = "{\n" +
                                    "    \"status\": 400,\n" +
                                    "    \"data\": {\n" +
                                    "        \"code\": \"01-00-01\",\n" +
                                    "        \"message\": \"board is not registered\"\n" +
                                    "    }\n" +
                                    "}"))}
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "5XX",
                            content = {@Content(schema = @Schema(example = "{\n" +
                                    "    \"status\": 500,\n" +
                                    "    \"data\": {\n" +
                                    "        \"code\": \"00-00-00\",\n" +
                                    "        \"message\": \"internal server error\"\n" +
                                    "    }\n" +
                                    "}"))}
                    )
            }
    )
    ApiResponse<PlayerJoinedResponse> join(@RequestBody JoinRequest joinRequest);
}
