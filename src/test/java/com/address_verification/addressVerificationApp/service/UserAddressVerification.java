package com.address_verification.addressVerificationApp.service;

import com.address_verification.addressVerificationApp.ApiRespondsData;
import com.address_verification.addressVerificationApp.dto.AddressDTO;
import com.address_verification.addressVerificationApp.dto.GeolocationDTO;
import com.address_verification.addressVerificationApp.model.Address;
import com.address_verification.addressVerificationApp.model.User;
import com.address_verification.addressVerificationApp.repository.AddressRepository;
import com.address_verification.addressVerificationApp.repository.UserRepository;
import com.address_verification.addressVerificationApp.service.common.GeoCodeApiResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for VerifyAddressService.
 * Dependencies (RestTemplate, UserRepository, AddressRepository) are mocked
 * so tests run in isolation without a real database or Google API connection.
 */
@ExtendWith(MockitoExtension.class)
public class UserAddressVerification {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    // Mocked to avoid real HTTP calls to the Google Geocoding API
    @Mock
    private RestTemplate restTemplate;

    // Real instance of the service being tested — mocks injected automatically
    @InjectMocks
    private VerifyAddressService verifyAddressService;

    /**
     * Clear the security context after each test to prevent
     * authentication state leaking between tests.
     */
    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Success case — new user with no existing address.
     * Verifies that:
     * - Google API response is parsed correctly
     * - Address is saved to the database
     * - Response message confirms successful verification
     */
    @Test
    void verifyUserAddress_success() {


        // Build a mock Google Geocoding API response
        // Mirrors the nested Map structure returned by RestTemplate
        Map<String, Object> googleResponseBody = GeoCodeApiResponse.buildMockGeoCodeApiResponse();

        // Simulate an authenticated user in the Spring Security context
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "test@example.com", null, List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tell RestTemplate to return the mock Google API response
        // any() matchers used because exact URL and headers are not relevant to this test
        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(ResponseEntity.ok(googleResponseBody));

        // Build a user with no existing address — tests the "create new address" branch
        User user = new User();
        user.setEmail("test@example.com");
        user.setAddress(null);

        // Tell userRepository to return the mock user when queried by email
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        // Build the geolocation input DTO sent from the frontend
        GeolocationDTO geolocation = new GeolocationDTO();
        geolocation.setLatitude(6.4666);
        geolocation.setLongitude(3.5566);

        // --- Act ---
        ApiRespondsData<AddressDTO> result = verifyAddressService.verifyUserAddress(geolocation);

        // --- Assert ---
        // Verify the service returns the expected success message
        assertEquals("Address Verified", result.getMessage());
    }

    @Test
    void  verifyUserAddress_googleApiDown(){

        // Build the geolocation input DTO sent from the frontend
        GeolocationDTO geolocation = new GeolocationDTO();
        geolocation.setLatitude(6.4666);
        geolocation.setLongitude(3.5566);

        //Emulate an authenticated user in the Spring Security context
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "test@example.com", null, List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ;

        // Tell RestTemplate to return the mock Google API response
        // any() matchers used because exact URL and headers are not relevant to this test
        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenThrow(new ResourceAccessException("Geocoding service is unavailable"));

        // Assert that the exception propagates out of the service
        assertThrows( ResourceAccessException.class, () -> {
            verifyAddressService.verifyUserAddress(geolocation);
        });

    }

    @Test
    void verifyUserAddress_updatesExistingAddress(){

        // Build a mock Google Geocoding API response
        // Mirrors the nested Map structure returned by RestTemplate
        Map<String, Object> googleResponseBody = GeoCodeApiResponse.buildMockGeoCodeApiResponse();


        // Simulate an authenticated user in the Spring Security context
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "test@example.com", null, List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tell RestTemplate to return the mock Google API response
        // any() matchers used because exact URL and headers are not relevant to this test
        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(ResponseEntity.ok(googleResponseBody));

        // Build a user with no existing address — tests the "create new address" branch
        User user = new User();
        user.setEmail("test@example.com");
        Address address = new Address();
                address.setId(23);
                address.setLatitude(6.4422);
                address.setLongitude(3.35335);
                address.setState("Lagos");
                address.setCountry("Nigeria");
                address.setFormattedAddress("Old Address, Lagos, Nigeria");
        user.setAddress(address);

        // Tell userRepository to return the mock user when queried by email
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        // Build the geolocation input DTO sent from the frontend
        GeolocationDTO geolocation = new GeolocationDTO();
        geolocation.setLatitude(6.4666);
        geolocation.setLongitude(3.5566);

        // --- Act ---
        ApiRespondsData<AddressDTO> result = verifyAddressService.verifyUserAddress(geolocation);

        // --- Assert ---
        // Verify the service returns the expected success message
        assertEquals("Address Verified and Updated", result.getMessage());
    }
}