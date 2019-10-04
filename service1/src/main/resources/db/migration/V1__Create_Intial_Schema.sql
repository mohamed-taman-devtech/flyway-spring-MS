-- Create schema is required because we are using MySQL 5.7
-- Flyway with MySQL version 8 creates the schema by default
-- create SCHEMA customer_service;
USE customer_service;

create table user1(
    id bigint not null auto_increment,
    name varchar(255) not null unique,
    primary key (id)
);