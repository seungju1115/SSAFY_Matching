# 🤝 SSAFY 팀빌딩 매칭 사이트

## 📝 프로젝트 소개
SSAFY 교육생들의 팀 빌딩 과정을 더욱 효율적이고 간편하게 만들어주는 매칭 서비스입니다.

### 주요 기능
- 팀 빌딩 프로세스 간소화
- 간편한 팀원 매칭 시스템
- 효율적인 팀 구성 지원

## 🛠 기술 스택
(추후 사용하실 기술 스택을 추가해주세요)

## 👥 팀원 소개
(팀원 정보를 추가해주세요)

## 📋 컨벤션

### Git 컨벤션

#### 1. Commit 컨벤션
```
<type>(optional scope): <subject> (optional body)
```

##### 커밋 타입
| 타입 | 설명 |
|------|------|
| feat | 새로운 기능 추가 |
| fix | 버그 수정 |
| docs | 문서 수정 (README 등) |
| style | 코드 포맷팅, 세미콜론, 들여쓰기 등 (기능 변화 없음) |
| refactor | 리팩토링 (기능 변화 없음) |
| test | 테스트 코드 추가 또는 수정 |
| chore | 기타 작업 (빌드 설정, 패키지 업데이트 등) |
| perf | 성능 개선 |
| ci | CI 관련 설정 변경 (예: GitHub Actions) |
| build | 빌드 시스템, 의존성 관련 설정 변경 |

##### 커밋 메시지 예시
```
# 간단 예시
feat: 로그인 기능 추가

# 상세 예시
feat(auth): 로그인 기능 추가
이메일과 비밀번호를 입력받아 JWT를 발급하는 로그인 기능 구현.
입력 검증 및 실패 시 에러 메시지 반환 처리 포함.
```

#### 2. PR 컨벤션
```markdown
### Summary
~~를 했습니다.

### Changes
- Feature: 새로운 기능 추가
- Fix: 버그 수정
- Docs: 문서 업데이트
- Refactor: 리팩토링
- Test: 테스트 코드 추가/수정
- Perf: 성능 개선
- Style: 코드 포맷팅
- Chore: 기타 작업

### Details
• 세부사항으로는 이런게 있습니다.

### Related Issue
Closes #이슈번호

### Additional Notes
부가적인 내용은 이런게 있습니다.
```

#### 3. 브랜치 네이밍 컨벤션
```
{type}/{jira-key}/{brief-description}

# 예시
feature/PROJ-123/user-login-api
feature/PROJ-124/password-validation
```

### Jira 컨벤션

#### 1. Story 작성
```markdown
### Describe Feature
As a [사용자 유형], I want [기능] so that [목적]

### Acceptance Criteria
- 구현되어야 하는 기능 1
- 구현되어야 하는 기능 2
- 구현되어야 하는 기능 3

### Describe the Solution
[구현 방향성 기술]

### Alternatives Considered
[대안적 해결 방안]

### Effort Estimation
- Story Points: [ ]
- 예상 소요 시간: [ ]

### Dependencies
- Related Epic:
- Blocked by:
- Blocks:

### Development Checklist
- [ ] 기술 스펙 검토
- [ ] 개발 완료
- [ ] 테스트 작성
- [ ] 코드 리뷰
- [ ] QA 완료
- [ ] 블로킹 이슈 상태 업데이트
```

#### 2. Task 작성
```markdown
### What to do
[구체적인 작업 내용]

### Why
[필요성 설명]

### Checklist
- [ ] 작업 1
- [ ] 작업 2
- [ ] 테스트/검증
```

## 📌 프로젝트 실행 방법
(프로젝트 실행 방법을 추가해주세요)