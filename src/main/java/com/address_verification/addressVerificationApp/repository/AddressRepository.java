package com.address_verification.addressVerificationApp.repository;

import com.address_verification.addressVerificationApp.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AddressRepository extends JpaRepository<Address, Integer>, JpaSpecificationExecutor<Address> {
}
