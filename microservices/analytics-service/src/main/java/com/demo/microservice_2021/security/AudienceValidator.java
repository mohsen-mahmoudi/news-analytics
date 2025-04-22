package com.demo.microservice_2021.security;

import com.demo.microservice_2021.configdata.config.KafkaStreamsConfigData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@Qualifier("analytics-service-audience-validator")
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final KafkaStreamsConfigData configData;

    public AudienceValidator(KafkaStreamsConfigData configData) {
        this.configData = configData;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (jwt.getAudience().contains(configData.getCustomAudience())) {
            return OAuth2TokenValidatorResult.success();
        }
        OAuth2Error error = new OAuth2Error("invalid_token",
                "required audience " + configData.getCustomAudience() + " is missing",
                null);
        return OAuth2TokenValidatorResult.failure(error);
    }
}
