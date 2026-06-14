package com.address_verification.addressVerificationApp.service;

import com.address_verification.addressVerificationApp.ApiRespondsData;
import com.address_verification.addressVerificationApp.VerificationStatus;
import com.address_verification.addressVerificationApp.exception.EmailException;
import com.address_verification.addressVerificationApp.model.Address;
import com.address_verification.addressVerificationApp.model.User;
import com.address_verification.addressVerificationApp.model.Verification;
import com.address_verification.addressVerificationApp.repository.UserRepository;
import com.address_verification.addressVerificationApp.repository.VerificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidateUtility implements IValidateUtility {

    private final UserRepository userRepository;
    private final VerificationRepository verificationRepository;
    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Value("${GEMINI_API_KEY}")
    private String geminiApiKey;

    @Value("${GEMINI_API_URL}")
    private String geminiApiUrl;

    public ValidateUtility(UserRepository userRepository,
                           VerificationRepository verificationRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.verificationRepository = verificationRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public ApiRespondsData<?> verifyUtility(MultipartFile bill) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EmailException("Email not Found"));

        //Get the users Address
        Address address = user.getAddress();
        //if the address is null
        if (address == null) {
            return new ApiRespondsData<>("Please verify your GPS address first", null);
        }

        //Get the users Address
        String gpsAddress = user.getAddress().getFormattedAddress();


        String contentType = bill.getContentType();
        String base64File;

        // Convert file to base64
        try {
            byte[] fileBytes = bill.getBytes();
            base64File = Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded file", e);
        }


        String geminiResponse = callGeminiApi(base64File, contentType, gpsAddress);


        try {
            Map<String, Object> geminiData = objectMapper.readValue(geminiResponse, Map.class);

            String billDateStr = (String) geminiData.get("billDate");
            String billAddress = (String) geminiData.get("billAddress");
            String statusStr = (String) geminiData.get("status");
            String comment = (String) geminiData.get("comment");

            LocalDate billDate = LocalDate.parse(billDateStr);
            VerificationStatus status = VerificationStatus.valueOf(statusStr);

            Verification existing = user.getVerification();

            if (existing != null) {
                // Update existing record
                existing.setBillDate(billDate);
                existing.setBillAddress(billAddress);
                existing.setStatus(status);
                existing.setAiComment(comment);
                existing.setBillImagePath(bill.getOriginalFilename());
                existing.setVerifiedAt(LocalDateTime.now());
                verificationRepository.save(existing);
            } else {
                // Create new record
                Verification verification = new Verification();
                verification.setBillDate(billDate);
                verification.setBillAddress(billAddress);
                verification.setStatus(status);
                verification.setAiComment(comment);
                verification.setBillImagePath(bill.getOriginalFilename());
                verification.setVerifiedAt(LocalDateTime.now());
                verification.setUser(user);
                verificationRepository.save(verification);
            }

            return new ApiRespondsData<>("Bill verification complete", Map.of(
                    "status", status,
                    "comment", comment,
                    "billAddress", billAddress,
                    "billDate", billDateStr
            ));

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }




    private String callGeminiApi(String base64File, String contentType, String gpsAddress) {

        //get the current time
        String today = LocalDate.now().toString();

        String prompt = "You are a utility bill verification assistant. Analyze the uploaded utility bill and respond ONLY with a JSON object — no markdown, no explanation, just raw JSON.\n\n" +
                "The user's GPS-verified address is: " + gpsAddress + "\n" +
                "Today's date is: " + today + "\n\n" +
                "Statuses:\n" +
                "- VERIFIED: bill address matches GPS address and bill is within 3 months\n" +
                "- EXPIRED_BILL: bill is older than 3 months\n" +
                "- PARTIAL_MATCH: street differs but city/country matches\n" +
                "- INSUFFICIENT_DETAIL: address on bill is too vague to verify\n" +
                "- ADDRESS_MISMATCH: bill and GPS address are completely different\n" +
                "- NO_BILL_UPLOADED: no bill was provided\n\n" +
                "Respond with this exact JSON structure:\n" +
                "{\"billDate\": \"YYYY-MM-DD\", \"billAddress\": \"full address\", \"status\": \"STATUS\", \"comment\": \"explanation\"}";

        // Build request body
        Map<String, Object> imagePart = new HashMap<>();
        imagePart.put("inline_data", Map.of(
                "mime_type", contentType,
                "data", base64File
        ));

        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(imagePart, textPart));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));

        // Call Gemini API
        String url = geminiApiUrl + "?key=" + geminiApiKey;



        ResponseEntity<Map> response;

        try{
            response = restTemplate.postForEntity(url, requestBody, Map.class);
        } catch (Exception e) {
            throw e;
        }





        // Extract text response
        Map<String, Object> body = response.getBody();
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) body.get("candidates");
        Map<String, Object> candidate = candidates.get(0);
        Map<String, Object> responseContent = (Map<String, Object>) candidate.get("content");
        List<Map<String, Object>> parts = (List<Map<String, Object>>) responseContent.get("parts");

        return (String) parts.get(0).get("text");
    }
}
