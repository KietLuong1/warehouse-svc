package com.capstone.warehousesvc.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUtils {

    private static final long EXPIRATION_TIME_IN_MILLISEC = 1000L * 60L * 60L * 24L * 30L * 6L; //expires in 6 months in milleces
    private SecretKey key;

    @Value("${secreteJwtString:defaultSecretKeyForDevelopmentOnlyDontUseInProduction}")
    private String secreteJwtString;

    @PostConstruct
    private void init() {
        // Use a default secret if the provided one is empty
        if (secreteJwtString == null || secreteJwtString.isEmpty() || secreteJwtString.equals("''")) {
            secreteJwtString = "defaultSecretKeyForDevelopmentOnlyDontUseInProduction";
            log.warn("Using default JWT secret key. This should be changed in production!");
        }
        byte[] keyBytes = secreteJwtString.getBytes(StandardCharsets.UTF_8);
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLISEC))
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        try {
            return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
        } catch (Exception e) {
            log.error("Error extracting claims from token: {}", e.getMessage());
            return null;
        }
    }

    public boolean isTokeValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokeExpired(token));
    }

    private boolean isTokeExpired(String token) {
        Date expirationDate = extractClaims(token, Claims::getExpiration);
        return expirationDate != null && expirationDate.before(new Date());
    }
}
