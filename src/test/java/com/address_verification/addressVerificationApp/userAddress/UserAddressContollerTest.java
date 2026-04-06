package com.address_verification.addressVerificationApp.userAddress;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class UserAddressContollerTest {


    private UserAddressContoller userAddressContoller;

    @BeforeEach
    void setUp() {
        userAddressContoller = new UserAddressContoller();
    }

    @Test
    void shouldRetunWelcomeMessage() throws Exception {

        var result = userAddressContoller.welcome();

        assertEquals("welcome to address Verification API", result);
    }

}