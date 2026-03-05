package com.ricky.chronicle.service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.ricky.chronicle.auth.JwtProperties;
import com.ricky.chronicle.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;

    public String generateToken(UUID userId){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", "ROLE_USER");
        return createToken(claims, userId);
    }

    private String createToken(Map<String,Object> claims, UUID userId){
        Instant now = Instant.now();
        Instant expiry = now.plus(24,ChronoUnit.HOURS);
        return Jwts.builder()
            .claims(claims)
            .subject(userId.toString())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .signWith(getSignKey())
            .compact();
    }

    private Key getSignKey(){
        String secret = jwtProperties.secret();
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getVerificationKey(){
        String secret = jwtProperties.secret();
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public UUID extractUserId(String token){
        String idString = extractClaim(token, Claims::getSubject);
        return UUID.fromString(idString);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
            .verifyWith(getVerificationKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    } 

    public Boolean isTokenExpired(String token){
        Instant now = Instant.now();
        return extractExpiration(token).before(Date.from(now));
    }

    public Boolean validateToken(String token, User user){
        final UUID userId = extractUserId(token);
        return (userId.equals(user.getId()) && !isTokenExpired(token));
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        try {
            Claims claims = extractAllClaims(token); 
            

            String role = claims.get("role", String.class); 

            if (role != null) {                
                return Collections.singletonList(new SimpleGrantedAuthority(role));
            } else {
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
