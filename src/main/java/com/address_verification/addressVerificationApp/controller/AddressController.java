package com.address_verification.addressVerificationApp.controller;

import com.address_verification.addressVerificationApp.ApiRespondsData;
import com.address_verification.addressVerificationApp.CreateCSVFile;
import com.address_verification.addressVerificationApp.dto.AddressDTO;
import com.address_verification.addressVerificationApp.dto.GeolocationDTO;
import com.address_verification.addressVerificationApp.dto.response.AddressResponse;
import com.address_verification.addressVerificationApp.service.VerifyAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    VerifyAddressService verifyAddressService;


    @PostMapping("/verify-address")
    public ResponseEntity<ApiRespondsData<AddressDTO>> verifyAddress(@RequestBody GeolocationDTO geolocationDTO){
        ApiRespondsData<AddressDTO> result = verifyAddressService.verifyUserAddress(geolocationDTO);
        return ResponseEntity.status(201).body(result);
    }

    @GetMapping("/address")
    public ResponseEntity<?> getAllAddress(
            @RequestParam(defaultValue = "csv") String format,
            @RequestParam(defaultValue = "false") String export){

        //call the address service and get data
        ApiRespondsData<List<AddressResponse>> apiRespondsData = verifyAddressService.getAllUserandAddress();

        if(!export.equals("false")) {

            String csvString = CreateCSVFile.setCSVString(apiRespondsData.getData());

            //Convert to CSV Byte
            byte[] csvBytes = csvString.getBytes(StandardCharsets.UTF_8);

            //Set the file Name
            String fileName = "address_" + System.currentTimeMillis() + ".csv";

            return ResponseEntity.ok()
                    .header("Content-Type", "text/csv")
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(csvBytes);
        }

        return ResponseEntity.ok()
                .body(apiRespondsData);
    }
}
