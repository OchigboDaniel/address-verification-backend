package com.address_verification.addressVerificationApp.userAddress;

import com.address_verification.addressVerificationApp.userAddress.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface IUserService {


    public ResponseEntity<?> verifyUserAddress(int user_id);

}
