-- H2 데이터베이스 테스트 데이터
-- application 시작 시 자동 실행됨 (spring.jpa.defer-datasource-initialization=true 설정 필요)

-- 팀 데이터 삽입
INSERT INTO team (
    team_name,
    backend_count,
    frontend_count,
    ai_count,
    pm_count,
    design_count,
    team_domain,
    member_wanted,
    team_description
) VALUES
      ('프론트엔드 팀', 1, 2, 0, 1, 1, '웹 개발', 'FRONTEND,DESIGN', '사용자 경험을 중시하는 프론트엔드 개발팀입니다.'),
      ('백엔드 팀', 3, 0, 1, 1, 0, '서버 개발', 'BACKEND,AI', '견고한 서버 아키텍처를 구축하는 백엔드팀입니다.'),
      ('풀스택 팀', 2, 2, 1, 1, 1, '전체 개발', 'BACKEND,FRONTEND,AI', '전 영역을 다루는 풀스택 개발팀입니다.'),
      ('AI 스타트업', 1, 1, 3, 1, 1, 'AI/ML', 'AI,BACKEND,FRONTEND', '혁신적인 AI 솔루션을 만드는 스타트업팀입니다.'),
      ('디자인 중심팀', 1, 1, 0, 1, 2, 'UX/UI', 'DESIGN,FRONTEND', '사용자 중심의 디자인을 추구하는 팀입니다.');

-- 사용자 데이터 삽입
INSERT INTO USERS (
    role,
    user_name,
    user_email,
    major,
    last_class,
    user_profile,
    project_experience,
    qualification,
    team_id
) VALUES
-- 팀 1 (프론트엔드 팀) 멤버들
('student', '김프론트', 'frontend1@test.com', true, 4, '프론트엔드 개발에 열정적인 학생입니다.', 'React 프로젝트 3개 경험', ' 정보처리기사', 1),
('student', '이디자인', 'design1@test.com', false, 3, 'UI/UX 디자인을 전공하고 있습니다.', '앱 디자인 2개 프로젝트', 'GTQ 1급', 1),

-- 팀 2 (백엔드 팀) 멤버들
('student', '박백엔드', 'backend1@test.com', true, 4, '서버 개발과 데이터베이스에 관심이 많습니다.', 'Spring Boot 프로젝트 5개', '정보처리기사', 2),
('student', '최AI', 'ai1@test.com', true, 4, '머신러닝과 딥러닝을 공부하고 있습니다.', 'Python ML 프로젝트 3개', 'ADsP', 2),

-- 팀 3 (풀스택 팀) 멤버들
('student', '정풀스택', 'fullstack1@test.com', true, 4, '프론트엔드와 백엔드 모두 경험이 있습니다.', '풀스택 프로젝트 4개', 'AWS 자격증', 3),

-- 팀 없는 후보자들 (추천 대상)
('student', '홍길동', 'hong@test.com', true, 4, '새로운 팀을 찾고 있는 백엔드 개발자입니다.', 'Java Spring 프로젝트 경험', '정보처리기사', NULL),
('student', '김철수', 'kim@test.com', false, 3, '프론트엔드 개발을 배우고 있는 학생입니다.', 'React 독학 중', NULL, NULL),
('student', '이영희', 'lee@test.com', true, 4, '데이터 분석과 AI에 관심이 있습니다.', 'Python 데이터 분석 프로젝트', 'ADsP', NULL),
('student', '박민수', 'park@test.com', false, 2, '디자인과 프론트엔드에 관심이 있습니다.', '포트폴리오 사이트 제작', NULL, NULL),
('student', '조현우', 'cho@test.com', true, 4, '팀 리딩 경험이 있는 PM 지망생입니다.', '프로젝트 관리 경험 2회', 'PMP 준비중', NULL);

