package cc.rits.membership.console.reminder.auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import cc.rits.membership.console.reminder.exception.ErrorCode;
import cc.rits.membership.console.reminder.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Authentication Entry Point
 */
@RequiredArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void commence( //
        final HttpServletRequest request, //
        final HttpServletResponse response, //
        final AuthenticationException authException //
    ) {
        final var exception = new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN);
        this.handlerExceptionResolver.resolveException(request, response, null, exception);
    }

}
