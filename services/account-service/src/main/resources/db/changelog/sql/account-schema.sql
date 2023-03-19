--liquibase formatted sql

--changeset shaplovdv:user-schema logicalFilePath:/
create schema os_accounts;

create table if not exists os_accounts.profiles
(
    id         bigserial primary key,
    username   varchar(256),
    first_name text,
    last_name  text,
    email      text,
    phone      text
);

create unique index profiles_username_uidx on os_accounts.profiles (username);