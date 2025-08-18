# Team API 사용 가이드

백엔드 TeamController와 연결되는 프론트엔드 Team 관련 스크립트들입니다.

## 📁 파일 구조

```
src/
├── types/team.ts           # Team 관련 타입 정의
├── api/team.ts            # Team API 함수들
├── store/matchStore.ts    # Team 상태 관리 (Zustand)
├── hooks/useTeam.ts       # Team 커스텀 훅
└── components/features/team/TeamList.tsx  # 사용 예시 컴포넌트
```

## 🔧 사용법

### 1. 기본 팀 조회

```typescript
import { useTeam } from '@/hooks/useTeam'

function TeamComponent() {
  const { teams, fetchAllTeams, isLoading } = useTeam()
  
  useEffect(() => {
    fetchAllTeams()
  }, [])
  
  return (
    <div>
      {isLoading ? '로딩중...' : teams.map(team => (
        <div key={team.teamId}>{team.teamName}</div>
      ))}
    </div>
  )
}
```

### 2. 팀 생성

```typescript
const { createTeam } = useTeam()

const handleCreateTeam = async () => {
  try {
    await createTeam({
      teamName: "새로운 팀",
      leaderId: 1,
      teamDescription: "팀 설명",
      teamDomain: "웹 개발"
    })
  } catch (error) {
    console.error('팀 생성 실패:', error)
  }
}
```

### 3. 팀 가입 요청

```typescript
const { requestJoinTeam } = useTeam()

const handleJoinRequest = async (teamId: number, userId: number) => {
  try {
    await requestJoinTeam(teamId, userId, "가입하고 싶습니다!")
  } catch (error) {
    console.error('가입 요청 실패:', error)
  }
}
```

### 4. 팀 검색

```typescript
const { searchTeams } = useTeam()

const handleSearch = async () => {
  try {
    const results = await searchTeams({
      teamName: "React"
    })
    console.log('검색 결과:', results)
  } catch (error) {
    console.error('검색 실패:', error)
  }
}
```

### 5. 직접 API 호출 (저수준)

```typescript
import { teamAPI } from '@/api/team'

// 팀 상세 정보 조회
const teamDetail = await teamAPI.getTeamDetail(1)
console.log(teamDetail.data)

// 팀원 목록 조회
const members = await teamAPI.getTeamMembers(1)
console.log(members)
```

## 🎯 주요 기능

### API 함수들 (src/api/team.ts)
- `createTeam()` - 팀 생성
- `getAllTeams()` - 전체 팀 목록 조회
- `searchTeams()` - 팀 검색
- `getTeamDetail()` - 팀 상세 정보
- `updateTeam()` - 팀 정보 수정
- `deleteTeam()` - 팀 삭제
- `submitTeamOffer()` - 가입/초대 요청
- `getTeamMembers()` - 팀원 목록

### 커스텀 훅 (src/hooks/useTeam.ts)
- 자동 에러 처리 및 토스트 메시지
- 로딩 상태 관리
- Zustand 스토어와 연동
- 편의 함수 제공

### 상태 관리 (src/store/matchStore.ts)
- `teams` - 전체 팀 목록
- `currentTeam` - 현재 선택된 팀
- `myTeam` - 내가 속한 팀
- `teamMembers` - 팀원 목록
- `isLoading`, `error` - 상태 관리

## 🔗 백엔드 연동

백엔드 TeamController의 모든 엔드포인트와 1:1 대응:

- `POST /team` → `createTeam()`
- `GET /team` → `getAllTeams()`
- `GET /team/search` → `searchTeams()`
- `GET /team/{teamId}` → `getTeamDetail()`
- `DELETE /team/{teamId}` → `deleteTeam()`
- `PUT /team` → `updateTeam()`
- `POST /team/offer` → `submitTeamOffer()`
- `GET /team/{teamId}/members` → `getTeamMembers()`

## 📝 타입 안전성

모든 API 함수와 상태는 TypeScript로 타입이 정의되어 있어 컴파일 타임에 오류를 잡을 수 있습니다.

```typescript
// 자동완성과 타입 체크 지원
const team: Team = {
  teamId: 1,
  teamName: "개발팀",
  leaderId: 5,
  // 타입에 맞지 않는 값 입력시 컴파일 에러
}
```

## ⚠️ 주의사항

1. **인증**: 대부분의 API는 JWT 토큰이 필요합니다.
2. **에러 처리**: `useTeam` 훅을 사용하면 자동으로 토스트 메시지가 표시됩니다.
3. **상태 동기화**: 팀 정보가 변경되면 관련 상태들이 자동으로 업데이트됩니다.
4. **환경 변수**: API baseURL은 `VITE_API_URL` 또는 기본값 `localhost:8080/api`를 사용합니다.

## 🔄 기존 코드와의 호환성

기존 프론트엔드 구조를 그대로 유지하며:
- Shadcn/ui 컴포넌트 사용
- Zustand 상태 관리 패턴 유지
- 기존 API 구조와 일관성 유지
- 기존 hook 패턴과 동일한 방식