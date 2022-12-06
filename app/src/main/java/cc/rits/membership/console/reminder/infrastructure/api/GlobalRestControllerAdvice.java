package cc.rits.membership.console.reminder.infrastructure.api;

import java.util.Locale;

import javax.annotation.Nullable;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import cc.rits.membership.console.reminder.exception.BaseException;
import cc.rits.membership.console.reminder.exception.ErrorCode;
import cc.rits.membership.console.reminder.infrastructure.api.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Global rest controller advice
 */
@Slf4j
@Hidden
@Controller
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalRestControllerAdvice extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Handle not found exception
     *
     * @return response entity
     */
    @RequestMapping("/api/**")
    public ResponseEntity<Object> handleApiNotFoundException() {
        return this.buildResponseEntity(ErrorCode.NOT_FOUND_API);
    }

    /**
     * Handle unexpected exception
     *
     * @param exception exception
     * @return response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        return this.buildResponseEntity(ErrorCode.UNEXPECTED_ERROR);
    }

    /**
     * Handle base exception
     *
     * @param exception exception
     * @return response entity
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleBaseException(final BaseException exception) {
        return this.buildResponseEntity(exception.getErrorCode());
    }

    /**
     * Handle constraint violation exception
     *
     * @param exception exception
     * @return response entity
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException exception) {
        return this.buildResponseEntity(ErrorCode.VALIDATION_ERROR);
    }

    /**
     * Handle method argument type mismatch exception
     *
     * @param exception exception
     * @return response entity
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception) {
        return this.buildResponseEntity(ErrorCode.INVALID_REQUEST_PARAMETER);
    }

    /**
     * Handle validation exception
     *
     * @param exception exception
     * @return response entity
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(final ValidationException exception) {
        final var causedException = exception.getCause();
        final var errorCode = causedException instanceof BaseException //
            ? ((BaseException) causedException).getErrorCode() //
            : ErrorCode.INVALID_REQUEST_PARAMETER;

        return this.buildResponseEntity(errorCode);
    }

    /**
     * Handle username not found exception
     * 
     * @param exception exception
     * @return then response entity
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(final UsernameNotFoundException exception) {
        return this.buildResponseEntity(ErrorCode.USER_NOT_LOGGED_IN);
    }

    /**
     * Handle method argument not valid exception
     *
     * @param exception exception
     * @param headers headers
     * @param status status
     * @param request request
     * @return the response entity
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@Nullable final MethodArgumentNotValidException exception,
        @Nullable final HttpHeaders headers, @Nullable final HttpStatusCode status, @Nullable final WebRequest request) {
        return this.buildResponseEntity(ErrorCode.INVALID_REQUEST_PARAMETER);
    }

    /**
     * Handle http message not readable exception
     *
     * @param exception exception
     * @param headers headers
     * @param status status
     * @param request request
     * @return the response entity
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@Nullable final HttpMessageNotReadableException exception,
        @Nullable final HttpHeaders headers, @Nullable final HttpStatusCode status, @Nullable final WebRequest request) {
        return this.buildResponseEntity(ErrorCode.INVALID_REQUEST_PARAMETER);
    }

    /**
     * エラーコードからエラーレスポンスを作成
     * 
     * @param errorCode エラーコード
     * @return エラーレスポンス
     */
    private ResponseEntity<Object> buildResponseEntity(final ErrorCode errorCode) {
        final var message = this.messageSource.getMessage(errorCode.getMessageKey(), null, Locale.ENGLISH);
        final var body = ErrorResponse.builder() //
            .code(errorCode.getCode()) //
            .message(message) //
            .build();

        if (errorCode.getHttpStatus().is4xxClientError()) {
            log.warn(String.format("%d: %s", errorCode.getCode(), message));
        } else if (errorCode.getHttpStatus().is5xxServerError()) {
            log.error(String.format("%d: %s", errorCode.getCode(), message));
        }

        return new ResponseEntity<>(body, errorCode.getHttpStatus());
    }

}
