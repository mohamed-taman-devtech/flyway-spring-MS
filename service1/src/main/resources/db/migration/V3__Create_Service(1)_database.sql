create table if not exists customer (
   id integer not null auto_increment,
   name varchar(255) not null unique,
   primary key (id)
);

insert into customer (name) values ('Mohamed Taman');
insert into customer (name) values ('Malik Taman');
insert into customer (name) values ('Mariam Taman');
insert into customer (name) values ('Malak Taman');

CREATE OR REPLACE VIEW people AS SELECT id, name FROM customer;