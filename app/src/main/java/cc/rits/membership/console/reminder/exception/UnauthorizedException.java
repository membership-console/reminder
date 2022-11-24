package cc.rits.membership.console.reminder.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * 500 Unauthorized
 */
public class UnauthorizedException extends BaseException {

    /**
     * create unauthorized exception
     *
     * @param errorCode error code
     */
    public UnauthorizedException(final ErrorCode errorCode) {
        super(UNAUTHORIZED, errorCode);
    }

}
