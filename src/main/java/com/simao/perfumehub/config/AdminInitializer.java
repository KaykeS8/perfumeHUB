package com.simao.perfumehub.config;

import com.simao.perfumehub.entities.User;
import com.simao.perfumehub.entities.enums.Role;
import com.simao.perfumehub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ConditionalOnProperty(
        name = "spring.app.bootstrap-admin.enabled",
        havingValue = "true"
)
public class AdminInitializer {

    @Bean
    ApplicationRunner initializeAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${spring.app.bootstrap-admin.name}") String name,
            @Value("${spring.app.bootstrap-admin.email}") String email,
            @Value("${spring.app.bootstrap-admin.password}") String password

    ) {
        return args -> {
            if (email.isBlank() || password.isBlank()) {
                throw new IllegalArgumentException("Bootstrap admin credentials must be configured");
            }

            String normalizedEmail = email.trim().toLowerCase();
            if (userRepository.existsByEmail(normalizedEmail)) {
                return;
            }
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(Role.ADMIN);

            userRepository.save(user);
        };
    }
}
