package cc.rits.membership.console.reminder.auth;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import cc.rits.membership.console.reminder.exception.ErrorCode;
import cc.rits.membership.console.reminder.exception.ForbiddenException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Access Denied Handler
 */
@RequiredArgsConstructor
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
        final AccessDeniedException accessDeniedException) throws IOException, ServletException {
        final var exception = new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        this.handlerExceptionResolver.resolveException(request, response, null, exception);
    }

}
