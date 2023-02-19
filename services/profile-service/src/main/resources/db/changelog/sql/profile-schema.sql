--liquibase formatted sql

--changeset shaplovdv:profiles-schema logicalFilePath:/
create schema profiles;

create table if not exists profiles.profiles
(
    id         bigserial primary key,
    username   varchar(256),
    first_name text,
    last_name  text,
    email      text,
    phone      text
);

create unique index profiles_username_uidx on profiles.profiles (username);