package com.books.bonnets.librarian.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JWTServiceImpl implements JWTService {

    // Secret used to sign and verify JWTs; should be kept safe and consistent across restarts.
    @Value("${app.jwt.secret}")
    private String secret;

    // Lifetime of access tokens in milliseconds.
    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    // Lifetime of refresh tokens in milliseconds.
    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpiration;

    @Override
    public String generateToken(UserDetails userDetails) {
        return buildToken(userDetails, jwtExpiration);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, refreshExpiration);
    }

    @Override
    public String extractUsername(String token) {
        // The username is stored as the JWT subject.
        return extractClaim(token, Claims::getSubject);
    }

    // Checks if the token is valid (not expired) and belongs to the user.
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    @Override
    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private String buildToken(UserDetails userDetails, long expirationMillis) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                // Store the username as the token subject for later lookup.
                .subject(userDetails.getUsername())
                // Include roles so the token can carry authorization info if needed.
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(authority -> authority.getAuthority())
                        .toList())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Verify the signature of the token.
                .build()
                .parseSignedClaims(token)// Parse the token to get its claims.
                .getPayload();
    }

    private SecretKey getSigningKey() {
        // Derive the HMAC signing key from the configured secret.
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}