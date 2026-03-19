package com.books.bonnets.librarian.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF because this API uses stateless JWT authentication.
                .csrf(csrf -> csrf.disable())

                // Do not create or store HTTP sessions; each request must carry its own token.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Return clear HTTP status codes for authentication and authorization failures.
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
                )

                // Define endpoint access rules based on roles and HTTP methods.
                .authorizeHttpRequests(auth -> auth
                        // Public authentication endpoints.
                        .requestMatchers("/auth/**").permitAll()

                        // Book endpoints: admin-only access.
                        .requestMatchers(HttpMethod.GET, "/api/books", "/api/book/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/books", "/api/book").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/book/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/book/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/book/**").hasRole("ADMIN")

                        // Customer endpoints: admin-only access.
                        .requestMatchers(HttpMethod.GET, "/api/customers", "/api/customer/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/customer").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/customer/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/customer/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/customer/**").hasRole("ADMIN")

                        // Purchase order listing is admin-only.
                        .requestMatchers(HttpMethod.GET, "/api/purchase_orders").hasRole("ADMIN")

                        // Creating orders is allowed for both admins and users.
                        .requestMatchers(HttpMethod.POST, "/api/purchase_order/**").hasAnyRole("ADMIN", "USER")

                        // Any other request must still be authenticated.
                        .anyRequest().authenticated()
                )

                // Add JWT processing before the default username/password filter.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt is used to hash user passwords before storing them.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Expose Spring Security's AuthenticationManager for login/authentication flow.
        return configuration.getAuthenticationManager();
    }
}