-- 사용자 포지션 정보 (wanted_position)
INSERT INTO USER_WANTED_POSITION (user_user_id, wanted_position) VALUES
-- 팀 멤버들
(1, 'FRONTEND'), (1, 'DESIGN'),
(2, 'DESIGN'), (2, 'FRONTEND'),
(3, 'BACKEND'), (3, 'AI'),
(4, 'AI'), (4, 'BACKEND'),
(5, 'FRONTEND'), (5, 'BACKEND'),

-- 후보자들
(6, 'BACKEND'), (6, 'AI'),
(7, 'FRONTEND'), (7, 'DESIGN'),
(8, 'AI'), (8, 'BACKEND'),
(9, 'DESIGN'), (9, 'FRONTEND'),
(10, 'PM'), (10, 'BACKEND');

-- 사용자 프로젝트 목표 (project_preference)
INSERT INTO USER_PROJECT_GOAL (user_user_id, project_preference) VALUES
-- 팀 멤버들
(1, 'PORTFOLIO'), (1, 'STUDY'),
(2, 'PORTFOLIO'), (2, 'IDEA'),
(3, 'PROFESSIONAL'), (3, 'QUALITY'),
(4, 'STUDY'), (4, 'IDEA'),
(5, 'JOB'), (5, 'PROFESSIONAL'),

-- 후보자들
(6, 'JOB'), (6, 'PROFESSIONAL'),
(7, 'STUDY'), (7, 'PORTFOLIO'),
(8, 'IDEA'), (8, 'STUDY'),
(9, 'PORTFOLIO'), (9, 'IDEA'),
(10, 'JOB'), (10, 'PROFESSIONAL');

-- 사용자 프로젝트 분위기 (personal_preference)
INSERT INTO USER_PROJECT_VIVE (user_user_id, personal_preference) VALUES
-- 팀 멤버들
(1, 'COMFY'), (1, 'DEMOCRACY'),
(2, 'COMFY'), (2, 'BRANDNEW'),
(3, 'RULE'), (3, 'LEADER'),
(4, 'COMFY'), (4, 'BRANDNEW'),
(5, 'DEMOCRACY'), (5, 'AGILE'),

-- 후보자들
(6, 'RULE'), (6, 'LEADER'),
(7, 'COMFY'), (7, 'DEMOCRACY'),
(8, 'BRANDNEW'), (8, 'AGILE'),
(9, 'COMFY'), (9, 'DEMOCRACY'),
(10, 'FORMAL'), (10, 'LEADER');

-- 팀 프로젝트 목표 (team_preference)
INSERT INTO TEAM_TEAM_PREFERENCE (team_team_id, team_preference) VALUES
                                                                     (1, 'PORTFOLIO'), (1, 'STUDY'),
                                                                     (2, 'PROFESSIONAL'), (2, 'QUALITY'),
                                                                     (3, 'JOB'), (3, 'PROFESSIONAL'),
                                                                     (4, 'IDEA'), (4, 'AWARD'),
                                                                     (5, 'PORTFOLIO'), (5, 'IDEA');

-- 팀 분위기 (team_vive)
INSERT INTO TEAM_TEAM_VIVE (team_team_id, team_vive) VALUES
                                                         (1, 'COMFY'), (1, 'DEMOCRACY'),
                                                         (2, 'RULE'), (2, 'LEADER'),
                                                         (3, 'DEMOCRACY'), (3, 'AGILE'),
                                                         (4, 'BRANDNEW'), (4, 'AGILE'),
                                                         (5, 'COMFY'), (5, 'BRANDNEW');

-- 팀 리더 설정
UPDATE team SET leader_id = 1 WHERE team_id = 1;
UPDATE team SET leader_id = 3 WHERE team_id = 2;
UPDATE team SET leader_id = 5 WHERE team_id = 3;
UPDATE team SET leader_id = NULL WHERE team_id = 4;  -- 리더 없는 팀
UPDATE team SET leader_id = NULL WHERE team_id = 5;  -- 리더 없는 팀