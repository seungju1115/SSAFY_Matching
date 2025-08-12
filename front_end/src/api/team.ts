// 팀 관련 API
import apiClient from './axios'
import type {
  TeamDetail,
  TeamRequest,
  TeamCreateRequest,
  TeamResponse,
  TeamInviteRequest,
  TeamOffer,
  TeamMember,
  ApiResponse
} from '@/types/team'

export const teamAPI = {
  /**
   * 팀 생성
   * POST /team
   */
  createTeam: (teamData: TeamCreateRequest): Promise<ApiResponse<TeamResponse>> =>
    apiClient.post('/team', teamData),

  /**
   * 전체 팀 목록 조회
   * GET /team
   */
  getAllTeams: (): Promise<ApiResponse<TeamResponse[]>> =>
    apiClient.get('/team'),

  /**
   * 조건별 팀 검색
   * GET /team/search
   */
  searchTeams: (searchCriteria: TeamRequest): Promise<ApiResponse<TeamResponse[]>> =>
    apiClient.get('/team/search', { data: searchCriteria }),

  /**
   * 팀 상세 정보 조회
   * GET /team/{teamId}
   */
  getTeamDetail: (teamId: number): Promise<ApiResponse<TeamDetail>> =>
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
  updateTeam: (teamData: TeamRequest): Promise<ApiResponse<TeamDetail>> =>
    apiClient.put('/team', teamData),

  /**
   * 팀원 직접 초대 (관리자 권한)
   * GET /team/invitation
   */
  inviteMemberToTeam: (inviteData: TeamInviteRequest): Promise<ApiResponse<TeamDetail>> =>
    apiClient.get('/team/invitation', { data: inviteData }),

  /**
   * 팀 가입 요청 / 초대 요청
   * POST /team/offer
   */
  submitTeamOffer: (offerData: TeamOffer): Promise<ApiResponse<void>> =>
    apiClient.post('/team/offer', offerData),

  /**
   * 팀원 목록 조회
   * GET /team/{teamId}/members
   */
  getTeamMembers: (teamId: number): Promise<TeamMember[]> =>
    apiClient.get(`/team/${teamId}/members`),

  /**
   * 팀 떠나기
   * POST /team/{userId}/leave
   */
  leaveTeam: (userId: number): Promise<ApiResponse<void>> =>
    apiClient.post(`/team/${userId}/leave`)
}

// 편의를 위한 추가 함수들
export const teamHelpers = {
  /**
   * 사용자가 팀에 가입 요청
   */
  requestToJoinTeam: (teamId: number, userId: number, message?: string) =>
    teamAPI.submitTeamOffer({
      teamId,
      userId,
      requestType: 'JOIN' as any,
      message
    }),

  /**
   * 팀에서 사용자에게 초대 요청
   */
  inviteUserToTeam: (teamId: number, userId: number, message?: string) =>
    teamAPI.submitTeamOffer({
      teamId,
      userId,
      requestType: 'INVITE' as any,
      message
    }),

  /**
   * 팀 이름으로 검색
   */
  searchTeamsByName: (teamName: string) =>
    teamAPI.searchTeams({ teamName } as TeamRequest),

  /**
   * 팀장 ID로 검색
   */
  searchTeamsByLeader: (leaderId: number) =>
    teamAPI.searchTeams({ leaderId } as TeamRequest),

  /**
   * 팀 생성 (최소 정보)
   */
  createSimpleTeam: (
    leaderId: number, 
    teamDomain: string, 
    teamDescription?: string,
    backendCount: number = 1,
    frontendCount: number = 1,
    aiCount: number = 0,
    pmCount: number = 0,
    designCount: number = 0
  ) =>
    teamAPI.createTeam({
      leaderId,
      teamDomain,
      teamDescription,
      backendCount,
      frontendCount,
      aiCount,
      pmCount,
      designCount
    })
}