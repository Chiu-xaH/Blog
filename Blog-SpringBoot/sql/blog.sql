drop database if exists blog_system;
create database blog_system DEFAULT CHARACTER SET utf8mb4;

use blog_system;

create table imageinfo(
    id int primary key auto_increment,
    url text not null,
    filename varchar(255) not null,
    size BIGINT NOT NULL,
    filetype VARCHAR(50) NOT NULL,
    uploadtime timestamp default now(),
    uid int not null,
    type int not null,
    state int default 1
)default charset 'utf8mb4';

drop table if exists userinfo;
create table userinfo(
    id int primary key auto_increment,
    username varchar(100) not null unique,
    password varchar(65) not null,
    photo varchar(500) default '',
    createtime timestamp default now(),
    state int default 1
) default charset 'utf8mb4';

drop table if exists articleinfo;
create table articleinfo(
    id int primary key auto_increment,
    title varchar(100) not null,
    content text not null,
    createtime timestamp default now(),
    uid int not null,
    rcount int not null default 1,
    state int default 1
)default charset 'utf8mb4';
