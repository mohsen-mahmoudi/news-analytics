package com.demo.microservice_2021.elastic.query.service.config.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("elastic-query-service-audience-validator")
public class AudienceValidator /*implements OAuth2Token*/ {

}
