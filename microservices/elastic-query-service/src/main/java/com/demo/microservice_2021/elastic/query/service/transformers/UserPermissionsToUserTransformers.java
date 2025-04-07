package com.demo.microservice_2021.elastic.query.service.transformers;

import com.demo.microservice_2021.elastic.query.service.config.security.NewsQueryUser;
import com.demo.microservice_2021.elastic.query.service.config.security.PermissionType;
import com.demo.microservice_2021.elastic.query.service.data.entity.UserPermission;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserPermissionsToUserTransformers {

    public NewsQueryUser getUserDetails(List<UserPermission> userPermissions) {
        return NewsQueryUser.builder()
                .username(userPermissions.get(0).getUsername())
                .permissions(userPermissions.stream().collect(Collectors.toMap(
                        UserPermission::getDocumentId,
                        permission -> PermissionType.valueOf(permission.getPermissionType()))))
                .build();
    }
}
