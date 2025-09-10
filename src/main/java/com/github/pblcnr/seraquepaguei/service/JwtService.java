package com.github.pblcnr.seraquepaguei.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String generateToken(Long userId) {
        JwtBuilder token = Jwts.builder();
        token.setSubject(userId.toString());
        token.setIssuedAt(new Date());
        token.setExpiration(new Date(System.currentTimeMillis() + expirationTime));
        token.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()));

        return token.compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String subject = claims.getSubject();
        return Long.parseLong(subject);
    }
}
