package dev.mstefanov.learncrypto.config;

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

    /**
     * Authentication itself is provided by the single {@code UserDetailsService} bean
     * ({@code UserServiceImpl}) together with the {@code PasswordEncoder} bean; Spring Security
     * wires them into a DaoAuthenticationProvider automatically.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SessionRegistry sessionRegistry) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index", "/users/login", "/users/login-error", "/users/register",
                                "/assets/**", "/error", "/actuator/health/**").permitAll()
                        // matches nothing unless spring.h2.console.enabled=true (the "local" profile)
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("/home", "/basics", "/earn-crypto", "/trade-crypto", "/use-crypto",
                                "/quiz", "/submit", "/result", "/charts").authenticated()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
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

        return http.build();
    }
}
