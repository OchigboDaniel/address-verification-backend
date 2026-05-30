package com.address_verification.addressVerificationApp.userAddress.mapper;

import com.address_verification.addressVerificationApp.userAddress.model.User;
import com.address_verification.addressVerificationApp.dto.request.CreateUserRequest;

public class UserMapper {
    // Converts User Entity to userDTO
    public static CreateUserRequest convertToDTO(User user){
        CreateUserRequest userDTO = new CreateUserRequest();
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }

    // Convert UserDTO to Entity
    public static User convertToEntity(CreateUserRequest userDTO){
        User userEntity = new User();
        userEntity.setFullName(userDTO.getFullName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPassword(userDTO.getPassword());
        return userEntity;

    }
}
