package com.address_verification.addressVerificationApp.service;

import com.address_verification.addressVerificationApp.ApiRespondsData;
import com.address_verification.addressVerificationApp.controller.AuthenticationController;
import com.address_verification.addressVerificationApp.dto.AddressDTO;
import com.address_verification.addressVerificationApp.dto.GeolocationDTO;
import com.address_verification.addressVerificationApp.exception.EmailException;
import com.address_verification.addressVerificationApp.userAddress.AddressRepository;
import com.address_verification.addressVerificationApp.userAddress.UserRepository;
import com.address_verification.addressVerificationApp.userAddress.mapper.AddressMapper;
import com.address_verification.addressVerificationApp.userAddress.model.Address;
import com.address_verification.addressVerificationApp.userAddress.model.User;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Service
public class VerifyAddressService implements IVerifyAddressService {


    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    private final RestTemplate restTemplate;
    HttpHeaders headers = new HttpHeaders();
    // Get base url from .env
    @Value("${GEO_CODE_URL_GOOGLE}")
    private String baseUrl;

    // Get Api key from .env
    @Value("${GEO_CODE_API_KEY_GOOGLE}")
    private String api_key;

    public VerifyAddressService(AddressRepository addressRepository, UserRepository userRepository, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public ApiRespondsData<AddressDTO> verifyUserAddress(GeolocationDTO geolocation) {

        String longitudeString = String.valueOf(geolocation.getLongitude());
        String latitudeString = String.valueOf(geolocation.getLatitude());

        //Set the headers
        headers.set("X-Goog-Api-Key", api_key);

        headers.set("X-Goog-FieldMask", "results.formattedAddress," + "results.location," + "results.addressComponents");

        //configure the GEO code required string
        String requestString = baseUrl + "?location.latitude=" + latitudeString + "&location.longitude=" + longitudeString;

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> result = restTemplate.exchange(requestString, HttpMethod.GET, entity, Map.class);

        Map<String, Object> body = result.getBody();

        //convert into a list of objects
        List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");

        Map<String, Object> addressInfo = results.get(1);

        // get the location
        Map<String, Object> location = (Map<String, Object>) addressInfo.get("location");
        //Get Latitude
        Double latitude = ((Number) location.get("latitude")).doubleValue();
        //Get longitude
        Double longitude = ((Number) location.get("longitude")).doubleValue();

        //Get formattedAddress
        String formattedAddress = (String) addressInfo.get("formattedAddress");


        //Get state and country
        String[] stateCountry = formattedAddress.split(",");
        // Trim spaces
        String state = stateCountry[stateCountry.length - 2].trim();
        String country = stateCountry[stateCountry.length - 1].trim();

        AddressDTO addressDTO = new AddressDTO(latitude,longitude,state,country,formattedAddress);

        //Convert to Entity
        Address addressEntity = AddressMapper.convertToAddressEntity(addressDTO);

        // Get the authenticated user details
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EmailException("Emial not Found"));


        //set User Address details
        user.setAddress(addressEntity);
        addressEntity.setUser(user);

        //Save to DB
        addressRepository.save(addressEntity);

        return new ApiRespondsData<>("Address Verified", addressDTO);
    }


}
