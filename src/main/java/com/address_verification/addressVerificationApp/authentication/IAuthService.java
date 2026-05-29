package com.address_verification.addressVerificationApp.authentication;

import com.address_verification.addressVerificationApp.userAddress.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    public void createUser(UserDTO userDTO);
}
