--liquibase formatted sql

--changeset shaplovdv:init-order-schema logicalFilePath:/
create schema os_orders;

create table os_orders.orders
(
    id               uuid primary key,
    user_id          bigint      not null,
    create_date      timestamptz not null,
    update_date      timestamptz not null,
    delivery_hour    int         not null,
    shipping_address text        not null,
    order_status     varchar(64) not null
);

create index if not exists orders_account_id_idx on os_orders.orders (user_id);

create table os_orders.cart_items
(
    user_id    bigint,
    product_id uuid,
    quantity   int not null,
    primary key (user_id, product_id)
);
