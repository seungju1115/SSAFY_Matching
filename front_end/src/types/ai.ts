// AI 관련 타입 정의
import type { ProjectGoalEnum, ProjectViveEnum } from '@/types/team'
import type { TechEnum, PositionEnum } from '@/types/user'

// CandidateDto - 후보자 정보 (백엔드 DTO와 일치)
export interface CandidateDto {
  userId: number
  userName: string
  mainPos: PositionEnum // 백엔드에서 PositionEnum으로 응답
  subPos: PositionEnum  // 백엔드에서 PositionEnum으로 응답
  userProfile: string
  goals: ProjectGoalEnum[] // enum 배열로 받아서 useEnumMapper로 변환
  vives: ProjectViveEnum[] // enum 배열로 받아서 useEnumMapper로 변환
  techs: TechEnum[] // enum 배열로 받아서 useEnumMapper로 변환
}

// 화면 표시용 변환된 CandidateDto
export interface CandidateDtoDisplay {
  userId: number
  userName: string
  mainPos: string
  subPos: string
  userProfile: string
  goals: string[] // 한글로 변환된 목표
  vives: string[] // 한글로 변환된 분위기
  techs: string[] // 한글로 변환된 기술스택
}

// API 요청 파라미터
export interface RecommendCandidatesParams {
  teamId: number
  all?: boolean // true면 /all 엔드포인트 사용
}

// API 응답 타입 (백엔드에서 받는 원본)
export type RecommendCandidatesResponse = CandidateDto[]