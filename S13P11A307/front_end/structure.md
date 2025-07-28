src/
├── assets/              # 이미지, 폰트 등 정적 파일
├── components/          # 재사용 가능한 컴포넌트
│   ├── common/         # 공통 컴포넌트
│   │   ├── Button/
│   │   ├── Input/
│   │   └── Modal/
│   ├── layout/         # 레이아웃 관련 컴포넌트
│   │   ├── Header/
│   │   ├── Footer/
│   │   └── Sidebar/
│   └── features/       # 특정 기능과 관련된 컴포넌트
│       ├── auth/
│       ├── matching/
│       └── chat/
├── pages/              # 페이지 컴포넌트
│   ├── Home/
│   ├── Login/
│   ├── Profile/
│   ├── Matching/
│   └── Chat/
├── store/              # Zustand 스토어
│   ├── userStore.ts
│   ├── matchStore.ts
│   └── chatStore.ts
├── api/                # API 관련 설정 및 함수
│   ├── axios.ts       # axios 인스턴스 및 설정
│   ├── auth.ts        # 인증 관련 API
│   ├── matching.ts    # 매칭 관련 API
│   └── chat.ts        # 채팅 관련 API
├── services/           # 비즈니스 로직
│   ├── socket.ts      # 소켓 설정 및 이벤트 핸들러
│   └── matching.ts    # 매칭 관련 로직
├── hooks/              # 커스텀 훅
│   ├── useAuth.ts
│   ├── useSocket.ts
│   └── useMatch.ts
├── utils/             # 유틸리티 함수
│   ├── format.ts
│   ├── validation.ts
│   └── constants.ts
├── types/            # TypeScript 타입 정의
│   ├── user.ts
│   ├── match.ts
│   └── chat.ts
├── styles/           # 전역 스타일
│   ├── global.css
│   └── theme.ts
├── App.tsx
├── main.tsx
└── vite-env.d.ts