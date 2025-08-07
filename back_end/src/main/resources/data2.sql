-- 1단계: 사용자 생성 (wanted_position 제외)
INSERT INTO USERS (user_name, role, user_email, user_profile, major, last_class, project_experience, qualification)
VALUES ('테스트사용자', 'MEMBER', 'test@example.com', '팀 생성 테스트용 사용자', true, 1, '테스트 프로젝트', null);

-- 2단계: wanted_position을 별도 테이블에 추가
INSERT INTO USER_WANTED_POSITION (user_user_id, wanted_position) VALUES
                                                                     (1, 'BACKEND'),
                                                                     (1, 'FRONTEND');

-- 나머지는 그대로
INSERT INTO USER_TECH_STACK (user_user_id, tech_stack) VALUES
                                                           (1, 'JAVA'), (1, 'SPRING');

INSERT INTO USER_PROJECT_GOAL (user_user_id, project_preference) VALUES
    (1, 'STUDY');

INSERT INTO USER_PROJECT_VIVE (user_user_id, personal_preference) VALUES
    (1, 'CASUAL');