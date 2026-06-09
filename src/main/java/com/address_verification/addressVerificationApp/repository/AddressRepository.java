package com.address_verification.addressVerificationApp.repository;

import com.address_verification.addressVerificationApp.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
