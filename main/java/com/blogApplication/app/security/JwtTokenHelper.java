package com.blogApplication.app.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtTokenHelper {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    // Store your Base64-encoded secret key (64 bytes for HS512)
    private final String secret = "Thisisthesecretekeyforjwtauthenticati3456789fghyjkm8654678ihjnbvcsert76543qazxcv7654dfgvhboninmye4567890ohgs4567890hdaproject";

    // Decode and create the SecretKey
    private SecretKey getKey() {
    	byte[] keyBytes = Decoders.BASE64.decode(secret); // Make sure 'secret' is Base64 encoded
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Retrieve username from JWT token
    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Retrieve expiration date from JWT token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Extract claims from the token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Retrieve all claims from the JWT token
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                   .verifyWith(getKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }

    // Check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Generate token for a user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities()); // Include user roles as a claim
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // Create JWT token with claims, subject, and expiration
    public String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(subject)
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                   .signWith(getKey(), SignatureAlgorithm.HS512)
                   .compact();
    }





    // Validate the token
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String userName = getUserNameFromToken(token);
            return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false; // If any error occurs, the token is invalid
        }
    }
}
