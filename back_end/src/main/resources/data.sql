-- H2 데이터베이스 테스트 데이터 (data.sql)
-- 외래키 제약 조건을 고려하여 올바른 순서로 데이터 삽입

-- ================================
-- 1. TEAM 테이블 (리더 없이 먼저 생성)
-- ================================
INSERT INTO TEAM (team_id, team_name, team_domain, member_wanted, team_description) VALUES
                                                                                        (1, '웹개발팀Alpha', '전자상거래 플랫폼 개발', 'React 프론트엔드 개발자, UI/UX 디자이너', '사용자 친화적인 온라인 쇼핑몰 구축을 목표로 하는 팀입니다'),
                                                                                        (2, 'AI혁신팀', '챗봇 및 추천시스템 개발', 'Python 백엔드 개발자, 데이터 엔지니어', '최신 AI 기술을 활용한 스마트 서비스 개발팀'),
                                                                                        (3, '모바일앱팀', '헬스케어 모바일 앱', 'Flutter 개발자, 백엔드 개발자', '건강관리 도우미 앱 개발을 위한 크로스플랫폼 팀'),
                                                                                        (4, '게임개발팀', '인디 게임 제작', 'Unity 개발자, 게임 기획자', '창의적인 인디 게임 제작에 도전하는 팀');

-- ================================
-- 2. USERS 테이블 (team_id 참조)
-- ================================
INSERT INTO USERS (user_id, user_name, role, team_id, user_email, user_profile, major, last_class, wanted_position, project_experience, qualification) VALUES
-- 팀장들
(1, '김팀장', 'LEADER', 1, 'leader1@example.com', '풀스택 개발 경험 5년, 다양한 프로젝트 리딩 경험', true, 4, 'BACKEND', '전자상거래 플랫폼, 금융 시스템 개발', 'AWS 클라우드 자격증'),
(2, '박리더', 'LEADER', 2, 'leader2@example.com', 'AI/ML 전문가, 데이터 사이언스 박사과정', true, 4, 'AI', '챗봇 개발, 추천 시스템 구축', 'TensorFlow 개발자 자격증'),
(3, '최앱장', 'LEADER', 3, 'leader3@example.com', '모바일 앱 개발 전문가, 크로스플랫폼 경험', true, 4, 'FRONTEND', 'Flutter 앱 5개 출시, React Native 경험', 'Google 모바일 웹 전문가'),
(4, '정게임', 'LEADER', 4, 'leader4@example.com', '게임 개발 5년차, Unity 전문가', false, 3, 'FRONTEND', '모바일 게임 3개 출시, PC 게임 개발', 'Unity 인증 개발자'),

-- 팀원들
(5, '이백엔드', 'MEMBER', 1, 'backend1@example.com', 'Spring Boot 전문 백엔드 개발자', true, 3, 'BACKEND', '스타트업 백엔드 시스템 구축', 'Spring 전문가 자격증'),
(6, '신프론트', 'MEMBER', 1, 'frontend1@example.com', 'React 전문 프론트엔드 개발자', false, 2, 'FRONTEND', '웹 애플리케이션 UI/UX 개발', null),
(7, '한디자인', 'MEMBER', 1, 'design1@example.com', 'UI/UX 디자이너, 사용자 경험 전문가', false, 3, 'DESIGN', '모바일 앱 디자인, 웹 서비스 디자인', 'Adobe 인증 디자이너'),

(8, '조데이터', 'MEMBER', 2, 'data1@example.com', '데이터 사이언티스트, 머신러닝 엔지니어', true, 4, 'AI', '빅데이터 분석, 예측 모델 구축', 'Google Cloud ML 자격증'),
(9, '강파이썬', 'MEMBER', 2, 'python1@example.com', 'Python 백엔드 개발자', true, 2, 'BACKEND', 'Django, FastAPI 프로젝트 경험', null),

(10, '윤플러터', 'MEMBER', 3, 'flutter1@example.com', 'Flutter 모바일 앱 개발자', false, 2, 'FRONTEND', '크로스플랫폼 앱 개발 경험', 'Google 모바일 개발자'),
(11, '임서버', 'MEMBER', 3, 'server1@example.com', 'Node.js 백엔드 개발자', true, 3, 'BACKEND', 'RESTful API 설계 및 구현', null),

(12, '성유니티', 'MEMBER', 4, 'unity1@example.com', 'Unity 게임 개발자', false, 1, 'FRONTEND', '인디 게임 개발 프로젝트 참여', null),
(13, '오기획', 'MEMBER', 4, 'plan1@example.com', '게임 기획자, 레벨 디자이너', false, 2, 'PM', '게임 기획 및 밸런싱 경험', null),

