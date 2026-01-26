package com.university.cosmocats.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TestConfiguration
public class TestSecurityConfig {

  @Bean
  @Primary
  public JwtDecoder testJwtDecoder() {
    return token -> {
      Map<String, Object> headers = new HashMap<>();
      headers.put("alg", "RS256");
      headers.put("typ", "JWT");

      Map<String, Object> claims = new HashMap<>();
      claims.put("sub", "test-user");
      claims.put("authorities", List.of("ADMIN"));
      claims.put("scope", "read write");
      claims.put("iss", "test-issuer");
      claims.put("aud", List.of("test-audience"));

      Instant now = Instant.now();
      claims.put("iat", now);
      claims.put("exp", now.plusSeconds(3600));

      Jwt jwt = new Jwt(token, now, now.plusSeconds(3600), headers, claims);

      return jwt;
    };
  }

  @Bean
  @Primary
  public JwtAuthenticationConverter testJwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(new TestAuthorityConverter());
    return converter;
  }

  @Bean
  @Primary
  public TestAuthorityConverter testAuthorityConverter() {
    return new TestAuthorityConverter();
  }
}