package com.address_verification.addressVerificationApp.userAddress;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/address-verification/api/v1")
public class UserAddressContoller {

    @GetMapping("/welcome")
    public String welcome(){
        return "welcome to address Verification API";
    }
}
