DROP DATABASE IF EXISTS blog_system;
CREATE DATABASE blog_system DEFAULT CHARACTER SET utf8mb4;
USE blog_system;
-- 存储图片信息
DROP TABLE IF EXISTS image_info;
CREATE TABLE image_info(
    id INT PRIMARY KEY AUTO_INCREMENT,
    create_time TIMESTAMP DEFAULT NOW(),
    url TEXT NOT NULL,
    filename VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    filetype VARCHAR(50) NOT NULL,
    uid INT NOT NULL,
    type TINYINT(1) CHECK (type IN (1, 2, 3)) -- 1用户头像 2博客插图 3评论图片
)DEFAULT CHARSET 'utf8mb4';
-- 存储用户信息
DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info(
    id INT PRIMARY KEY AUTO_INCREMENT,
    create_time TIMESTAMP DEFAULT NOW(),
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(225) NOT NULL,
    email VARCHAR(225) NOT NULL UNIQUE,
    phone_number VARCHAR(20) DEFAULT NULL UNIQUE,
    description TEXT DEFAULT NULL,-- (个人描述)
    sex TINYINT(1) DEFAULT 0 CHECK (sex IN (0, 1, 2)),  -- 0: 未知, 1: 男, 2: 女
    born_date DATE DEFAULT NULL, -- 出生日期
    region VARCHAR(255) DEFAULT NULL,
    website VARCHAR(255) DEFAULT NULL,
    photo VARCHAR(500) DEFAULT '/guest.png', -- 默认使用游客头像
    state TINYINT(1) DEFAULT 1 CHECK (state IN (1, 2))  -- 1: 正常, 2: 冻结
) default charset 'utf8mb4';
-- 存储文章
DROP TABLE IF EXISTS article_info;
CREATE TABLE article_info(
    id INT PRIMARY KEY AUTO_INCREMENT,
    create_time TIMESTAMP DEFAULT NOW(),
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    update_time TIMESTAMP DEFAULT NOW(),
    uid INT NOT NULL,
    state TINYINT(0) DEFAULT 0 CHECK (state IN (0, 1, 2)) -- 0 审核中 1 已发布 2 私人可见
)default charset 'utf8mb4';
-- 存储用户的关注
DROP TABLE IF EXISTS user_follow;
CREATE TABLE user_follow (
    follower_id INT NOT NULL,    -- 关注者ID
    followee_id INT NOT NULL,    -- 被关注者ID
    create_time TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (follower_id, followee_id), -- 联合主键，确保每个关注关系唯一
    FOREIGN KEY (follower_id) REFERENCES user_info(id), -- 外键约束 确保关注者ID存在于User表中
    FOREIGN KEY (followee_id) REFERENCES user_info(id)  -- 外键约束 确保被关注者ID存在于User表中
);
-- 存储收藏夹信息
DROP TABLE IF EXISTS collection_folder;
CREATE TABLE collection_folder (
    id INT PRIMARY KEY AUTO_INCREMENT,
    create_time TIMESTAMP DEFAULT NOW(),
    uid INT NOT NULL,                     -- 用户ID
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    state TINYINT(1) CHECK (state IN (1, 2)), -- 1 隐私 2 开放
    FOREIGN KEY (uid) REFERENCES user_info(id) -- 外键关联到用户表
) DEFAULT CHARSET 'utf8mb4';
-- 存储用户的收藏夹
DROP TABLE IF EXISTS user_collection;
CREATE TABLE user_collection (
    id INT PRIMARY KEY AUTO_INCREMENT,
    create_time TIMESTAMP DEFAULT NOW(),
    uid INT NOT NULL,
    article_id INT NOT NULL,
    folder_id INT DEFAULT NULL,              -- 文件夹ID，外键关联到user_collection_category表,默认null代表不归属文件夹
    UNIQUE KEY unique_user_content (uid, article_id), -- 确保用户不能重复收藏相同内容
    FOREIGN KEY (uid) REFERENCES user_info(id),   -- 外键关联到用户表
    FOREIGN KEY (folder_id) REFERENCES collection_folder(id) -- 外键关联到分类表
) DEFAULT CHARSET 'utf8mb4';
-- 存储评论
DROP TABLE IF EXISTS comment;
CREATE TABLE comment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    create_time TIMESTAMP DEFAULT NOW(),
    uid INT NOT NULL,
    article_id INT DEFAULT NULL,         -- 文章ID，null 表示是回复别的评论
    parent_comment_id INT DEFAULT NULL,  -- 父评论ID，null 表示是直接评论文章
    content TEXT NOT NULL,
    image_url VARCHAR(255) DEFAULT NULL, -- 评论图片（可为空）
    FOREIGN KEY (uid) REFERENCES user_info(id),
    FOREIGN KEY (article_id) REFERENCES article_info(id),
    FOREIGN KEY (parent_comment_id) REFERENCES comment(id) ON DELETE CASCADE -- 级联删除
) DEFAULT CHARSET = 'utf8mb4';
-- 存储评论点赞
DROP TABLE IF EXISTS comment_like;
CREATE TABLE comment_like (
    id INT PRIMARY KEY AUTO_INCREMENT,
    create_time TIMESTAMP DEFAULT NOW(),
    uid INT NOT NULL,
    comment_id INT NOT NULL, -- 被点赞的评论
    FOREIGN KEY (uid) REFERENCES user_info(id),
    FOREIGN KEY (comment_id) REFERENCES comment(id) ON DELETE CASCADE, -- 评论被删除则自动删除点赞
    UNIQUE KEY unique_like (uid, comment_id) -- 防止用户重复点赞
) DEFAULT CHARSET = 'utf8mb4';
-- 存储文章点赞
DROP TABLE IF EXISTS article_like;
CREATE TABLE article_like (
    id INT PRIMARY KEY AUTO_INCREMENT,
    create_time TIMESTAMP DEFAULT NOW(),
    uid INT NOT NULL,          -- 点赞的用户
    article_id INT NOT NULL,   -- 被点赞的文章
    FOREIGN KEY (uid) REFERENCES user_info(id),
    FOREIGN KEY (article_id) REFERENCES article_info(id) ON DELETE CASCADE, -- 文章删除时，相关点赞自动删除
    UNIQUE KEY unique_article_like (uid, article_id) -- 确保用户不能重复点赞同一篇文章
) DEFAULT CHARSET = 'utf8mb4';
-- 存储访问记录
DROP TABLE IF EXISTS article_visit_history;
CREATE TABLE article_visit_history (
    id INT PRIMARY KEY AUTO_INCREMENT,
    create_time TIMESTAMP DEFAULT NOW(),
    uid INT NOT NULL,           -- 观看的用户
    article_id INT NOT NULL,    -- 观看的文章
    last_view_time TIMESTAMP DEFAULT NOW() ON UPDATE NOW(), -- 最近观看时间
    FOREIGN KEY (uid) REFERENCES user_info(id),
    FOREIGN KEY (article_id) REFERENCES article_info(id) ON DELETE CASCADE, -- 文章删除时，历史记录自动删除
    UNIQUE KEY unique_article_read (uid, article_id) -- 同一个用户多次观看同一篇文章只记录一次
) DEFAULT CHARSET = 'utf8mb4';