package com.leadforge.auth.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private long jwtExpirationMs;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public String generateToken(String userId, String companyId, String role) {
    return Jwts.builder()
      .setSubject(userId)
      .claim("companyId", companyId)
      .claim("role", role)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
      .signWith(getSigningKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token);
      return true;
    } catch (JwtException ex) {
      return false;
    }
  }

  public String extractUserId(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(getSigningKey())
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }
}
