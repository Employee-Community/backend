# DROP DATABASE IF EXISTS jobtalk;
# CREATE DATABASE IF NOT EXISTS jobtalk;
# USE jobtalk;

CREATE TABLE TBL_JOBTALK_MEMBER (
                                    idx BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    login_type VARCHAR(20) NOT NULL,
                                    name VARCHAR(50) NOT NULL,
                                    id VARCHAR(50) NOT NULL UNIQUE ,
                                    password VARCHAR(255) NOT NULL,
                                    email VARCHAR(100) NOT NULL UNIQUE,
                                    phone VARCHAR(20) NOT NULL,
                                    nickname VARCHAR(50) NOT NULL,
                                    role ENUM('FREE', 'PREMIUM') NOT NULL DEFAULT 'FREE',
                                    state TINYINT NOT NULL DEFAULT 1,
                                    created_at DATETIME,
                                    updated_at DATETIME
);

CREATE TABLE TBL_JOBTALK_POST_CATEGORY (
                                           idx BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           name VARCHAR(100) NOT NULL,
                                           image_url VARCHAR(255)
);

CREATE TABLE TBL_JOBTALK_POST (
                                  idx BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  name VARCHAR(100),
                                  member_idx BIGINT NOT NULL,
                                  category_idx BIGINT NOT NULL,
                                  title VARCHAR(255) NOT NULL,
                                  contents TEXT NOT NULL,
                                  view_count INT DEFAULT 0,
                                  state TINYINT NOT NULL DEFAULT 1,
                                  created_at DATETIME,
                                  updated_at DATETIME,
                                  FOREIGN KEY (member_idx) REFERENCES TBL_JOBTALK_MEMBER(idx),
                                  FOREIGN KEY (category_idx) REFERENCES TBL_JOBTALK_POST_CATEGORY(idx)
);

CREATE TABLE TBL_JOBTALK_COMMENT (
                                     idx BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     post_id BIGINT NOT NULL,
                                     member_idx BIGINT NOT NULL,
                                     content TEXT NOT NULL,
                                     state TINYINT NOT NULL DEFAULT 1,
                                     created_at DATETIME,
                                     updated_at DATETIME,
                                     FOREIGN KEY (post_id) REFERENCES TBL_JOBTALK_POST(idx),
                                     FOREIGN KEY (member_idx) REFERENCES TBL_JOBTALK_MEMBER(idx)
);