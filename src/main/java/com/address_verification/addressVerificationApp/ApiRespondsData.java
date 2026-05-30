package com.address_verification.addressVerificationApp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiRespondsData<T> {
    private String message;
    private T data;
}
