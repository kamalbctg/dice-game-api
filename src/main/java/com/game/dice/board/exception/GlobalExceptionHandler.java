package com.game.dice.board.exception;

import com.game.dice.board.model.response.ApiResponse;
import com.game.dice.board.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.game.dice.board.exception.ErrorDefinition.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<ErrorResponse> handleApiException(ApiException e) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e);
        return ApiResponse.error(e.getDefinition().getStatus(), new ErrorResponse(e.getDefinition()));

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ApiResponse<ErrorResponse>  handleMethodNotSupported(Exception e) {
        log.warn(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), e);
        return ApiResponse.error(METHOD_NOT_ALLOWED.getStatus(), new ErrorResponse(METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public ApiResponse<ErrorResponse>  handleContentTypeSupported(Exception e) {
        log.warn(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase(), e);
        return ApiResponse.error(UNSUPPORTED_MEDIA_TYPE.getStatus(), new ErrorResponse(UNSUPPORTED_MEDIA_TYPE));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponse<ErrorResponse> handleException(Exception e) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e);
        return ApiResponse.error(INTERNAL_SERVER_ERROR.getStatus(), new ErrorResponse(INTERNAL_SERVER_ERROR));

    }
}
