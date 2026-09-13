package dev.mstefanov.learncrypto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final boolean h2ConsoleEnabled;

    public SecurityConfig(@Value("${spring.h2.console.enabled:false}") boolean h2ConsoleEnabled) {
        this.h2ConsoleEnabled = h2ConsoleEnabled;
    }

    /**
     * Authentication itself is provided by the single {@code UserDetailsService} bean
     * ({@code UserServiceImpl}) together with the {@code PasswordEncoder} bean; Spring Security
     * wires them into a DaoAuthenticationProvider automatically.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SessionRegistry sessionRegistry) throws Exception {
        http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/", "/index", "/users/login", "/users/login-error", "/users/register",
                            "/assets/**", "/error", "/actuator/health/**").permitAll();
                    if (h2ConsoleEnabled) {
                        // "local" profile only. PathRequest.toH2Console() may only be used while the
                        // console auto-configuration is active (it needs the H2ConsoleProperties bean).
                        auth.requestMatchers(PathRequest.toH2Console()).permitAll();
                    }
                    auth.requestMatchers("/home", "/basics", "/earn-crypto", "/trade-crypto", "/use-crypto",
                                    "/quiz", "/submit", "/result", "/charts").authenticated()
                            .anyRequest().authenticated();
                })
                .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"))
                .formLogin(form -> form
                        .loginPage("/users/login")
                        .defaultSuccessUrl("/home", true)
                        .failureForwardUrl("/users/login-error"))
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .sessionManagement(session -> session
                        .maximumSessions(100)
                        .maxSessionsPreventsLogin(false)
                        .expiredUrl("/users/login")
                        .sessionRegistry(sessionRegistry));

        if (h2ConsoleEnabled) {
            // The H2 web console posts without a CSRF token and renders itself in frames.
            http.csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()))
                    .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        }

        return http.build();
    }
}
