package com.address_verification.addressVerificationApp.userAddress;

import org.springframework.http.ResponseEntity;

public interface IUserService {


    public ResponseEntity<?> verifyUserAddress(int user_id);

}
