// User 관련 타입 정의
export interface User {
  id: string
  email: string
  name: string
  profileImage?: string
  semester?: string // 학기 정보 (1학기, 2학기 등)
  classNumber?: string // 반 정보
  major?: string // 전공 정보
  isMajor?: boolean // 전공/비전공 여부
  isProfileComplete?: boolean // 프로필 설정 완료 여부
  createdAt: Date
}

export interface LoginCredentials {
  email: string
  password: string
}

// 상세 설정을 위한 타입
export interface UserDetailSettings {
  semester: string
  classNumber: string
  major: string
  isMajor: boolean
}

// 학기 옵션
export const SEMESTER_OPTIONS = [
  { value: '1', label: '1학기' },
  { value: '2', label: '2학기' },
] as const

// 반 옵션 (1반부터 12반까지)
export const CLASS_OPTIONS = Array.from({ length: 12 }, (_, i) => ({
  value: String(i + 1),
  label: `${i + 1}반`
}))

// 전공 옵션 (SSAFY 전공 트랙 기준)
export const MAJOR_OPTIONS = [
  { value: 'embedded', label: '임베디드' },
  { value: 'mobile', label: '모바일' },
  { value: 'web', label: '웹' },
  { value: 'ai', label: 'AI' },
  { value: 'data', label: '데이터' },
  { value: 'game', label: '게임' },
  { value: 'blockchain', label: '블록체인' },
  { value: 'iot', label: 'IoT' },
] as const 