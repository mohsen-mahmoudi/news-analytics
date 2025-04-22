package com.demo.microservice_2021.repository;

import com.demo.microservice_2021.entity.AnalyticsEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnalyticsRepository extends JpaRepository<AnalyticsEntity, UUID>,
        AnalyticsCustomRepository<AnalyticsEntity, UUID> {

    List<AnalyticsEntity> getAnalyticsEntityByWordOrderByRecordDateDesc(String word, Pageable pageable);

}
