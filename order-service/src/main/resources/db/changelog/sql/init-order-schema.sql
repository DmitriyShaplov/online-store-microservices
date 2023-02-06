--liquibase formatted sql

--changeset shaplovdv:init-order-schema logicalFilePath:/
create schema app_orders;

create table if not exists app_orders.orders (
    id uuid primary key,
    profile_id bigint not null,
    date timestamptz not null default now(),
    shipping_address text,
    total_amount decimal,
    payment_method varchar(128),
    order_status varchar(64)
);

create index if not exists orders_profile_id_idx on app_orders.orders (profile_id);