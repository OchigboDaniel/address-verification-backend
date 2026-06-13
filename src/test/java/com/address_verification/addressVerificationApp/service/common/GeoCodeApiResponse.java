package com.address_verification.addressVerificationApp.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeoCodeApiResponse {

    public static Map<String, Object> buildMockGeoCodeApiResponse(){

        // Build a mock Google Geocoding API response
        // Mirrors the nested Map structure returned by RestTemplate
        Map<String, Object> location = new HashMap<>();
        location.put("latitude", 6.4666);
        location.put("longitude", 3.5566);

        Map<String, Object> addressInfo = new HashMap<>();
        addressInfo.put("location", location);
        addressInfo.put("formattedAddress", "Lekki, Lagos, Nigeria");

        // Service reads from index 1, so index 0 is a placeholder
        List<Map<String, Object>> results = new ArrayList<>();
        results.add(new HashMap<>());   // index 0 — unused placeholder
        results.add(addressInfo);        // index 1 — address data used by service

        Map<String, Object> body = new HashMap<>();
        body.put("results", results);

        return body;

    }
}
