package hishab.app.game.dice.board.controller;

import hishab.app.game.dice.board.model.request.CreateBoardRequest;
import hishab.app.game.dice.board.model.response.ApiResponse;
import hishab.app.game.dice.board.model.response.GameBoardDetailsResponse;
import hishab.app.game.dice.board.model.response.GameBoardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface GameBoardController {
    @Operation(summary = "User can register new board to play game",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "user have to provide a name for the board",
                    content = {@Content(schema = @Schema(example = "{\n" +
                            "    \"name\":\"board-a\"\n" +
                            "}"
                    ))}
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            content = {@Content(schema = @Schema(example = "{\n" +
                                    "    \"status\": 200,\n" +
                                    "    \"data\": {\n" +
                                    "        \"id\": \"e4d90bb2-457d-43f1-9e96-4cde233c7f8c\",\n" +
                                    "        \"name\": \"board-a\",\n" +
                                    "        \"play\": \"OFF\",\n" +
                                    "        \"numberOfPlayers\": 0\n" +
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
    ApiResponse<GameBoardResponse> initiate(@RequestBody CreateBoardRequest createBoardRequest);

    @Operation(summary = "User can start roll specific board",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            content = {@Content(schema = @Schema(example = "{\n" +
                                    "    \"status\": 200,\n" +
                                    "    \"data\": {\n" +
                                    "        \"id\": \"706b020f-6e1e-4e9b-a688-1451497719ac\",\n" +
                                    "        \"name\": \"board-a\",\n" +
                                    "        \"play\": \"OFF\",\n" +
                                    "        \"numberOfPlayers\": 4\n" +
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
    ApiResponse<GameBoardResponse> start(@PathVariable String boardId);

    @Operation(summary = "User can get details of specific board",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            content = {@Content(schema = @Schema(example = "{\n" +
                                    "    \"status\": 200,\n" +
                                    "    \"data\": {\n" +
                                    "        \"id\": \"706b020f-6e1e-4e9b-a688-1451497719ac\",\n" +
                                    "        \"name\": \"board-a\",\n" +
                                    "        \"minPlayerRequired\": 2,\n" +
                                    "        \"maxPlayerAllowed\": 4,\n" +
                                    "        \"winningScore\": 26,\n" +
                                    "        \"penaltyScore\": 4,\n" +
                                    "        \"play\": \"OFF\",\n" +
                                    "        \"players\": [\n" +
                                    "            {\n" +
                                    "                \"id\": \"f06cf236-bbc3-4468-995e-e72a43739462\",\n" +
                                    "                \"name\": \"user1\",\n" +
                                    "                \"age\": 20,\n" +
                                    "                \"score\": 26\n" +
                                    "            },\n" +
                                    "            {\n" +
                                    "                \"id\": \"e414ecaf-f9d0-4acc-90c3-bf44dc65a097\",\n" +
                                    "                \"name\": \"user2\",\n" +
                                    "                \"age\": 20,\n" +
                                    "                \"score\": 19\n" +
                                    "            },\n" +
                                    "            {\n" +
                                    "                \"id\": \"d51243aa-67d8-4cdc-af21-e6ce51b401ce\",\n" +
                                    "                \"name\": \"user3\",\n" +
                                    "                \"age\": 20,\n" +
                                    "                \"score\": 0\n" +
                                    "            },\n" +
                                    "            {\n" +
                                    "                \"id\": \"673b55e1-78db-4201-8d87-a428e4ae02f1\",\n" +
                                    "                \"name\": \"user4\",\n" +
                                    "                \"age\": 20,\n" +
                                    "                \"score\": 18\n" +
                                    "            }\n" +
                                    "        ]\n" +
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
    ApiResponse<GameBoardDetailsResponse> status(@PathVariable String boardId);
}
