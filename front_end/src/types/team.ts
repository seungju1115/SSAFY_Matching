// Team 관련 타입 정의
import type { UserDetailResponse } from './user'
// export interface Team {
//   teamId: number
//   teamName: string
//   teamDomain?: string
//   memberWanted?: string
//   teamDescription?: string
//   teamPreference?: ProjectGoalEnum[]
//   teamVive?: ProjectViveEnum[]
//   leaderId: number
//   memberCount?: number
// }
//
// export interface TeamDetail extends Team {
//   membersId: number[]
// }
//
// export interface TeamMember {
//   memberId: number
//   username: string
// }
//
// // 팀 생성 요청 (백엔드 TeamCreateRequest와 매칭)
// export interface TeamCreateRequest {
//   leaderId: number
//   teamDomain: string
//   teamVive?: ProjectViveEnum[]
//   teamPreference?: ProjectGoalEnum[]
//   backendCount: number
//   frontendCount: number
//   aiCount: number
//   pmCount: number
//   designCount: number
//   teamDescription?: string
// }
//
//
// // 팀 응답 타입
// export interface TeamResponse {
//   teamId: number
//   teamName: string
//   leaderId: number
//   memberCount: number
// }

// Enum 타입들 (백엔드와 일치) - union type으로 대체
export type RequestType = 'JOIN' | 'INVITE'

export type RequestStatus = 'PENDING' | 'APPROVED' | 'REJECTED'

// 프로젝트 목표 타입 (백엔드 ProjectGoalEnum과 매칭)
export type ProjectGoalEnum = 
  | 'JOB'           // 취업우선
  | 'AWARD'         // 수상목표
  | 'PORTFOLIO'     // 포트폴리오중심
  | 'STUDY'         // 학습중심
  | 'IDEA'          // 아이디어실현
  | 'PROFESSIONAL'  // 실무경험
  | 'QUICK'         // 빠른개발
  | 'QUALITY'       // 완성도추구

// 프로젝트 분위기 타입 (백엔드 ProjectViveEnum과 매칭)
export type ProjectViveEnum = 
  | 'CASUAL'        // 반말 지향
  | 'FORMAL'        // 존대 지향
  | 'COMFY'         // 편한 분위기
  | 'RULE'          // 규칙적인 분위기
  | 'LEADER'        // 리더 중심
  | 'DEMOCRACY'     // 합의 중심
  | 'BRANDNEW'      // 새로운 주제
  | 'STABLE'        // 안정적인 주제
  | 'AGILE'         // 애자일 방식
  | 'WATERFALL'     // 워터폴 방식

// 상수 객체로 값들 제공
export const REQUEST_TYPE = {
  JOIN: 'JOIN' as const,
  INVITE: 'INVITE' as const
}

export const REQUEST_STATUS = {
  PENDING: 'PENDING' as const,
  APPROVED: 'APPROVED' as const,
  REJECTED: 'REJECTED' as const
}

// TeamDetailResponse - 백엔드 응답과 매칭
export interface TeamDetailResponse {
  teamId: number
  chatRoomId: number
  teamName: string
  leader: UserDetailResponse
  members: UserDetailResponse[]
  teamDomain: string
  teamVive?: ProjectViveEnum[]
  teamPreference?: ProjectGoalEnum[]
  backendCount: number
  frontendCount: number
  aiCount: number
  pmCount: number
  designCount: number
  teamDescription?: string
  teamStatus: TeamStatus
}

// 백엔드 TeamRequest DTO와 매칭
export interface TeamRequest {
  teamId?: number
  teamName?: string
  leaderId: number
  teamDomain: string
  teamVive?: ProjectViveEnum[]
  teamPreference?: ProjectGoalEnum[]
  backendCount: number
  frontendCount: number
  aiCount: number
  pmCount: number
  designCount: number
  teamDescription?: string
}

// 팀 검색 요청
export interface TeamSearchRequest {
  teamName?: string
  leaderId?: number
}

// 팀 초대 요청
export interface TeamInviteRequest {
  userId: number
  teamId: number
}

// 팀 가입/초대 요청
export interface TeamOffer {
  requestType: RequestType
  userId: number
  message: string
  teamId: number
}

// 팀 멤버십 응답
export interface TeamMembershipResponse {
  id: number
  teamId: number
  userId: number
  requestType: RequestType
  status: RequestStatus
  message: string
  createdAt: string
}

// 팀 상태 타입
// 팀 상태 타입 (백엔드 TeamStatus enum과 매칭)
export type TeamStatus = 'FULL' | 'LOCKED' | 'UNLOCKED'

// API 응답을 위한 래퍼 타입
export interface ApiResponse<T> {
  data: {
    status: number
    message: string
    data: T
  }
}