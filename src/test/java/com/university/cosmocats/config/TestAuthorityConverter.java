package com.university.cosmocats.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestAuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

  @Override
  public Collection<GrantedAuthority> convert(final Jwt jwt) {

    List<GrantedAuthority> authorities = new ArrayList<>();
    Optional<List<String>> authoritiesClaim =
            Optional.ofNullable((List<String>) jwt.getClaims().get("authorities"));

    authoritiesClaim.ifPresent(
            auths -> {
              authorities.addAll(
                      auths.stream()
                              .map(roleName -> "ROLE_" + roleName)
                              .map(SimpleGrantedAuthority::new)
                              .collect(Collectors.toList()));
            });

    String scopeClaim = jwt.getClaimAsString("scope");

    if (scopeClaim != null && !scopeClaim.isEmpty()) {
      String[] scopes = scopeClaim.split(" ");
      for (String scope : scopes) {
        authorities.add(new SimpleGrantedAuthority("SCOPE_" + scope));
      }
    }

    return authorities;
  }
}