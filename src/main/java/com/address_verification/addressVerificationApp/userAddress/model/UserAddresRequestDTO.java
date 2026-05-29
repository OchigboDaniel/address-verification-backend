package com.address_verification.addressVerificationApp.userAddress.model;

import com.address_verification.addressVerificationApp.userAddress.dto.AddressRequestDTO;
import com.address_verification.addressVerificationApp.userAddress.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddresRequestDTO {
    private UserDTO user;
    private AddressRequestDTO address;

}
