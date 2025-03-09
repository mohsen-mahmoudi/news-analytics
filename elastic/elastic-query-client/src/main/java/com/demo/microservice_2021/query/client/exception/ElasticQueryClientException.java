package com.demo.microservice_2021.query.client.exception;

public class ElasticQueryClientException extends RuntimeException {

    public ElasticQueryClientException(String message) {
        super(message);
    }

    public ElasticQueryClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
