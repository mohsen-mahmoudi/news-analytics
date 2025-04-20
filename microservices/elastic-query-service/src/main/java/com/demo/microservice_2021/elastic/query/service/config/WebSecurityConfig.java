package com.demo.microservice_2021.elastic.query.service.config;

import com.demo.microservice_2021.elastic.query.service.config.security.NewsQueryUserDetailsService;
import com.demo.microservice_2021.elastic.query.service.config.security.NewsQueryUserJwtConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Value("${security.path-to-ignore}")
    private String[] pathToIgnore;

    private final NewsQueryUserDetailsService newsQueryUserDetailsService;
    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    public WebSecurityConfig(NewsQueryUserDetailsService newsQueryUserDetailsService,
                             OAuth2ResourceServerProperties oAuth2ResourceServerProperties) {
        this.newsQueryUserDetailsService = newsQueryUserDetailsService;
        this.oAuth2ResourceServerProperties = oAuth2ResourceServerProperties;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Disable session creation
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/api-docs/**").permitAll()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(newsQueryUserJwtConverter())
                .and()
                .and().build();
    }

    @Bean
    JwtDecoder jwtDecoder(@Qualifier("elastic-query-service-audience-validator") OAuth2TokenValidator<Jwt> audienceValidator) {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(
                oAuth2ResourceServerProperties.getJwt().getIssuerUri());
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(
                oAuth2ResourceServerProperties.getJwt().getIssuerUri());
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<Jwt>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(withAudience);
        return jwtDecoder;
    }

    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> newsQueryUserJwtConverter() {
        return new NewsQueryUserJwtConverter(newsQueryUserDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
