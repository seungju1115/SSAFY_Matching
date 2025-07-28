// User 관련 타입 정의
export interface User {
  id: string
  email: string
  name: string
  profileImage?: string
  createdAt: Date
}

export interface LoginCredentials {
  email: string
  password: string
} 