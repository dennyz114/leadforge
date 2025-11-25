package com.leadforge.gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String jwtSecret;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public boolean isTokenValid(String token) {
    try {
      parseClaims(token);
      return true;
    } catch (JwtException ex) {
      return false;
    }
  }

  public String extractUserId(String token) {
    return parseClaims(token).getSubject();
  }

  public String extractCompanyId(String token) {
    Object companyId = parseClaims(token).get("companyId");
    return companyId != null ? companyId.toString() : null;
  }

  public String extractRole(String token) {
    Object role = parseClaims(token).get("role");
    return role != null ? role.toString() : null;
  }

  private Claims parseClaims(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(getSigningKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }
}
