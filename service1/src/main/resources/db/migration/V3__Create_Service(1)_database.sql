create table if not exists customer (
   id integer not null auto_increment,
   name varchar(255) not null unique,
   primary key (id)
);

insert into customer (name) values ('Dejan Jerkitj');
insert into customer (name) values ('Goran Erhartič');
insert into customer (name) values ('Ivan Vasiljevic');
insert into customer (name) values ('Mohamed Taman');

CREATE OR REPLACE VIEW people AS SELECT id, name FROM customer;