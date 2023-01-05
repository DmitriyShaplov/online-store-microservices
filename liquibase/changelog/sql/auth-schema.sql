--liquibase formatted sql

--changeset shaplovdv:auth-schema logicalFilePath:/
create schema profiles;
create schema auth;

alter table public.users
    rename to profiles;
alter table public.profiles
    set schema profiles;

create unique index profiles_username_uidx on profiles.profiles(username);

create table auth.users
(
    id         bigserial primary key,
    profile_id bigint unique             null,
    login      varchar(256) unique       not null,
    password   varchar(256)              not null,
    reg_date   timestamptz default now() not null
);