package hishab.app.game.dice.board.model.response;

import hishab.app.game.dice.board.exception.ErrorDefinition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;

    public ErrorResponse(ErrorDefinition definition) {
        this.code = definition.getCode();
        this.message = definition.getMessage();
    }
}
