package com.demo.microservice_2021.elastic.query.service.common.exception;

public class ElasticQueryServiceException extends RuntimeException {

    public ElasticQueryServiceException(String message) {
        super(message);
    }

    public ElasticQueryServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}