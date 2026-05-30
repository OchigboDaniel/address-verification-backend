package com.address_verification.addressVerificationApp;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtility {



    private final SecretKey key;
    private final long JWT_EXPIRE_TIME;

    public JWTUtility(
            @Value("${JWT_SECRETE_KEY}") String jwtSecretKey,
            @Value("${JWT_EXPIRE_TIME}") long jwtExpireTime) {



        this.key = Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
        this.JWT_EXPIRE_TIME = jwtExpireTime;
    }

    public String generateToken(String email){

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    public  Claims extractClaims(String token){
        return  Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Extrate User Email
    public String extractUserEmail(String token){
        return extractClaims(token).getSubject();
    }

    //Extract User Role
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public Boolean validateToken(String email, UserDetails userDetails, String token){
               return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public Boolean isTokenExpired(String token){
        return extractClaims(token).getExpiration().before( new Date());
    }
}
