package com.demo.microservice_2021.elastic.query.service.config.security;

public enum PermissionType {

    READ("READ"), WRITE("WRITE"), ADMIN("ADMIN");

    private String type;

    PermissionType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;

    }
}
