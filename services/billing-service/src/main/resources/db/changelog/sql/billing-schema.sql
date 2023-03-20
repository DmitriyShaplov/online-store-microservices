--liquibase formatted sql

--changeset shaplovdv:user-schema logicalFilePath:/
create schema ${schema};

create table ${schema}.accounts
(
    user_id bigint primary key,
    balance numeric(20, 2) check ( balance > 0 )
);

create table ${schema}.payments
(
    id uuid primary key,
    user_id bigint references ${schema}.accounts,
    amount numeric(20, 2) not null check ( amount > 0 ),
    prefix varchar(4),
    suffix varchar(4),
    payment_date timestamptz not null default now()
);

create index payments_user_id_idx on ${schema}.payments (user_id);

create table ${schema}.transactions
(
    id bigserial primary key,
    user_id bigint references ${schema}.accounts,
    order_id uuid,
    type varchar(64),
    amount numeric(20, 2) not null,
    date timestamptz not null default now()
);

create index transactions_user_id_idx on ${schema}.transactions (user_id);
create index transactions_order_id_idx on ${schema}.transactions (order_id);