-- 팀 없는 사용자들
(14, '독립개발자', 'MEMBER', null, 'independent@example.com', '혼자서도 잘하는 풀스택 개발자', true, 4, 'BACKEND', '개인 프로젝트 다수, 오픈소스 기여', 'AWS Solutions Architect'),
(15, '구직중', 'MEMBER', null, 'jobseeker@example.com', '신입 개발자, 열정 가득', false, 4, 'FRONTEND', '부트캠프 수료, 개인 포트폴리오 사이트 제작', null),
(16, '팀찾기', 'MEMBER', null, 'teamfinder@example.com', 'AI 분야 석사과정, 팀 프로젝트 희망', true, 3, 'AI', '논문 연구, 머신러닝 프로젝트', null);

-- ================================
-- 3. Team의 leader_id 업데이트
-- ================================
UPDATE TEAM SET leader_id = 1 WHERE team_id = 1;
UPDATE TEAM SET leader_id = 2 WHERE team_id = 2;
UPDATE TEAM SET leader_id = 3 WHERE team_id = 3;
UPDATE TEAM SET leader_id = 4 WHERE team_id = 4;

-- ================================
-- 4. ElementCollection 테이블들
-- ================================

-- User 기술 스택 (USERS_TECH_STACK)
INSERT INTO USER_TECH_STACK (user_user_id, tech_stack) VALUES
-- 김팀장 (1) - 백엔드 리더
(1, 'JAVA'), (1, 'SPRING'), (1, 'MYSQL'), (1, 'AWS'), (1, 'DOCKER'),
-- 박리더 (2) - AI 리더
(2, 'PYTHON'), (2, 'TENSORFLOW'), (2, 'PYTORCH'), (2, 'MACHINE_LEARNING'), (2, 'DEEP_LEARNING'),
-- 최앱장 (3) - 모바일 리더
(3, 'FLUTTER'), (3, 'REACT_NATIVE'), (3, 'JAVASCRIPT'), (3, 'TYPESCRIPT'), (3, 'FIREBASE'),
-- 정게임 (4) - 게임 리더
(4, 'CSHARP'), (4, 'JAVASCRIPT'), (4, 'HTML'), (4, 'CSS'),
-- 이백엔드 (5)
(5, 'JAVA'), (5, 'SPRING'), (5, 'JPA'), (5, 'MYSQL'), (5, 'REDIS'),
-- 신프론트 (6)
(6, 'REACT'), (6, 'TYPESCRIPT'), (6, 'HTML'), (6, 'CSS'), (6, 'REDUX'),
-- 한디자인 (7)
(7, 'HTML'), (7, 'CSS'), (7, 'JAVASCRIPT'),
-- 조데이터 (8)
(8, 'PYTHON'), (8, 'TENSORFLOW'), (8, 'SCIKIT_LEARN'), (8, 'POSTGRESQL'), (8, 'ELASTICSEARCH'),
-- 강파이썬 (9)
(9, 'PYTHON'), (9, 'DJANGO'), (9, 'POSTGRESQL'), (9, 'REDIS'),
-- 윤플러터 (10)
(10, 'FLUTTER'), (10, 'FIREBASE'), (10, 'JAVASCRIPT'),
-- 임서버 (11)
(11, 'NODE_JS'), (11, 'EXPRESS'), (11, 'MONGODB'), (11, 'JAVASCRIPT'),
-- 성유니티 (12)
(12, 'CSHARP'), (12, 'JAVASCRIPT'),
-- 오기획 (13)
(13, 'HTML'), (13, 'CSS'),
-- 독립개발자 (14)
(14, 'JAVA'), (14, 'SPRING'), (14, 'REACT'), (14, 'AWS'), (14, 'DOCKER'), (14, 'KUBERNETES'),
-- 구직중 (15)
(15, 'REACT'), (15, 'JAVASCRIPT'), (15, 'HTML'), (15, 'CSS'),
-- 팀찾기 (16)
(16, 'PYTHON'), (16, 'MACHINE_LEARNING'), (16, 'TENSORFLOW');

-- User 프로젝트 목표 (USERS_PROJECT_GOAL)
INSERT INTO USER_PROJECT_GOAL (user_user_id, project_preference) VALUES
-- 김팀장 (1)
(1, 'PROFESSIONAL'), (1, 'QUALITY'),
-- 박리더 (2)
(2, 'STUDY'), (2, 'IDEA'),
-- 최앱장 (3)
(3, 'PORTFOLIO'), (3, 'PROFESSIONAL'),
-- 정게임 (4)
(4, 'IDEA'), (4, 'QUALITY'),
-- 이백엔드 (5)
(5, 'JOB'), (5, 'PROFESSIONAL'),
-- 신프론트 (6)
(6, 'PORTFOLIO'), (6, 'STUDY'),
-- 한디자인 (7)
(7, 'PORTFOLIO'), (7, 'AWARD'),
-- 조데이터 (8)
(8, 'STUDY'), (8, 'PROFESSIONAL'),
-- 강파이썬 (9)
(9, 'JOB'), (9, 'STUDY'),
-- 윤플러터 (10)
(10, 'PORTFOLIO'), (10, 'QUICK'),
-- 임서버 (11)
(11, 'STUDY'), (11, 'QUALITY'),
-- 성유니티 (12)
(12, 'IDEA'), (12, 'PORTFOLIO'),
-- 오기획 (13)
(13, 'IDEA'), (13, 'STUDY'),
-- 독립개발자 (14)
(14, 'PROFESSIONAL'), (14, 'QUALITY'),
-- 구직중 (15)
(15, 'JOB'), (15, 'PORTFOLIO'),
-- 팀찾기 (16)
(16, 'STUDY'), (16, 'IDEA');

