package com.demo.microservice_2021.elastic.query.service.service;

import com.demo.microservice_2021.elastic.query.service.data.entity.UserPermission;

import java.util.List;
import java.util.Optional;

public interface QueryUserService {
    Optional<List<UserPermission>> findPermissionsByUsername(String username);
}
