package com.address_verification.addressVerificationApp.controller;

import com.address_verification.addressVerificationApp.ApiRespondsData;
import com.address_verification.addressVerificationApp.dto.request.LoginRequest;
import com.address_verification.addressVerificationApp.service.authentication.AuthService;
import com.address_verification.addressVerificationApp.userAddress.UserService;
import com.address_verification.addressVerificationApp.dto.request.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class Authentication {

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


    @PostMapping("/login")
    public ResponseEntity<ApiRespondsData> userLogin(@RequestBody LoginRequest loginRequest){
        ApiRespondsData<?> result = authService.loginUser(loginRequest);
        return ResponseEntity.status(200).body(result);
    }

}
