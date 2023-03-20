--liquibase formatted sql

--changeset shaplovdv:init-delivery-schema logicalFilePath:/
create schema ${schema};

CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE ${schema}.couriers
(
    courier_id SERIAL PRIMARY KEY,
    phone      VARCHAR(11),
    name       VARCHAR(255) NOT NULL
);

CREATE TABLE ${schema}.delivery_slots
(
    id         BIGSERIAL PRIMARY KEY,
    status     varchar(16) NOT NULL CHECK (status IN ('IN_DELIVERY', 'FINISHED') ),
    order_id   uuid        NOT NULL UNIQUE,
    courier_id INTEGER     NOT NULL,
    start_time TIMESTAMPTZ NOT NULL,
    end_time   TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_courier
        FOREIGN KEY (courier_id) REFERENCES ${schema}.couriers (courier_id),
    CONSTRAINT non_overlapping_slots
        EXCLUDE USING gist (courier_id WITH =, tstzrange(start_time, end_time, '[)'::text) WITH &&),
    CONSTRAINT slot_time_range
        CHECK (EXTRACT(HOUR FROM start_time AT TIME ZONE 'UTC') >= 10 AND
               EXTRACT(HOUR FROM end_time AT TIME ZONE 'UTC') <= 22),
    CONSTRAINT slot_start_from_tomorrow
        CHECK (start_time >= CURRENT_DATE + INTERVAL '1 day')
);

