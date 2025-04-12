package com.demo.microservice_2021.kafka.streams.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

public class KafkaStreamsUserJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsUserJwtConverter.class);

    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";
    private static final String SCOPE_CLAIM = "scope";
    private static final String USERNAME_CLAIM = "preferred_username";
    private static final String DEFAULT_ROLE_PREFIX = "ROLE_";
    private static final String DEFAULT_SCOPE_PREFIX = "SCOPE_";
    private static final String SCOPE_SEPARATOR = " ";

    private final KafkaStreamsUserDetailsService kafkaStreamsUserDetailsService;

    public KafkaStreamsUserJwtConverter(KafkaStreamsUserDetailsService kafkaStreamsUserDetailsService) {
        this.kafkaStreamsUserDetailsService = kafkaStreamsUserDetailsService;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<SimpleGrantedAuthority> authoritiesFromJwt = getAuthoritiesFromJwt(jwt);
        return Optional
                .ofNullable(kafkaStreamsUserDetailsService.loadUserByUsername(jwt.getClaimAsString(USERNAME_CLAIM)))
                .map(userDetails -> {
                    ((KafkaStreamsUser) userDetails).setAuthorities(authoritiesFromJwt);
                    return new UsernamePasswordAuthenticationToken(userDetails, "N/A", authoritiesFromJwt);
                })
                .orElseThrow(() -> new BadCredentialsException("User not found in the system"));
    }

    private Collection<SimpleGrantedAuthority> getAuthoritiesFromJwt(Jwt jwt) {
        Collection<String> authorities = combinedAuthorities(jwt);
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    private Collection<String> combinedAuthorities(Jwt jwr) {
        List<String> authorities = new ArrayList<>();
        authorities.addAll(getRoles(jwr));
        authorities.addAll(getScopes(jwr));
        return authorities;
    }

    private Collection<String> getRoles(Jwt jwt) {
        Object o = jwt.getClaims().get(REALM_ACCESS_CLAIM);
        if (o instanceof Collection) {
            return ((Collection<String>) o).stream()
                    .map(authority -> DEFAULT_ROLE_PREFIX + authority.toUpperCase())
                    .toList();
        }
        return Collections.emptyList();
    }

    private Collection<String> getScopes(Jwt jwt) {
        Object o = jwt.getClaims().get(SCOPE_CLAIM);
        if (o instanceof String) {
            return Arrays.stream(((String) o).split(SCOPE_SEPARATOR))
                    .map(authority -> DEFAULT_ROLE_PREFIX + authority.toUpperCase())
                    .toList();
        }
        return Collections.emptyList();
    }
}
