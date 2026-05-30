package com.address_verification.addressVerificationApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> geoCodeAPIException(HttpClientErrorException ex){

        HttpStatusCode status = ex.getStatusCode();
        String message = ex.getMessage();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(ex.getResponseBodyAsString());

        return ResponseEntity
                .status(status)
                .body(ex.getResponseBodyAsString());
    }


    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<?> handleEmailExists(EmailAlreadyExistException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "message", ex.getMessage()
                ));
    }
}
