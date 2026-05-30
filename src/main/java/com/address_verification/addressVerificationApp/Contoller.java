package com.address_verification.addressVerificationApp;

import com.address_verification.addressVerificationApp.authentication.AuthService;
import com.address_verification.addressVerificationApp.userAddress.UserService;
import com.address_verification.addressVerificationApp.userAddress.dto.request.CreateUserRequest;
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
    public ResponseEntity<ApiRespondsData> addUser(@RequestBody CreateUserRequest userDTO){
        ApiRespondsData result = authService.createUser(userDTO);
        return ResponseEntity.status(201).body(result);
    }

    @GetMapping("/addresses/{id}/verify-address")
    public ResponseEntity<?> verifyAddress(@PathVariable int id){
        return userService.verifyUserAddress(id);
    }
}
