package com.address_verification.addressVerificationApp.controller;

import com.address_verification.addressVerificationApp.service.ValidateUtility;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ValidateUtilityBill {

    private final ValidateUtility verifyUtility;

    public ValidateUtilityBill(ValidateUtility verifyUtility){
        this.verifyUtility = verifyUtility;
    }

    @PostMapping("/validate-bill")
    public ResponseEntity<?> verifyUtilityBill(@RequestParam("bill") MultipartFile bill) {
        return ResponseEntity.ok().body(verifyUtility.verifyUtility(bill));
    }

}
