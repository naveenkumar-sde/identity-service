package com.snapstore.identity_service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${jwt.secret-key}")
    private String EXPIRE_AT;

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Long.getLong(EXPIRE_AT)))
                .signWith(SignatureAlgorithm.HS384, SECRET_KEY)
                .compact();
    }

    public String extractEmail(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJwt(accessToken)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String accessToken, String email) {
        String extractedEmail = extractEmail(accessToken);
        return extractedEmail.equals(email);
    }
}
