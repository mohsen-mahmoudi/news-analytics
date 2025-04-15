package com.demo.microservice_2021.elastic.query.service.transformers;

public enum QueryType {
    KAFKA_STATE_STORE("KAFKA_STATE_STORE"), ANALYTICS_DATABASE("ANALYTICS_DATABASE");

    private String type;

    QueryType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
