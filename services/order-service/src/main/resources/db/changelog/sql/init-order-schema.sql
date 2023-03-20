--liquibase formatted sql

--changeset shaplovdv:init-order-schema logicalFilePath:/
create schema ${schema};

create table ${schema}.orders
(
    id               uuid primary key,
    user_id          bigint      not null,
    email            text        not null,
    create_date      timestamptz not null,
    update_date      timestamptz not null,
    delivery_date    timestamptz not null,
    shipping_address text        not null,
    courier_info     text        null,
    order_status     varchar(64) not null,
    error_info       json        null
);

create index if not exists orders_account_id_idx on ${schema}.orders (user_id);

create table ${schema}.order_products
(
    order_id      uuid references ${schema}.orders on delete cascade,
    product_id    uuid,
    current_price numeric(10, 2) not null,
    quantity      int            not null,
    primary key (order_id, product_id)
);

create table ${schema}.cart_items
(
    user_id    bigint,
    product_id uuid,
    quantity   int not null,
    primary key (user_id, product_id)
);
