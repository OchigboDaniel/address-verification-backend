package com.address_verification.addressVerificationApp.userAddress;

import com.address_verification.addressVerificationApp.controller.AuthenticationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


class UserAddressContollerTest {


    private AuthenticationController userAddressContoller;

    @BeforeEach
    void setUp() {
        userAddressContoller = new AuthenticationController();
    }

    @Test
    void shouldRetunWelcomeMessage() throws Exception {

        var result = userAddressContoller.welcome();

        assertEquals("welcome to address Verification API", result);
    }

}