--liquibase formatted sql

--changeset shaplovdv:ddl-outbox logicalFilePath:/
create table ${schema}.outbox
(
    id            uuid         primary key,
    aggregatetype varchar(255) not null,
    aggregateid   varchar(255) not null,
    type          varchar(255) not null,
    date          timestamptz default now(),
    payload       jsonb
);