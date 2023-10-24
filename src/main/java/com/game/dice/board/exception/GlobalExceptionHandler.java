package com.game.dice.board.exception;

import com.game.dice.board.model.response.ApiResponse;
import com.game.dice.board.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.game.dice.board.exception.ErrorDefinition.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public ApiResponse<ErrorResponse> handleApiException(ApiException e) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e);
        return ApiResponse.error(e.getDefinition().getStatus(), new ErrorResponse(e.getDefinition()));

    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResponse<ErrorResponse> handleException(Exception e) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e);
        return ApiResponse.error(INTERNAL_SERVER_ERROR.getStatus(), new ErrorResponse(INTERNAL_SERVER_ERROR));

    }
}
