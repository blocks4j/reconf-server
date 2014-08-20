/* http://docs.spring.io/spring-security/site/docs/3.0.x/reference/appendix-schema.html */
create table users (
    username varchar(50) not null primary key,
    password varchar(255) not null,
    enabled boolean not null
);

create table authorities (
    username varchar(50) not null,
    authority varchar(50) not null,
    foreign key (username)
    references users (username)
);

create table reconf_property_v1 (
    component_name varchar(256) not null,
    property_name varchar(256) not null,
    product_name varchar(256) not null,
    rule_name varchar(256) not null,
    property_desc varchar(4096),
    rule_priority integer not null,
    rule_regexp varchar(256) not null,
    property_value clob not null,
    constraint pkPropertyV1 primary key (component_name, property_name, product_name, rule_name)
);

create table reconf_component_v1 (
    component_name varchar(256) not null,
    product_name varchar(256) not null,
    component_desc varchar(4096),
    constraint pkComponentV1 primary key (component_name, product_name)
);

create table reconf_product_v1 (
    product_name varchar(256) not null,
    product_desc varchar(4096),
    constraint pkProductV1 primary key (product_name)
);

create table user_product_v1 (
    product_name varchar(256) not null,
    username varchar(50) not null,
    insertion_date timestamp,
    constraint pkUserProductV1 primary key (product_name, username)
);