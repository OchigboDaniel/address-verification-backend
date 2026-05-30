package com.address_verification.addressVerificationApp.authentication;

import com.address_verification.addressVerificationApp.ApiRespondsData;
import com.address_verification.addressVerificationApp.exception.EmailAlreadyExistException;
import com.address_verification.addressVerificationApp.userAddress.UserRepository;
import com.address_verification.addressVerificationApp.userAddress.dto.request.CreateUserRequest;
import com.address_verification.addressVerificationApp.userAddress.dto.response.UserResponse;
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

    public ApiRespondsData<UserResponse> createUser(CreateUserRequest userDTO) {

        //Check if the email already exist
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistException("Email already exists");
        }


        //Convert DTO to Entity
        User newUserEntity = UserMapper.convertToEntity(userDTO);
        newUserEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        //Save user Entity in the database
        userRepository.save(newUserEntity);

        //Response Data
        UserResponse userResponse = new UserResponse(newUserEntity.getId(),userDTO.getFullName(), userDTO.getEmail());

        return  new ApiRespondsData<>("User created successfully", userResponse);
    }
}
