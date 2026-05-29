package com.address_verification.addressVerificationApp;

import com.address_verification.addressVerificationApp.authentication.AuthService;
import com.address_verification.addressVerificationApp.userAddress.UserService;
import com.address_verification.addressVerificationApp.userAddress.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user/address-verification/api/v1")
public class Contoller {

    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;

    @GetMapping("/welcome")
    public String welcome(){
        return "welcome to address Verification API";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO){
        authService.createUser(userDTO);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/addresses/{id}/verify-address")
    public ResponseEntity<?> verifyAddress(@PathVariable int id){
        return userService.verifyUserAddress(id);
    }
}
