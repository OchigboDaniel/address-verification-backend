package com.address_verification.addressVerificationApp.userAddress.model.mapper;

import com.address_verification.addressVerificationApp.userAddress.mapper.UserMapper;
import com.address_verification.addressVerificationApp.model.User;
import com.address_verification.addressVerificationApp.dto.request.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {



    private User user;
    private CreateUserRequest userDTO;

    @BeforeEach
    void setUp() {


        userDTO = new CreateUserRequest(
                "John Deo",
                "johndeo@gmail.com",
                51.5074,
                -0.1278
        );
    }


    //Test the Entity to DTO mapper
    @Test
    void shouldMapEntityToDTO(){
        CreateUserRequest userDTO = UserMapper.convertToDTO(user);

        assertNotNull(userDTO);
        assertEquals(user.getFullName(), userDTO.getFullName());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getLatitude(), userDTO.getLatitude());
        assertEquals(user.getLongitude(), userDTO.getLongitude());
    }

    @Test
    void shouldMapDTOToEntity(){
        User user = UserMapper.convertToEntity(userDTO);


        assertNotNull(user);
        assertEquals(userDTO.getFullName(), user.getFullName());
        assertEquals(userDTO.getEmail(), user.getEmail());
        assertEquals(userDTO.getLatitude(), user.getLatitude());
        assertEquals(userDTO.getLongitude(), user.getLongitude());
    }
}