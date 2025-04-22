package com.demo.microservice_2021.repository;

import java.util.Collection;

public interface AnalyticsCustomRepository<T, PK> {

    <S extends T> PK persist(S entity);

    <S extends T> void persist(Collection<S> entities);

    <S extends T> S merge(S entity);

    <S extends T> void batchMerge(Collection<S> entities);

    void clear();
}
