package com.books.bonnets.librarian.config;

import com.books.bonnets.librarian.entity.User;
import com.books.bonnets.librarian.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AdminBootstrapConfig {

    @Value("${app.bootstrap.admin.username}")
    private String adminUsername;

    @Value("${app.bootstrap.admin.password}")
    private String adminPassword;

    @Value("${app.bootstrap.admin.email}")
    private String adminEmail;

    @Bean
    public CommandLineRunner seedAdminUser(UserRepository userRepository,
                                           PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed the initial admin account only when there are no users yet
            if (userRepository.count() == 0) {
                User admin = User.builder()
                        .username(adminUsername)
                        .password(passwordEncoder.encode(adminPassword))
                        .email(adminEmail)
                        .roles(Set.of("ROLE_ADMIN"))
                        .build();

                userRepository.save(admin);
            }
        };
    }
}