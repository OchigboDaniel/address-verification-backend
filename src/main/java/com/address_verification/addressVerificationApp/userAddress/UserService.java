package com.address_verification.addressVerificationApp.userAddress;

import com.address_verification.addressVerificationApp.userAddress.dto.AddressRequestDTO;
import com.address_verification.addressVerificationApp.userAddress.mapper.AddressMapper;
import com.address_verification.addressVerificationApp.userAddress.mapper.UserMapper;
import com.address_verification.addressVerificationApp.userAddress.model.Address;
import com.address_verification.addressVerificationApp.userAddress.model.User;
import com.address_verification.addressVerificationApp.userAddress.dto.UserDTO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Service
public class UserService implements IUserService{

    @Autowired
    private PasswordEncoder passwordEncoder;


    private final UserRepository userRepository;
    private final IAddressRepository addressRepository;
    private final RestTemplate restTemplate;


    public UserService(UserRepository userRepository, IAddressRepository addressRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.restTemplate = restTemplate;
    }

    // Get base url from .env
    @Value("${GEO_CODE_URL}")
    private String baseUrl;

    // Get Api key from .env
    @Value("${GEO_CODE_API_KEY}")
    private String api_key;


    public ResponseEntity<?> verifyUserAddress(int user_id){
        AddressRequestDTO addressRequestDTO;

        addressRequestDTO = new AddressRequestDTO();

        User userEntity = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User Not found"));


        String longitudeStr = "";//String.valueOf(userEntity.getLongitude());
        String latitudeStr = "";//String.valueOf(userEntity.getLatitude());

        //configure the GEO code required string
        String requestString = baseUrl + "?q=" + longitudeStr + "+" + latitudeStr + "&key=" + api_key;

        Map<String, Object>  response;

        response = restTemplate.getForObject(
                    requestString,
                    Map.class
            );



        List<Map<String, Object>> results =
                (List<Map<String, Object>>) response.get("results");

        Map<String, Object> firstResult = results.get(0);

        Map<String, Object> components =
                (Map<String, Object>) firstResult.get("components");

        addressRequestDTO.setCity((String) components.get("village"));
        addressRequestDTO.setState((String) components.get("state"));
        addressRequestDTO.setCountry((String) components.get("country"));
        addressRequestDTO.setFormattedAddress((String) firstResult.get("formatted"));

        Address addressEntity = AddressMapper.convertToAddressEntity(addressRequestDTO);
        addressEntity.setUser(userEntity);
        addressRepository.save(addressEntity);
        return ResponseEntity.ok(addressRequestDTO);
    }



}
