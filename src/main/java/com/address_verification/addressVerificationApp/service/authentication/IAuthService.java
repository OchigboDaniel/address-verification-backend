package com.address_verification.addressVerificationApp.service.authentication;

import com.address_verification.addressVerificationApp.ApiRespondsData;
import com.address_verification.addressVerificationApp.dto.LoginResponse;
import com.address_verification.addressVerificationApp.dto.request.CreateUserRequest;
import com.address_verification.addressVerificationApp.dto.request.LoginRequest;
import com.address_verification.addressVerificationApp.dto.response.UserResponse;

import java.util.Map;


public interface IAuthService {
    ApiRespondsData<UserResponse> createUser(CreateUserRequest userDTO);

    ApiRespondsData loginUser(LoginRequest loginRequest);
}
