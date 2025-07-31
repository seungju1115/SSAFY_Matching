CREATE DATABASE TEST;

CREATE TABLE USERS(
user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
username varchar(255) not null,
email varchar(255) unique not null,
role varchar(20)
);

CREATE TABLE TEAMS(
	team_id BIGINT AUTO_INCREMENT,
    name varchar(255),
    primary key(team_id)
);

CREATE TABLE tech_stack (
    tech_stack_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_tech_stack (
    user_tech_stack_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    tech_stack_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (tech_stack_id) REFERENCES tech_stack(tech_stack_id) ON DELETE CASCADE,
    UNIQUE KEY uq_user_stack (user_id, tech_stack_id) -- 중복 방지
);