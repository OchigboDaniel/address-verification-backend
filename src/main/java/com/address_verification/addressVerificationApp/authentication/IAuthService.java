package com.address_verification.addressVerificationApp.authentication;

import com.address_verification.addressVerificationApp.ApiRespondsData;
import com.address_verification.addressVerificationApp.userAddress.dto.request.CreateUserRequest;
import com.address_verification.addressVerificationApp.userAddress.dto.response.UserResponse;


public interface IAuthService {
    ApiRespondsData<UserResponse> createUser(CreateUserRequest userDTO);
}
