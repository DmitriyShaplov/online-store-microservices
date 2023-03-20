--liquibase formatted sql

--changeset shaplovdv:ddl-processed-messages logicalFilePath:/
create table ${schema}.processed_messages
(
    message_id uuid primary key
);