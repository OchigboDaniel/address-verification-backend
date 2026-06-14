package com.address_verification.addressVerificationApp.service;

import com.address_verification.addressVerificationApp.ApiRespondsData;
import org.springframework.web.multipart.MultipartFile;

public interface IValidateUtility {
    ApiRespondsData<?> verifyUtility(MultipartFile bill);
}
