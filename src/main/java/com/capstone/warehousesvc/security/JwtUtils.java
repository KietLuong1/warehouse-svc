package com.capstone.warehousesvc.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUtils {

    private Key key;

    /**
     * Initialize the JWT validation key.
     * This key must match the signing key used in the login service.
     * The warehouse service uses this key to validate tokens issued by the login service.
     */
    @PostConstruct
    private void init() {
        byte[] keyBytes = Decoders.BASE64.decode("BF7FD11ACE545745B7BA1AF98B6F156D127BC7BB544BAB6A4FD74E4FC7");
        this.key = Keys.hmacShaKeyFor(keyBytes);
        log.info("JWT validation key initialized for tokens from loginservice");
    }

    /**
     * Extract the username from a JWT token issued by the login service.
     * 
     * @param token JWT token string (without 'Bearer ' prefix)
     * @return the username (subject) contained in the token, or null if token is invalid
     */
    public String getUsernameFromToken(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claimsTFunction.apply(claims);
        } catch (Exception e) {
            log.error("Error extracting claims from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Validates a JWT token from the login service.
     * Checks if the token is not expired and contains the correct username.
     * 
     * @param token JWT token string (without 'Bearer ' prefix)
     * @param userDetails UserDetails object to compare against token claims
     * @return true if token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = extractClaims(token, Claims::getExpiration);
        return expirationDate != null && expirationDate.before(new Date());
    }
}
