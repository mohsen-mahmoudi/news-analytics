package com.demo.microservice_2021.elastic.query.service.config.security;

import com.demo.microservice_2021.elastic.query.service.common.model.ElasticQueryServiceRequestModel;
import com.demo.microservice_2021.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Component
public class UserServicePermissionEvaluator implements PermissionEvaluator {

    private final String SUPER_USER_ROLE = "APP_SUPER_USER_ROLE";
    private final HttpServletRequest httpServletRequest;

    public UserServicePermissionEvaluator(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (isSuperUser()) return true;
        if (targetDomainObject instanceof ElasticQueryServiceRequestModel) {
            return preAuthorize(authentication, ((ElasticQueryServiceRequestModel) targetDomainObject).getId(), permission);
        } else if (targetDomainObject instanceof ResponseEntity || targetDomainObject == null) {
            // return from controller
            if (targetDomainObject == null) {
                return true;
            }
            List<ElasticQueryServiceResponseModel> responseBody =
                    ((ResponseEntity<List<ElasticQueryServiceResponseModel>>) targetDomainObject).getBody();
            Objects.requireNonNull(responseBody);
            return postAuthorize(authentication, responseBody, permission);
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (isSuperUser()) return true;
        if (targetId == null) {
            return false;
        }
        return preAuthorize(authentication, String.valueOf(targetId), permission);
    }

    private boolean preAuthorize(Authentication authentication, String id, Object permission) {
        NewsQueryUser newsQueryUser = (NewsQueryUser) authentication.getPrincipal();
        PermissionType permissionType = newsQueryUser.getPermissions().get(id);
        return hasPermission(String.valueOf(permission), permissionType);
    }

    private boolean postAuthorize(Authentication authentication, List<ElasticQueryServiceResponseModel> responseBody, Object permission) {
        NewsQueryUser newsQueryUser = (NewsQueryUser) authentication.getPrincipal();
        for (ElasticQueryServiceResponseModel model : responseBody) {
            PermissionType permissionType = newsQueryUser.getPermissions().get(model.getId().toString());
            if (!hasPermission(String.valueOf(permission), permissionType)) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasPermission(String permission, PermissionType permissionType) {
        return permissionType != null && permission.equals(permissionType.getType());
    }

    private boolean isSuperUser() {
        return httpServletRequest.isUserInRole(SUPER_USER_ROLE);
    }
}
