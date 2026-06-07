package com.address_verification.addressVerificationApp.configuration;


import com.address_verification.addressVerificationApp.Role;
import com.address_verification.addressVerificationApp.userAddress.AddressRepository;
import com.address_verification.addressVerificationApp.userAddress.UserRepository;
import com.address_verification.addressVerificationApp.userAddress.model.Address;
import com.address_verification.addressVerificationApp.userAddress.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class InitializeAdmin {

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           PasswordEncoder passwordEncoder, AddressRepository addressRepository) {
        return args -> {

            //Dome Address Data
            String[][] locations = {
                    {"Nigeria", "Lagos"},
                    {"Nigeria", "Abuja"},
                    {"Nigeria", "Lagos"},
                    {"United Kingdom", "England"},
                    {"United Kingdom", "Abuja"},
                    {"United States", "England"},
                    {"United States", "Abuja"},
                    {"Canada", "Lagos"},
                    {"Canada", "England"},
                    {"Canada", "England"}
            };

            // if the user DB is empty add data to it
            if (!userRepository.existsByEmail("manager@example.com")) {
                User manager = new User();
                manager.setFullName("Manager Manager");
                manager.setEmail("manager@example.com");
                manager.setPassword(passwordEncoder.encode("password"));
                manager.setRole(Role.MANAGER);

                userRepository.save(manager);

                for (int i = 1; i <= 20; i++) {

                    User user = new User();
                    user.setFullName("User" + i);
                    user.setEmail("user" + i + "@test.com");
                    user.setPassword(passwordEncoder.encode("password"));
                    user.setRole(Role.USER);

                    userRepository.save(user);

                }
            }

            // if the address repo is empty add data to it
            if (addressRepository.count() == 0) {

                List<User> roleUsers = userRepository.findAll()
                        .stream()
                        .filter(user -> user.getRole() == Role.USER)
                        .toList();

                for (int i = 0; i < roleUsers.size(); i++) {

                    String[] location = locations[i % locations.length];

                    User user = roleUsers.get(i);

                    Address address = new Address();
                    address.setLatitude(1.48943);
                    address.setLongitude(0.123432);
                    address.setCountry(location[0]);
                    address.setState(location[1]);
                    address.setFormattedAddress(location[1] + ", " + location[0]);

                    address.setUser(user);
                    user.setAddress(address); // if bidirectional

                    addressRepository.save(address);
                }
            }
        };
    }


}
