package com.address_verification.addressVerificationApp.userAddress;

import com.address_verification.addressVerificationApp.userAddress.mapper.UserMapper;
import com.address_verification.addressVerificationApp.userAddress.model.User;
import com.address_verification.addressVerificationApp.userAddress.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{


    @Autowired
    IUserRepository userRepository;

    @Override
    public void verifyUserAddress(UserDTO userDTO) {

        User user = UserMapper.convertToEntity(userDTO);

        userRepository.save(user);
    }
}
