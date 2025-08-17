// Team API - Backend TeamController와 매칭
import apiClient from './axios'


import type {
  TeamDetailResponse,
  TeamRequest,
  TeamSearchRequest,
  TeamInviteRequest,
  TeamOffer,
  TeamMembershipResponse,
  ApiResponse
} from '@/types/team'

export const teamAPI = {
  /**
   * 팀 생성
   * POST /team
   */
  createTeam: (teamRequest: TeamRequest): Promise<ApiResponse<TeamDetailResponse>> =>
    apiClient.post('/team', teamRequest),

  /**
   * 전체 팀 조회
   * GET /team
   */
  getAllTeams: (): Promise<ApiResponse<TeamDetailResponse[]>> =>
    apiClient.get('/team'),

  /**
   * 팀 조건 검색
   * POST /team/search
   */
  searchTeams: (searchRequest: TeamSearchRequest): Promise<ApiResponse<TeamDetailResponse[]>> =>
    apiClient.post('/team/search', searchRequest),

  /**
   * 팀 상세 정보 조회
   * GET /team/{teamId}
   */
  getTeamDetail: (teamId: number): Promise<ApiResponse<TeamDetailResponse>> =>
    apiClient.get(`/team/${teamId}`),

  /**
   * 팀 삭제
   * DELETE /team/{teamId}
   */
  deleteTeam: (teamId: number): Promise<ApiResponse<void>> =>
    apiClient.delete(`/team/${teamId}`),

  /**
   * 팀 정보 수정
   * PUT /team
   */
  updateTeam: (teamRequest: TeamRequest): Promise<ApiResponse<TeamDetailResponse>> =>
    apiClient.put('/team', teamRequest),

  /**
   * 팀 멤버 직접 초대 (관리자 권한)
   * POST /team/invitation
   */
  inviteMemberToTeam: (inviteRequest: TeamInviteRequest): Promise<ApiResponse<void>> =>
    apiClient.post('/team/invitation', inviteRequest),

  /**
   * 팀 가입/초대 요청
   * POST /team/offer
   */
  submitTeamOffer: (teamOffer: TeamOffer): Promise<ApiResponse<void>> =>
    apiClient.post('/team/offer', teamOffer),

  /**
   * 팀 초대 거절
   * POST /team/{teamId}/reject
   */
  rejectOffer: (teamId: number): Promise<ApiResponse<void>> =>
    apiClient.post(`/team/${teamId}/reject`),

  /**
   * 팀 떠나기
   * POST /team/{userId}/leave
   */
  leaveTeam: (userId: number): Promise<ApiResponse<void>> =>
    apiClient.post(`/team/${userId}/leave`),

  /**
   * 팀 요청 목록 조회
   * GET /team/{teamId}/request
   */
  getTeamRequests: (teamId: number): Promise<ApiResponse<TeamMembershipResponse[]>> =>
    apiClient.get(`/team/${teamId}/request`),

  /**
   * 팀 요청 목록 조회
   * GET /team/{userId}/request/user
   */
  getUserRequests: (userId: number): Promise<ApiResponse<TeamMembershipResponse[]>> =>
    apiClient.get(`/team/${userId}/request/user`),

  /**
   * 팀 잠금 (모집 마감)
   * POST /team/{teamId}/lock
   */
  lockTeam: (teamId: number): Promise<ApiResponse<void>> =>
    apiClient.post(`/team/${teamId}/lock`),

}

// 편의 함수들
export const teamHelpers = {
  /**
   * 사용자가 팀에 가입 요청
   */
  requestToJoinTeam: (teamId: number, userId: number, message: string) =>
    teamAPI.submitTeamOffer({
      requestType: 'JOIN_REQUEST',
      teamId,
      userId,
      message
    }),

  /**
   * 팀에서 사용자에게 초대 요청
   */
  inviteUserToTeam: (teamId: number, userId: number, message: string) =>
    teamAPI.submitTeamOffer({
      requestType: 'INVITE',
      teamId,
      userId,
      message
    }),

  /**
   * 팀 이름으로 검색
   */
  searchTeamsByName: (teamName: string) =>
    teamAPI.searchTeams({ teamName }),

  /**
   * 팀장 ID로 검색
   */
  searchTeamsByLeader: (leaderId: number) =>
    teamAPI.searchTeams({ leaderId }),
}

export default teamAPI