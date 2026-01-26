package com.university.cosmocats.config.security;

import com.university.cosmocats.util.SecurityUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

public class CosmicApiKeyFilter extends OncePerRequestFilter {

    private final BearerTokenResolver resolver =
            new org.springframework.security.oauth2.server.resource.web.HeaderBearerTokenResolver(SecurityUtil.X_API_KEY_HEADER);

    private final BearerTokenAuthenticationEntryPoint entryPoint = new BearerTokenAuthenticationEntryPoint();

    private final AuthenticationFailureHandler failureHandler =
            new AuthenticationEntryPointFailureHandler(entryPoint);

    private final AuthenticationProvider jwtAuthProvider;

    public CosmicApiKeyFilter(JwtDecoder decoder, Converter<Jwt, Collection<GrantedAuthority>> authorityConverter) {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(decoder);

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorityConverter);

        provider.setJwtAuthenticationConverter(converter);

        this.jwtAuthProvider = provider;
    }

    public CosmicApiKeyFilter(JwtDecoder decoder) {
        this(decoder, new AuthorityConverter());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String apiKey;
        try {
            apiKey = resolver.resolve(request);
        } catch (OAuth2AuthenticationException ex) {
            this.logger.trace("Failed to resolve API Key", ex);
            entryPoint.commence(request, response, ex);
            return;
        }

        if (apiKey == null) {
            this.logger.trace("No API Key provided in header: " + SecurityUtil.X_API_KEY_HEADER);
            entryPoint.commence(request, response, null);
            return;
        }

        try {
            BearerTokenAuthenticationToken authRequest = new BearerTokenAuthenticationToken(apiKey);
            Authentication authResult = jwtAuthProvider.authenticate(authRequest);

            if (!authResult.isAuthenticated()) {
                failureHandler.onAuthenticationFailure(request, response,
                        new OAuth2AuthenticationException("Invalid API Key"));
                return;
            }

            SecurityContextHolder.getContext().setAuthentication(authResult);
            filterChain.doFilter(request, response);

        } catch (AuthenticationException failed) {
            this.logger.trace("API Key authentication failed", failed);
            failureHandler.onAuthenticationFailure(request, response, failed);
        }
    }
}