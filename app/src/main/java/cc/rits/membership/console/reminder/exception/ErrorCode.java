package cc.rits.membership.console.reminder.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * エラーコード
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 40000~40999: 400 Bad Request
     */
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, 40000, "exception.bad_request.validation_error"),

    INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, 40001, "exception.bad_request.invalid_request_parameter"),

    INVALID_NOTIFICATION_TITLE(HttpStatus.BAD_REQUEST, 40002, "exception.bad_request.invalid_notification_title"),

    INVALID_NOTIFICATION_BODY(HttpStatus.BAD_REQUEST, 40003, "exception.bad_request.invalid_notification_body"),

    USER_IDS_MUST_NOT_BE_EMPTY(HttpStatus.BAD_REQUEST, 40004, "exception.bad_request.user_ids_must_not_be_empty"),

    INVALID_NOTIFICATION_REMINDER_DATE(HttpStatus.BAD_REQUEST, 40005, "exception.bad_request.invalid_notification_reminder_date"),

    /**
     * 41000~41999: 401 Unauthorized
     */
    USER_NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, 41000, "exception.unauthorized.user_not_logged_in"),

    /**
     * 42000~42999: 403 Forbidden
     */
    USER_HAS_NO_PERMISSION(HttpStatus.FORBIDDEN, 42000, "exception.forbidden.user_has_no_permission"),

    /**
     * 43000:43999: 404 Not Found
     */
    NOT_FOUND_API(HttpStatus.NOT_FOUND, 43000, "exception.not_found.api"),

    NOT_FOUND_NOTIFICATION(HttpStatus.NOT_FOUND, 43001, "exception.not_found.notification"),

    NOT_FOUND_NOTIFICATION_REMINDER(HttpStatus.NOT_FOUND, 43002, "exception.not_found.notification_reminder"),

    /**
     * 45000~45999: 500 Internal Server Error
     */
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 45000, "exception.internal_server_error.unexpected_error");

    /**
     * HTTPステータスコード
     */
    private final HttpStatus httpStatus;

    /**
     * エラーコード
     */
    private final Integer code;

    /**
     * resources/i18n/messages.ymlのキーに対応
     */
    private final String messageKey;

}
