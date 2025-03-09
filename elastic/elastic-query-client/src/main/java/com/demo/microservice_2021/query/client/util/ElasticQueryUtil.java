package com.demo.microservice_2021.query.client.util;

import com.demo.microservice_2021.elastic.model.index.IndexModel;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ElasticQueryUtil<T extends IndexModel> {

    public Query getSearchById(String id) {
        return new NativeSearchQueryBuilder()
                .withIds(Collections.singleton(id))
                .build();
    }

    public Query getSearchByField(String field, String value) {
        return new NativeSearchQueryBuilder()
                .withQuery(new BoolQueryBuilder().must(QueryBuilders.matchQuery(field, value)))
                .build();
    }

    public Query getSearchForAll() {
        return new NativeSearchQueryBuilder()
                .withQuery(new BoolQueryBuilder().must(QueryBuilders.matchAllQuery()))
                .build();
    }
}
