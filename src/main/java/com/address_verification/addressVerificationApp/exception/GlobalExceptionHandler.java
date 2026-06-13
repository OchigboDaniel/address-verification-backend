package com.address_verification.addressVerificationApp.exception;

import com.address_verification.addressVerificationApp.ApiRespondsData;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // Google API returned a 4xx error (bad request, invalid key, etc.)
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleGeoCodeClientError(HttpClientErrorException ex) {
        return ResponseEntity
                .status(502)
                .body(new ApiRespondsData<>("Geocoding service returned an error", null));
    }

    // Google API is unreachable or timed out
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, String>> handleGeoCodeServiceDown(ResourceAccessException ex) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("message", "Geocoding service is unavailable");

        return ResponseEntity
                .status(503)
                .body(messageMap);
    }


    @ExceptionHandler(EmailException.class)
    public ResponseEntity<?> handleEmailExists(EmailException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "message", "Incorrect email or password"
                ));
    }
}