-- User 개인 성향 (USERS_PROJECT_VIVE)
INSERT INTO USER_PROJECT_VIVE (user_user_id, personal_preference) VALUES
-- 김팀장 (1)
(1, 'FORMAL'), (1, 'LEADER'), (1, 'STABLE'), (1, 'WATERFALL'),
-- 박리더 (2)
(2, 'CASUAL'), (2, 'DEMOCRACY'), (2, 'BRANDNEW'), (2, 'AGILE'),
-- 최앱장 (3)
(3, 'CASUAL'), (3, 'COMFY'), (3, 'STABLE'), (3, 'AGILE'),
-- 정게임 (4)
(4, 'CASUAL'), (4, 'LEADER'), (4, 'BRANDNEW'), (4, 'AGILE'),
-- 이백엔드 (5)
(5, 'FORMAL'), (5, 'RULE'), (5, 'STABLE'), (5, 'WATERFALL'),
-- 신프론트 (6)
(6, 'CASUAL'), (6, 'COMFY'), (6, 'STABLE'), (6, 'AGILE'),
-- 한디자인 (7)
(7, 'CASUAL'), (7, 'COMFY'), (7, 'BRANDNEW'), (7, 'AGILE'),
-- 조데이터 (8)
(8, 'FORMAL'), (8, 'DEMOCRACY'), (8, 'BRANDNEW'), (8, 'WATERFALL'),
-- 강파이썬 (9)
(9, 'CASUAL'), (9, 'COMFY'), (9, 'STABLE'), (9, 'AGILE'),
-- 윤플러터 (10)
(10, 'CASUAL'), (10, 'DEMOCRACY'), (10, 'BRANDNEW'), (10, 'AGILE'),
-- 임서버 (11)
(11, 'FORMAL'), (11, 'RULE'), (11, 'STABLE'), (11, 'WATERFALL'),
-- 성유니티 (12)
(12, 'CASUAL'), (12, 'COMFY'), (12, 'BRANDNEW'), (12, 'AGILE'),
-- 오기획 (13)
(13, 'CASUAL'), (13, 'DEMOCRACY'), (13, 'BRANDNEW'), (13, 'AGILE'),
-- 독립개발자 (14)
(14, 'FORMAL'), (14, 'LEADER'), (14, 'STABLE'), (14, 'WATERFALL'),
-- 구직중 (15)
(15, 'CASUAL'), (15, 'COMFY'), (15, 'STABLE'), (15, 'AGILE'),
-- 팀찾기 (16)
(16, 'CASUAL'), (16, 'DEMOCRACY'), (16, 'BRANDNEW'), (16, 'AGILE');

-- Team 프로젝트 성향 (TEAM_TEAM_PREFERENCE)
INSERT INTO TEAM_TEAM_PREFERENCE (team_team_id, team_preference) VALUES
-- 웹개발팀Alpha (1)
(1, 'PROFESSIONAL'), (1, 'QUALITY'),
-- AI혁신팀 (2)
(2, 'STUDY'), (2, 'IDEA'),
-- 모바일앱팀 (3)
(3, 'PORTFOLIO'), (3, 'PROFESSIONAL'),
-- 게임개발팀 (4)
(4, 'IDEA'), (4, 'QUALITY');

-- Team 팀 분위기 (TEAM_TEAM_VIVE)
INSERT INTO TEAM_TEAM_VIVE (team_team_id, team_vive) VALUES
-- 웹개발팀Alpha (1)
(1, 'FORMAL'), (1, 'LEADER'), (1, 'STABLE'), (1, 'WATERFALL'),
-- AI혁신팀 (2)
(2, 'CASUAL'), (2, 'DEMOCRACY'), (2, 'BRANDNEW'), (2, 'AGILE'),
-- 모바일앱팀 (3)
(3, 'CASUAL'), (3, 'COMFY'), (3, 'STABLE'), (3, 'AGILE'),
-- 게임개발팀 (4)
(4, 'CASUAL'), (4, 'LEADER'), (4, 'BRANDNEW'), (4, 'AGILE');