package com.example.taskmanagementsystem.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    // Secret key for signing the JWT
    @Value("${jwt.secret.key}")
    private String secretKey;

    private SecretKey key;

    // JWT expiration time in milliseconds
    @Value("${jwt.expiration.ms}")
    private long jwtExpirationInMs;

    @PostConstruct
    public void setup() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes()); // Create a secure key from the secret
    }

    /**
     * Generate a JWT token.
     *
     * @param claims   the claims to be included in the token (e.g., username, roles)
     * @param subject  the subject of the token (e.g., the username or user ID)
     * @return the generated JWT
     */
    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // Add the subject (e.g., username)
                .setIssuedAt(new Date()) // Set the issue time
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs)) // Set expiration time
                .signWith(key, SignatureAlgorithm.HS256) // Sign the token using the secret key
                .compact();
    }

    /**
     * Validate the token and check if it has expired.
     *
     * @param token   the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // Parse the token; throws exception if invalid
            return true;
        } catch (Exception e) {
            // Log the error for debugging purposes (use a logger in real applications)
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extract claims from the token.
     *
     * @param token   the JWT token
     * @return the claims present in the token
     */
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody(); // Extract the claims from the token
    }

    /**
     * Extract a specific claim from the token.
     *
     * @param token        the JWT token
     * @param claimResolver a function to extract the desired claim
     * @param <T>          the type of the claim
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimResolver) {
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    /**
     * Extract the subject (e.g., username) from the token.
     *
     * @param token   the JWT token
     * @return the subject present in the token
     */
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

}

