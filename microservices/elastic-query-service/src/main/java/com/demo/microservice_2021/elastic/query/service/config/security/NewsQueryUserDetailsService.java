package com.demo.microservice_2021.elastic.query.service.config.security;

import com.demo.microservice_2021.elastic.query.service.service.QueryUserService;
import com.demo.microservice_2021.elastic.query.service.transformers.UserPermissionsToUserTransformers;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NewsQueryUserDetailsService implements UserDetailsService {

    private final QueryUserService queryUserService;
    private final UserPermissionsToUserTransformers userPermissionsToUserTransformers;

    public NewsQueryUserDetailsService(QueryUserService queryUserService,
                                       UserPermissionsToUserTransformers userPermissionsToUserTransformers) {
        this.queryUserService = queryUserService;
        this.userPermissionsToUserTransformers = userPermissionsToUserTransformers;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return queryUserService.findPermissionsByUsername(username)
                .map(userPermissionsToUserTransformers::getUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("user not founded!"));
    }
}
