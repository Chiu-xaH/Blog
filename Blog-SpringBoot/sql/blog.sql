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
    state int default 1
)default charset 'utf8mb4';

drop table if exists user_info;
create table user_info(
    id int primary key auto_increment,
    username varchar(100) not null unique,
    password varchar(65) not null,
    photo varchar(500) default '',
    create_time timestamp default now(),
    state int default 1
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
    state int default 1
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

DELIMITER $$

CREATE TRIGGER prevent_self_follow
BEFORE INSERT ON user_follow
FOR EACH ROW
BEGIN
    -- 如果关注者和被关注者相同，抛出异常
    IF NEW.follower_id = NEW.followee_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '用户不能关注自己';
    END IF;
END $$

DELIMITER ;


