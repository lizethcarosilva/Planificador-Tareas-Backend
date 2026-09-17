package com.generation.planificadortareasbackend.config;

import com.generation.planificadortareasbackend.model.User;
import com.generation.planificadortareasbackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Crea el usuario de demostración del proyecto (el mismo que antes vivía
 * hardcodeado en el frontend) si todavía no existe en la base de datos.
 */
@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDemoUser(UserRepository userRepository) {
        return args -> {
            String demoEmail = "lizethcaro@correo.com";

            if (!userRepository.existsByEmailIgnoreCase(demoEmail)) {
                User demoUser = User.builder()
                        .name("Lizeth Caro")
                        .email(demoEmail)
                        .password(new BCryptPasswordEncoder().encode("12345"))
                        .build();

                userRepository.save(demoUser);
            }
        };
    }
}
