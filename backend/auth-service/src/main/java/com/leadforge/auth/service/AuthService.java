package com.leadforge.auth.service;

import com.leadforge.auth.dto.AuthResponse;
import com.leadforge.auth.dto.LoginRequest;
import com.leadforge.auth.dto.RegisterRequest;
import com.leadforge.auth.entity.User;
import com.leadforge.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthResponse register(RegisterRequest req) {
    if (userRepository.existsByEmail(req.getEmail())) {
      throw new RuntimeException("Email ya está registrado");
    }

    User user = User.builder()
      .email(req.getEmail())
      .password(passwordEncoder.encode(req.getPassword()))
      .fullName(req.getFullName())
      .companyId(req.getCompanyId())
      .role("ADMIN") // o USER, según esquema
      .build();

    userRepository.save(user);

    String token = jwtService.generateToken(user.getId(), user.getCompanyId(), user.getRole());
    // Refresh token real lo podrías implementar después (tabla aparte)
    return new AuthResponse(token, "dummy-refresh");
  }

  public AuthResponse login(LoginRequest req) {
    User user = userRepository.findByEmail(req.getEmail())
      .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
      throw new RuntimeException("Credenciales inválidas");
    }

    String token = jwtService.generateToken(user.getId(), user.getCompanyId(), user.getRole());
    return new AuthResponse(token, "dummy-refresh");
  }
}
