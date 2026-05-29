package com.address_verification.addressVerificationApp.authentication;

import com.address_verification.addressVerificationApp.userAddress.UserRepository;
import com.address_verification.addressVerificationApp.userAddress.dto.UserDTO;
import com.address_verification.addressVerificationApp.userAddress.mapper.UserMapper;
import com.address_verification.addressVerificationApp.userAddress.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService{

    @Autowired
    PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UserDTO userDTO) {

        //Check if the email already exist
        if(userRepository.existsByEmail(userDTO.getEmail())) throw new IllegalArgumentException("Email already exists");

        //Encoder password
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        //Convert DTO to Entity
        User newUserEntity = UserMapper.convertToEntity(userDTO);


        //Save user Entity in the database
        userRepository.save(newUserEntity);

    }
}
