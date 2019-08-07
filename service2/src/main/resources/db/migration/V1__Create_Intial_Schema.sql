create table user(
    id bigint not null auto_increment,
    name varchar(255) not null unique,
    primary key (id)
);

insert into user (name) values ("Mohamed Taman");
insert into user (name) values ("Malik Taman");
insert into user (name) values ("Mariam Taman");