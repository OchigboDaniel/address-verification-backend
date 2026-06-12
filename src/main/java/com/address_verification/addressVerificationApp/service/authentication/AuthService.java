package com.address_verification.addressVerificationApp.service.authentication;

import com.address_verification.addressVerificationApp.ApiRespondsData;
import com.address_verification.addressVerificationApp.JWTUtility;
import com.address_verification.addressVerificationApp.Role;
import com.address_verification.addressVerificationApp.dto.LoginResponse;
import com.address_verification.addressVerificationApp.dto.request.LoginRequest;
import com.address_verification.addressVerificationApp.exception.EmailException;
import com.address_verification.addressVerificationApp.repository.UserRepository;
import com.address_verification.addressVerificationApp.dto.request.CreateUserRequest;
import com.address_verification.addressVerificationApp.dto.response.UserResponse;
import com.address_verification.addressVerificationApp.userAddress.mapper.UserMapper;
import com.address_verification.addressVerificationApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService implements IAuthService{

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTUtility jwtUtility;

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Sign users
    public ApiRespondsData createUser(CreateUserRequest userDTO) {

        //Check if the email already exist
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailException("Email already exists");
        }

        //Encode the password
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));


        //Convert DTO to Entity
        User userEntity = UserMapper.convertToEntity(userDTO);
        userEntity.setRole(Role.USER);

        //Save user Entity in the database
        userRepository.save(userEntity);

        //Response Data
        UserResponse userResponse = new UserResponse(userEntity.getId(),userDTO.getFullName(), userDTO.getEmail());

        return  new ApiRespondsData<>("User created successfully", userResponse);
    }

    //Login Users
    @Override
    public ApiRespondsData<Map<String,String>> loginUser(LoginRequest loginRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword())
            );


        } catch (BadCredentialsException e) {
            throw e;
        }


            //Generate JWT token
        String jwtToken = jwtUtility.generateToken(loginRequest.getEmail());

        // Get user role
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = user.getRole();

        LoginResponse loginResponse = new LoginResponse(jwtToken, role);


        return new ApiRespondsData("User Authenticated", loginResponse);
    }
}
