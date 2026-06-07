package com.address_verification.addressVerificationApp.configuration;


import com.address_verification.addressVerificationApp.Role;
import com.address_verification.addressVerificationApp.userAddress.UserRepository;
import com.address_verification.addressVerificationApp.userAddress.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitializeAdmin {

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByEmail("manager@example.com")) {
                User manager = new User();
                manager.setFullName("Manager Manager");
                manager.setEmail("manager@example.com");
                manager.setPassword(passwordEncoder.encode("password"));
                manager.setRole(Role.MANAGER);

                userRepository.save(manager);
            }
        };
    }


}
