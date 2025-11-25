package com.leadforge.gateway.filter;

import com.leadforge.gateway.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

  private final JwtService jwtService;

  private static final String AUTH_PREFIX = "Bearer ";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getPath().value();

    // Skip authentication for auth endpoints
    if (path.startsWith("/api/auth")) {
      return chain.filter(exchange);
    }

    String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith(AUTH_PREFIX)) {
      return unauthorized(exchange, "Missing or invalid Authorization header");
    }

    String token = authHeader.substring(AUTH_PREFIX.length());

    if (!jwtService.isTokenValid(token)) {
      return unauthorized(exchange, "Invalid or expired token");
    }

    String userId = jwtService.extractUserId(token);
    String companyId = jwtService.extractCompanyId(token);
    String role = jwtService.extractRole(token);

    var mutatedRequest = exchange.getRequest()
      .mutate()
      .header("X-User-Id", userId != null ? userId : "")
      .header("X-Company-Id", companyId != null ? companyId : "")
      .header("X-User-Role", role != null ? role : "")
      .build();

    var mutatedExchange = exchange.mutate()
      .request(mutatedRequest)
      .build();

    return chain.filter(mutatedExchange);
  }

  private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
    var response = exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    String body = String.format("{\"error\":\"unauthorized\",\"message\":\"%s\"}", message);
    var buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));

    return response.writeWith(Mono.just(buffer));
  }

  @Override
  public int getOrder() {
    // Run early in the filter chain
    return -1;
  }
}
