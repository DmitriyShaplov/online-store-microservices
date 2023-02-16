--liquibase formatted sql

--changeset shaplovdv:init-order-schema logicalFilePath:/
create schema app_orders;

create table if not exists app_orders.orders
(
    id                 uuid primary key,
    version            int,
    profile_id         bigint      not null,
    create_date        timestamptz not null,
    update_date        timestamptz not null,
    shipping_address   text        not null,
    product_id         uuid        not null,
    quantity           int         not null,
    current_item_price decimal     not null,
    payment_method     varchar(128),
    order_status       varchar(64) not null
);

create index if not exists orders_profile_id_idx on app_orders.orders (profile_id);