create table user(
    id bigint not null auto_increment,
    name varchar(255) not null unique,
    primary key (id)
);