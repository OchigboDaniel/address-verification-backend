package com.address_verification.addressVerificationApp.userAddress;

import com.address_verification.addressVerificationApp.userAddress.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/address-verification/api/v1")
public class UserAddressContoller {

    @Autowired
    UserService userService;

    @GetMapping("/welcome")
    public String welcome(){
        return "welcome to address Verification API";
    }

    @PostMapping("/user-verification")
    public UserDTO addUser(@RequestBody UserDTO userDTO){
        userService.verifyUserAddress(userDTO);
        return userDTO;
    }
}
