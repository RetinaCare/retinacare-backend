package org.retina.care.backend.core.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration}")
    private Long accessTokenExpirationMs;

    // -------------------------- METHODS -------------------------------------

    public String extractUsername(String token) {
       return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Token is validated when the user details from our request matches
    // the one in the token and that the token has not expired yet.
    public Boolean validateToken(String token, UserDetails userDetails) {
        // Technically, what we're working with is the email subject.
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public AccessToken generateAccessToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        String token = createToken(claims, email, accessTokenExpirationMs);
        return new AccessToken(token, Instant.now().plusMillis(accessTokenExpirationMs));
    }

    // -------------- UTILITY / PRIVATE METHODS -----------------------------

    // Utility method - solely for extracting claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Utility method - extracts all claims using signing key.
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Reads the jwt.secret key from the application.properties.
    // Then returns the HMAC SHA key of its bytes.
    private SecretKey getSignKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Jwt Builder - claims are passed - along with expiration and email subject.
    private String createToken(Map<String, Object> claims, String email, Long expiration) {
        return Jwts.builder()
                .claims().add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .and().signWith(getSignKey())
                .compact();
    }
}
