package com.demo.microservice_2021.repository;

import com.demo.microservice_2021.entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

public class AnalyticsRepositoryImpl<T extends BaseEntity<PK>, PK> implements
        AnalyticsCustomRepository<T, PK> {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyticsRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public <S extends T> PK persist(S entity) {
        em.persist(entity);
        return entity.getId();
    }

    @Override
    public <S extends T> void persist(Collection<S> entities) {
        for (S entity : entities) {
            em.persist(entity);
        }
        em.flush();
        em.clear();
    }

    @Override
    public <S extends T> S merge(S entity) {
        return em.merge(entity);
    }

    @Override
    public <S extends T> void batchMerge(Collection<S> entities) {
        for (S entity : entities) {
            em.merge(entity);
        }
        em.flush();
        em.clear();
    }

    @Override
    public void clear() {
        em.clear();
    }
}
