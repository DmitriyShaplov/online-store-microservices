--liquibase formatted sql

--changeset shaplovdv:init-notification-schema logicalFilePath:/
create schema ${schema};

create table ${schema}.notifications
(
    id             bigserial primary key,
    email          varchar(256) not null,
    message        text         not null,
    user_id        bigint       not null,
    order_id       uuid,
    date           timestamptz default now(),
    processed_date timestamptz  null
);