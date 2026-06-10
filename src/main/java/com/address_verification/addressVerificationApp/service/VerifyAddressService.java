package com.address_verification.addressVerificationApp.service;

import com.address_verification.addressVerificationApp.ApiRespondsData;
import com.address_verification.addressVerificationApp.dto.AddressDTO;
import com.address_verification.addressVerificationApp.dto.GeolocationDTO;
import com.address_verification.addressVerificationApp.dto.response.AddressResponse;
import com.address_verification.addressVerificationApp.exception.EmailException;
import com.address_verification.addressVerificationApp.repository.AddressRepository;
import com.address_verification.addressVerificationApp.repository.UserRepository;
import com.address_verification.addressVerificationApp.userAddress.mapper.AddressMapper;
import com.address_verification.addressVerificationApp.model.Address;
import com.address_verification.addressVerificationApp.model.User;
import com.address_verification.addressVerificationApp.utils.AddressSpecification;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

        ResponseEntity<Map> result = restTemplate.exchange(
                requestString, HttpMethod.GET, entity, Map.class
        );

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



        // Get the users address
        Address existingAddress = user.getAddress();
        //If user already has an address update the address
        if (existingAddress != null) {
            // Update existing address
            existingAddress.setLatitude(addressEntity.getLatitude());
            existingAddress.setLongitude(addressEntity.getLongitude());
            existingAddress.setFormattedAddress(addressEntity.getFormattedAddress());
            existingAddress.setCountry(addressEntity.getCountry());
            existingAddress.setState(addressEntity.getState());

            addressRepository.save(existingAddress);
        } else {
            // Create new address
            user.setAddress(addressEntity);
            addressEntity.setUser(user);

            addressRepository.save(addressEntity);
        }

        return new ApiRespondsData<>("Address Verified", addressDTO);
    }

    @Override
    public ApiRespondsData<Page<AddressResponse> > getAllUserandAddress(Pageable pageable, String country, String state, String email) {

        ArrayList<AddressResponse> allAddressPlusEmail = new ArrayList<>();

        //Get all users from DB with pagination and filter
        Specification<Address> spec = AddressSpecification.withFilters(country, state, email);
        Page<Address> allAddressData = addressRepository.findAll(spec, pageable);

        Page<AddressResponse> responsePage = allAddressData.map(address -> {
            AddressResponse addressResponse = new AddressResponse();
            addressResponse.setId(address.getId());
            addressResponse.setLatitude(address.getLatitude());
            addressResponse.setLongitude(address.getLongitude());
            addressResponse.setState(address.getState());
            addressResponse.setCountry(address.getCountry());
            addressResponse.setFormattedAddress(address.getFormattedAddress());
            addressResponse.setOwnerEmail(address.getUser().getEmail());
        return addressResponse;
    });

        return new ApiRespondsData<>("Retrieved all address", responsePage);
    }

    //Export Service
    @Override
    public ApiRespondsData<List<AddressResponse>> getAllUserandAddressForExport() {

        ArrayList<AddressResponse> allAddressPlusEmail = new ArrayList<>();

        //Get all users from DB
        List<Address> allAddressData = addressRepository.findAll();


        for (Address address : allAddressData){
            AddressResponse addressResponse = new AddressResponse();

            addressResponse.setId(address.getId());
            addressResponse.setLatitude(address.getLatitude());
            addressResponse.setLongitude(address.getLongitude());
            addressResponse.setState(address.getState());
            addressResponse.setCountry(address.getCountry());
            addressResponse.setFormattedAddress(address.getFormattedAddress());
            addressResponse.setOwnerEmail(address.getUser().getEmail());

            allAddressPlusEmail.add(addressResponse);
        }


        return new ApiRespondsData<>("Retrieved all address", allAddressPlusEmail);
    }
}
