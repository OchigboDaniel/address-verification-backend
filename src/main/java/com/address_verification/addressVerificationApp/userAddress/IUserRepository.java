package com.address_verification.addressVerificationApp.userAddress;

import com.address_verification.addressVerificationApp.userAddress.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

}
