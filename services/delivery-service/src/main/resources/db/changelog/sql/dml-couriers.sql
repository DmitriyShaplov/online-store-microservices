--liquibase formatted sql

--changeset shaplovdv:dml-couriers logicalFilePath:/

-- Insert courier data
INSERT INTO ${schema}.couriers (phone, name)
VALUES ('79998886655', 'Courier 1'),
       ('71112223344', 'Courier 2');