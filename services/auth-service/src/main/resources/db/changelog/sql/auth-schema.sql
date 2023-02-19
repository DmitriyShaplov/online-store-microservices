--liquibase formatted sql

--changeset shaplovdv:auth-schema logicalFilePath:/
create schema auth;

create table auth.users
(
    id         bigserial primary key,
    profile_id bigint unique             null,
    login      varchar(256) unique       not null,
    password   varchar(256)              not null,
    reg_date   timestamptz default now() not null
);