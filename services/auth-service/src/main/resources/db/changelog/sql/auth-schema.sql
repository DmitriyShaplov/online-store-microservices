--liquibase formatted sql

--changeset shaplovdv:auth-schema logicalFilePath:/
create schema os_auth;

create table os_auth.users
(
    id         bigserial primary key,
    account_id bigint unique             null,
    login      varchar(256) unique       not null,
    password   varchar(256)              not null,
    username   varchar(256)              not null,
    first_name text                      not null,
    last_name  text                      not null,
    email      text                      not null,
    phone      text                      not null,
    reg_date   timestamptz default now() not null
);