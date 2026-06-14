package com.address_verification.addressVerificationApp.configuration;

import com.address_verification.addressVerificationApp.Role;
import com.address_verification.addressVerificationApp.model.User;
import com.address_verification.addressVerificationApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitializeManager {


    @Value("${manager.email}")
    private String managerEmail;

    @Value("${manager.password}")
    private String managerPassword;



    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {

        return args -> {

            // Always create manager — runs in all profiles
            if (!userRepository.existsByEmail("manager@example.com")) {
                User manager = new User();
                manager.setFullName("Manager Manager");
                manager.setEmail(managerEmail);
                manager.setPassword(passwordEncoder.encode(managerPassword));
                manager.setRole(Role.MANAGER);
                userRepository.save(manager);
            }

        };
    }
}