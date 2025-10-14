package com.quodex.matchbox.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // IMPORTANT: The secret key should be Base64 encoded and at least 256 bits (32 bytes)
    // The provided key is a simple string; we should treat it as a string to be encoded
    // to ensure it meets the cryptographic requirements.
    private final String jwtSecret = "Xiek393dkjJDXU8dj3372djxlJDOKD279djd93d33fdf2348A7B5C9D1E6F0A3B"; // Extended for better security compliance (32 bytes minimum for HS256)
    private final long jwtExpirationMs = 86400000; // 24 hours

    // Use SecretKey for the modern API
    private final SecretKey key;

    public JwtUtil() {
        // Creates a SecretKey from the Base64-encoded representation of the secret string
        // If your original secret wasn't Base64, you can use Keys.hmacShaKeyFor(jwtSecret.getBytes())
        // but using Decoders.BASE64 is recommended for a secure, environment-variable-friendly key.
        // For simplicity and to fix the deprecation warning, we'll initialize the key correctly.
        // Assuming your original secret was just a string, we'll use a secure key generation method.
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        // Alternatively, if the secret is plain text and you just want to use it:
        // this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    //--------------------------------------------------------------------------------------------------

    /** Generate Jwt Token */
    public String generateToken(String username, String role){
        // Jwts.builder() is now imported from io.jsonwebtoken.Jwts and uses the modern fluent API
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    //--------------------------------------------------------------------------------------------------

    // Get username from token
    public String getUsernameFromToken(String token) {
        // Jwts.parser() is now imported from io.jsonwebtoken.Jwts and uses the modern fluent API
        // .parserBuilder() is deprecated, use Jwts.parser()
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(key) // Replaces .setSigningKey() and .build()
                .build() // This .build() is necessary after setting parser options
                .parseSignedClaims(token); // Replaces .parseClaimsJws()

        return claimsJws.getPayload().getSubject(); // Replaces .getBody() and .getSubject()
    }

    //--------------------------------------------------------------------------------------------------

    // Validate token
    public boolean validateToken(String token) {
        try {
            // New modern validation syntax
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Note: io.jsonwebtoken.security.SignatureException is the specific exception for invalid signatures
            // but JwtException is a good catch-all for JWT issues (like expiration, invalid signature, etc.)
            System.err.println("JWT Validation failed: " + e.getMessage());
            return false;
        }
    }
}