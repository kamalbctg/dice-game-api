package hishab.app.game.dice.board.exception;

import hishab.app.game.dice.board.model.response.ApiResponse;
import hishab.app.game.dice.board.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
        return ApiResponse.error(ErrorDefinition.METHOD_NOT_ALLOWED.getStatus(), new ErrorResponse(ErrorDefinition.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public ApiResponse<ErrorResponse>  handleContentTypeSupported(Exception e) {
        log.warn(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase(), e);
        return ApiResponse.error(ErrorDefinition.UNSUPPORTED_MEDIA_TYPE.getStatus(), new ErrorResponse(ErrorDefinition.UNSUPPORTED_MEDIA_TYPE));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponse<ErrorResponse> handleException(Exception e) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e);
        return ApiResponse.error(ErrorDefinition.INTERNAL_SERVER_ERROR.getStatus(), new ErrorResponse(ErrorDefinition.INTERNAL_SERVER_ERROR));

    }
}
