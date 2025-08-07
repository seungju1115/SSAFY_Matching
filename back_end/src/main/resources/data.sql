-- H2 데이터베이스 테스트 데이터 (data.sql)
-- 팀 생성 테스트를 위한 최소 더미 데이터

-- 팀이 없는 사용자 한 명만 삽입
INSERT INTO USERS (user_name, role, user_email, user_profile, major, last_class, wanted_position, project_experience, qualification) 
VALUES ('테스트사용자', 'MEMBER', 'test@example.com', '팀 생성 테스트용 사용자', true, 1, 'BACKEND', '테스트 프로젝트', null);

-- 사용자 기술 스택 추가
INSERT INTO USER_TECH_STACK (user_user_id, tech_stack) VALUES 
(1, 'JAVA'), (1, 'SPRING');

-- 사용자 프로젝트 목표 추가  
INSERT INTO USER_PROJECT_GOAL (user_user_id, project_preference) VALUES 
(1, 'STUDY');

-- 사용자 개인 성향 추가
INSERT INTO USER_PROJECT_VIVE (user_user_id, personal_preference) VALUES 
(1, 'CASUAL');