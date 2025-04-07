package com.demo.microservice_2021.elastic.query.service.service;

import com.demo.microservice_2021.elastic.query.service.data.entity.UserPermission;
import com.demo.microservice_2021.elastic.query.service.data.repository.UserPermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsQueryServiceImpl implements QueryUserService{

    private static final Logger LOG = LoggerFactory.getLogger(NewsQueryServiceImpl.class);

    private final UserPermissionRepository userPermissionRepository;

    public NewsQueryServiceImpl(UserPermissionRepository userPermissionRepository) {
        this.userPermissionRepository = userPermissionRepository;
    }

    @Override
    public Optional<List<UserPermission>> findPermissionsByUsername(String username) {
        LOG.info("findPermissionsByUsername {} ", username);
        return userPermissionRepository.findPermissionsByUsername(username);
    }
}
