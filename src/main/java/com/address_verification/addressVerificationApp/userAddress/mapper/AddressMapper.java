package com.address_verification.addressVerificationApp.userAddress.mapper;

import com.address_verification.addressVerificationApp.userAddress.dto.AddressRequestDTO;
import com.address_verification.addressVerificationApp.userAddress.model.Address;

public class AddressMapper {
    public static AddressRequestDTO convertToDTO(Address address){
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setCity(address.getCity());
        addressRequestDTO.setState(address.getState());
        addressRequestDTO.setCountry(address.getCountry());
        addressRequestDTO.setFormattedAddress(address.getFormattedAddress());

        return  addressRequestDTO;
    }


    public static Address convertToAddressEntity(AddressRequestDTO addressRequestDTO){
        Address address = new Address();
        address.setCity(addressRequestDTO.getCity());
        address.setState(addressRequestDTO.getState());
        address.setCountry(addressRequestDTO.getCountry());
        address.setFormattedAddress(addressRequestDTO.getFormattedAddress());

        return  address;
    }
}
