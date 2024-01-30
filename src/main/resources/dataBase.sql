drop database if exists chat_application;

create database if not exists chat_application;

use chat_application;

create table login(
                      userName VARCHAR(10) PRIMARY KEY,
                      password VARCHAR(10) NOT NULL,
                      image LONGBLOB
);