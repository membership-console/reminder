package cc.rits.membership.console.reminder.auth;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import cc.rits.membership.console.reminder.domain.model.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Authentication Filter
 */
public class CustomAuthenticationFilter extends BasicAuthenticationFilter {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.objectMapper = new ObjectMapper().configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
        throws IOException, ServletException {
        final var authentication = this.getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(final HttpServletRequest request) {
        final var authorizationHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).orElse("");
        if (!authorizationHeader.startsWith("User ")) {
            return null;
        }

        try {
            final var loginUser = this.objectMapper.readValue( //
                authorizationHeader.replace("User ", ""), //
                UserModel.class //
            );
            final var authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            return new UsernamePasswordAuthenticationToken(new LoginUserDetails(loginUser, authorities), null, authorities);
        } catch (final Exception e) {
            return null;
        }
    }

}
