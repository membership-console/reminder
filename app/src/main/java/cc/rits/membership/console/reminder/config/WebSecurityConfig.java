package cc.rits.membership.console.reminder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import cc.rits.membership.console.reminder.auth.CustomAccessDeniedHandler;
import cc.rits.membership.console.reminder.auth.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;

/**
 * セキュリティの設定
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        // CORSを無効化
        http.cors().disable();

        // CSRF対策を無効化
        http.csrf().disable();

        // アクセス許可
        http.authorizeHttpRequests() //
            // REST API
            .requestMatchers("/api/batch/**", "/api/health").permitAll() //
            .requestMatchers("/api/**").hasRole("USER") //
            // 静的コンテンツ
            .requestMatchers("/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**", "/webjars/**").permitAll() //
            .requestMatchers("/**", "**.**").permitAll() //
            .anyRequest().authenticated();
        http.exceptionHandling() //
            // 未認証のユーザに適応されるハンドラ
            .authenticationEntryPoint(this.authenticationEntryPoint) //
            // 認証済みだが権限がないユーザに適応されるハンドラ
            .accessDeniedHandler(this.accessDeniedHandler);

        return http.build();
    }

}
