// User 관련 타입 정의
import type {ProjectGoalEnum, ProjectViveEnum} from "@/types/team.ts";

export interface User {
  id: number | null
  userName: string | null
  role: string | null
  email: string | null
  userProfile?: string | null // 자기소개
  major: boolean | null
  lastClass: number | null
  wantedPosition?: string[] | null // 희망 포지션
  projectGoal?: string[] | null // 프로젝트 선호도
  projectVive?: string[] | null // 개인 성향
  projectExp?: string | null // 프로젝트 경험
  qualification?: string | null // 자격증
  techStack?: string[] | null // 기술 스택
  teamId?: number | null // 소속 팀 ID
  teamName?: string | null // 소속 팀 이름
  isProfileComplete?: boolean | null // 프로필 완료 여부
  isSigned?: boolean
}

export interface LoginCredentials {
  email: string
  password: string
}

// 상세 설정을 위한 타입
export interface UserDetailSettings {
  name: string
  semester?: string
  classNumber: string
  major?: string
  isMajor: boolean
}

// UserDetailResponse - 백엔드 응답과 매칭
export interface UserDetailResponse {
  id: number
  userName: string
  role: string
  email: string
  major: boolean
  lastClass: number
  userProfile?: string
  wantedPosition?: PositionEnum[]
  projectGoal?: ProjectGoalEnum[]
  projectVive?: ProjectViveEnum[]
  projectExp?: string
  qualification?: string
  techStack?: TechEnum[]
}

// 학기 옵션
export const SEMESTER_OPTIONS = [
    { value: '1', label: '1학기' },
    { value: '2', label: '2학기' },
] as const

// 반 옵션 (1반부터 12반까지)
export const CLASS_OPTIONS = Array.from({ length: 20 }, (_, i) => ({
    value: String(i + 1),
    label: `${i + 1}반`
}))

// 전공자 트랙 옵션
export const MAJOR_TRACK_OPTIONS = [
    { value: 'java', label: 'Java' },
    { value: 'embedded', label: '임베디드' },
] as const

// 비전공자 트랙 옵션
export const NON_MAJOR_TRACK_OPTIONS = [
    { value: 'python', label: 'Python' },
    { value: 'java', label: 'Java' },
] as const



// 사용자 관련 Enum 타입들
export type PositionEnum = 'BACKEND' | 'FRONTEND' | 'AI' | 'PM' | 'DESIGN'

export type TechEnum = 
  // Frontend
  | 'REACT' | 'VUE_JS' | 'ANGULAR' | 'NEXT_JS' | 'TYPESCRIPT' | 'JAVASCRIPT'
  | 'HTML' | 'CSS' | 'SCSS' | 'TAILWIND_CSS' | 'REDUX' | 'ZUSTAND'
  
  // Backend  
  | 'NODE_JS' | 'EXPRESS' | 'SPRING' | 'DJANGO' | 'FLASK' | 'NESTJS'
  | 'JAVA' | 'PYTHON' | 'CSHARP' | 'GO' | 'RUBY_ON_RAILS' | 'PHP' | 'JPA'
  
  // Database
  | 'MYSQL' | 'POSTGRESQL' | 'MONGODB' | 'REDIS' | 'FIREBASE' | 'ORACLE'
  | 'SQL_SERVER' | 'DYNAMODB' | 'ELASTICSEARCH'
  
  // DevOps
  | 'DOCKER' | 'KUBERNETES' | 'AWS' | 'AZURE' | 'GCP' | 'JENKINS' 
  | 'GITHUB_ACTIONS' | 'TERRAFORM' | 'ANSIBLE' | 'PROMETHEUS'
  
  // Mobile
  | 'REACT_NATIVE' | 'FLUTTER' | 'SWIFT' | 'KOTLIN' | 'ANDROID' | 'IOS'
  | 'XAMARIN' | 'IONIC'
  
  // AI
  | 'TENSORFLOW' | 'PYTORCH' | 'SCIKIT_LEARN' | 'OPENCV' | 'NLP'
  | 'COMPUTER_VISION' | 'MACHINE_LEARNING' | 'DEEP_LEARNING'