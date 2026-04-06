package com.address_verification.addressVerificationApp.userAddress.model.mapper;

import com.address_verification.addressVerificationApp.userAddress.model.User;
import com.address_verification.addressVerificationApp.userAddress.model.UserDTO;

public class UserMapper {
    // Converts User Entity to userDTO
    public static UserDTO convertToDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setLatitude(user.getLatitude());
        userDTO.setLongitude(user.getLongitude());
        return userDTO;
    }

    // Convert UserDTO to Entity
    public static  User convertToEntity(UserDTO userDTO){
        User userEntity = new User();
        userEntity.setFullName(userDTO.getFullName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setLatitude(userDTO.getLatitude());
        userEntity.setLongitude(userDTO.getLongitude());
        return userEntity;

    }
}
