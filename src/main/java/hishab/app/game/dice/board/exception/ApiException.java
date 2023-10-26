package hishab.app.game.dice.board.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ErrorDefinition definition;

    public ApiException(ErrorDefinition definition) {
        super(definition.getMessage());
        this.definition = definition;
    }
}
