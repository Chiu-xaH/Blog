drop database if exists blog_system;
create database blog_system DEFAULT CHARACTER SET utf8mb4;

use blog_system;

drop table if exists image_info;
create table image_info(
    id int primary key auto_increment,
    url text not null,
    filename varchar(255) not null,
    size BIGINT NOT NULL,
    filetype VARCHAR(50) NOT NULL,
    create_time timestamp default now(),
    uid int not null,
    type int not null,
    state int default 1 -- 1用户头像 2博客插图
)default charset 'utf8mb4';

drop table if exists user_info;
create table user_info(
    id int primary key auto_increment,
    username varchar(100) not null unique,
    password varchar(65) not null,
    photo varchar(500) default '/guest.png', -- 默认使用游客头像
    create_time timestamp default now(),
    state int default 1 -- 正常1 冻结2
) default charset 'utf8mb4';

drop table if exists article_info;
create table article_info(
    id int primary key auto_increment,
    title varchar(100) not null,
    content text not null,
    create_time timestamp default now(),
    update_time timestamp default now(),
    uid int not null,
    rcount int not null default 1,
    state int default 1 -- 正常1 审核中2
)default charset 'utf8mb4';

drop table if exists user_follow;
CREATE TABLE user_follow (
    follower_id INT NOT NULL,    -- 关注者ID
    followee_id INT NOT NULL,    -- 被关注者ID
    create_time timestamp default now(),
    PRIMARY KEY (follower_id, followee_id), -- 联合主键，确保每个关注关系唯一
    FOREIGN KEY (follower_id) REFERENCES user_info(id), -- 外键约束 确保关注者ID存在于User表中
    FOREIGN KEY (followee_id) REFERENCES user_info(id)  -- 外键约束 确保被关注者ID存在于User表中
);

DROP TABLE IF EXISTS collection_folder;
CREATE TABLE collection_folder (
    id INT PRIMARY KEY AUTO_INCREMENT,
    uid INT NOT NULL,                     -- 用户ID
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    create_time TIMESTAMP DEFAULT NOW(),
    state int default 1,    -- 1 隐私 2 开放
    FOREIGN KEY (uid) REFERENCES user_info(id) -- 外键关联到用户表
) DEFAULT CHARSET 'utf8mb4';


DROP TABLE IF EXISTS user_collection;
CREATE TABLE user_collection (
    id INT PRIMARY KEY AUTO_INCREMENT,
    uid INT NOT NULL,
    article_id INT NOT NULL,
    folder_id INT DEFAULT NULL,              -- 文件夹ID，外键关联到user_collection_category表,默认null代表不归属文件夹
    create_time TIMESTAMP DEFAULT NOW(),
    UNIQUE KEY unique_user_content (uid, article_id), -- 确保用户不能重复收藏相同内容
    FOREIGN KEY (uid) REFERENCES user_info(id),   -- 外键关联到用户表
    FOREIGN KEY (folder_id) REFERENCES collection_folder(id) -- 外键关联到分类表
) DEFAULT CHARSET 'utf8mb4';




