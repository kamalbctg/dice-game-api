package com.game.dice.board.exception;

public enum ErrorDefinition {
    INTERNAL_SERVER_ERROR("00-00-00", "internal server error", 500),
    BOARD_NOT_FOUND("01-00-01", "board is not registered", 400),
    INSUFFICIENT_BOARD_DATA("01-00-01", "insufficient player ", 400),
    BOARD_IS_FULL("01-00-02", "board max payer limit reached", 400),
    BOARD_PLAYER_REQUIREMENT("01-00-03", "board do not have enough player", 400),
    BOARD_PLAY_INPROGRESS("01-00-04", "board do not have enough player", 400),
    INSUFFICIENT_PLAYER_DATA("02-00-01", "insufficient player ", 400),
    PLAYER_NOT_FOUND("02-00-02", "player is not registered", 400);

    private String code;
    private String message;
    private int status;

    ErrorDefinition(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
