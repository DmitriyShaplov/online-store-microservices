--liquibase formatted sql

--changeset shaplovdv:init-products-schema logicalFilePath:/
create schema ${schema};

create table ${schema}.products
(
    id          uuid primary key,
    name        varchar(256)   not null unique,
    price       numeric(20, 2) not null check ( price > 0 ),
    create_date timestamptz    not null default now(),
    update_date timestamptz    not null default now(),
    quantity    int            not null default 0 check ( quantity >= 0 ),
    reserved    int            not null default 0 check ( reserved >= 0 ),
    constraint quantity_reserved_constraint check ( quantity - reserved >= 0 )
);

create table ${schema}.orders
(
    order_id      uuid primary key,
    reserved_date timestamptz not null,
    status        varchar(20) not null
);

create table ${schema}.order_products
(
    order_id   uuid references ${schema}.orders,
    product_id uuid references ${schema}.products,
    quantity   int not null,
    primary key (order_id, product_id)
);
