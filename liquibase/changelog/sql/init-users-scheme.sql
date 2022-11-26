--liquibase formatted sql

--changeset eganovsa:MPC-172-init-assets logicalFilePath:/
create table if not exists users (
    id bigserial primary key ,
    username varchar(256),
    first_name text,
    last_name text,
    email text,
    phone text
);