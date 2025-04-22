CREATE SCHEMA IF NOT EXISTS analytics AUTHORIZATION postgres;
CREATE TABLE analytics.news_analytics
(
    id          uuid                                           NOT NULL,
    word        character varying COLLATE pg_catalog."default" NOT NULL,
    word_count  bigint                                         NOT NULL,
    record_date time with time zone,
    CONSTRAINT news_analytics_pkey PRIMARY KEY (id)
) TABLESPACE pg_default;

ALTER TABLE analytics.news_analytics OWNER to postgres;

-- Index: INDX_WORD_BY_DATE
-- DROP INDEX analytics."INDX_WORD_BY_DATE";

CREATE INDEX "INDX_WORD_BY_DATE"
    ON analytics.news_analytics USING btree
    (word COLLATE pg_catalog."default" ASC NULLS LAST, record_date DESC NULLS LAST)
    TABLESPACE pg_default;