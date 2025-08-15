// Team 관련 커스텀 훅 - 새로운 teamStore와 연동
import { useCallback } from 'react'
import { useTeamStore } from '@/stores/teamStore'
import { teamAPI, teamHelpers } from '@/api/team'
import type { TeamRequest, TeamDetailResponse, ApiResponse } from '@/types/team'
import { useToast } from '@/hooks/use-toast'
import useUserStore from '@/stores/userStore'
import { useEnumMapper } from '@/hooks/useEnumMapper'

// TeamCard에서 사용하는 Team 타입 (TeamSection과 동일)
export interface Team {
  id: number
  name: string
  description: string
  tech: string[]
  members: number
  maxMembers: number
  deadline: string
  leader: {
    name: string
    avatar: string
    role: string
  }
  domain?: string
  projectPreferences?: string[]
  roleDistribution?: {
    BACKEND: number
    FRONTEND: number
    AI: number
    PM: number
    DESIGN: number
  }
  roleCurrent?: {
    BACKEND: number
    FRONTEND: number
    AI: number
    PM: number
    DESIGN: number
  }
}

export const useTeam = () => {
  const {
    setTeamDetail,
    getTeamDetailById,
    getTeamMembersById,
    getUserById,
    isCacheStale,
    invalidateTeamCache,
    isLoading,
    error,
    setLoading,
    setError,
    clearError,
    reset
  } = useTeamStore()

  const { toast } = useToast()
  const { user, setUser } = useUserStore()
  const { mapProjectGoalArray } = useEnumMapper()

  // TeamDetailResponse를 Team interface로 변환하는 함수
  const convertTeamDetailToTeam = useCallback((teamDetail: TeamDetailResponse): Team => {
    const totalMembers = teamDetail.members?.length || 0
    const maxMembers = (teamDetail.backendCount || 0) +
                      (teamDetail.frontendCount || 0) +
                      (teamDetail.aiCount || 0) +
                      (teamDetail.pmCount || 0) +
                      (teamDetail.designCount || 0)

    return {
      id: teamDetail.teamId || 0,
      name: teamDetail.teamName || '팀 이름 없음',
      description: teamDetail.teamDescription || '팀 설명 없음',
      tech: [], // TeamDetailResponse에는 개별 기술스택 정보가 없음
      members: totalMembers,
      maxMembers: Math.max(maxMembers, 1),
      deadline: '2024-12-31', // 백엔드에 deadline 필드가 없어서 임시값
      leader: {
        name: teamDetail.leader?.userName || '리더',
        avatar: '',
        role: teamDetail.leader?.wantedPosition?.[0] || 'leader'
      },
      domain: teamDetail.teamDomain,
      projectPreferences: mapProjectGoalArray(teamDetail.teamPreference),
      roleDistribution: {
        BACKEND: teamDetail.backendCount || 0,
        FRONTEND: teamDetail.frontendCount || 0,
        AI: teamDetail.aiCount || 0,
        DESIGN: teamDetail.designCount || 0,
        PM: teamDetail.pmCount || 0
      },
      roleCurrent: (() => {
        const roleCurrent = {
          BACKEND: 0,
          FRONTEND: 0,
          AI: 0,
          DESIGN: 0,
          PM: 0
        }

        // 멤버들의 주 포지션으로 현재 인원 계산
        teamDetail.members?.forEach(member => {
          const position = member.wantedPosition?.[0]
          if (position && position in roleCurrent) {
            roleCurrent[position as keyof typeof roleCurrent]++
          }
        })

        return roleCurrent
      })()
    }
  }, [mapProjectGoalArray])

  // 팀 상세 정보 조회 (캐시 고려)
  const fetchTeamDetail = async (teamId: number, forceRefresh = false) => {
    try {
      // 캐시가 유효하고 강제 새로고침이 아닐 때
      if (!forceRefresh && !isCacheStale(teamId)) {
        const cachedTeam = getTeamDetailById(teamId)
        if (cachedTeam) {
          return cachedTeam
        }
      }

      setLoading(true)
      clearError()
      
      const response = await teamAPI.getTeamDetail(teamId)
      
      // TeamDetailResponse를 teamStore에 정규화해서 저장
      setTeamDetail(response.data.data)



      return response.data.data
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '팀 정보를 불러오는데 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
      throw err
    } finally {
      setLoading(false)
    }
  }

  // 전체 팀 목록 조회
  const fetchAllTeams = async () => {
    try {
      setLoading(true)
      clearError()
      
      const response = await teamAPI.getAllTeams()
      
      // 각 TeamDetailResponse를 teamStore에 저장
      response.data.data.forEach(teamDetail => {
        setTeamDetail(teamDetail)
      })
      
      return response.data.data
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '팀 목록을 불러오는데 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
      throw err
    } finally {
      setLoading(false)
    }
  }

  // 팀 생성
  const createTeam = async (teamRequest: TeamRequest) => {
    try {
      setLoading(true)
      clearError()
      
      const response: ApiResponse<TeamDetailResponse> = await teamAPI.createTeam(teamRequest)
      
      // 중첩된 응답 구조에서 실제 데이터 추출
      console.log('API 전체 응답:', response)
      console.log('response.data:', response.data)
      console.log('response.data.data:', response.data.data)
      
      // 생성된 TeamDetailResponse를 teamStore에 저장
      setTeamDetail(response.data.data)
      
      // userStore에 팀 정보 업데이트 (기존 user 정보 유지)
      setUser({
        ...user,
        teamId: response.data.data.teamId,
        teamName: response.data.data.teamName
      })

      toast({
        title: "성공",
        description: "팀이 성공적으로 생성되었습니다"
      })
      
      return response.data
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '팀 생성에 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
      throw err
    } finally {
      setLoading(false)
    }
  }

  // 팀 정보 수정
  const updateTeam = async (teamRequest: TeamRequest) => {
    try {
      setLoading(true)
      clearError()
      
      const response = await teamAPI.updateTeam(teamRequest)
      
      // 수정된 TeamDetailResponse를 teamStore에 저장
      setTeamDetail(response.data.data)
      
      toast({
        title: "성공",
        description: "팀 정보가 수정되었습니다"
      })
      
      return response.data.data
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '팀 수정에 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
      throw err
    } finally {
      setLoading(false)
    }
  }

  // 팀 삭제
  const deleteTeam = async (teamId: number) => {
    try {
      setLoading(true)
      clearError()
      
      await teamAPI.deleteTeam(teamId)
      
      // 캐시에서 제거
      invalidateTeamCache(teamId)
      
      toast({
        title: "성공",
        description: "팀이 삭제되었습니다"
      })
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '팀 삭제에 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
      throw err
    } finally {
      setLoading(false)
    }
  }

  // 팀 탈퇴
  const leaveTeam = async (userId: number) => {
    try {
      setLoading(true)
      clearError()
      await teamAPI.leaveTeam(userId)
      toast({
        title: "성공",
        description: "팀에서 탈퇴했습니다",
      })
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '팀 탈퇴에 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive",
      })
      throw err
    } finally {
      setLoading(false)
    }
  }

  // 팀 가입 요청
  const requestJoinTeam = async (teamId: number, userId: number, message: string) => {
    try {
      setLoading(true)
      clearError()
      
      await teamHelpers.requestToJoinTeam(teamId, userId, message)
      
      toast({
        title: "성공",
        description: "팀 가입 요청이 전송되었습니다"
      })
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '팀 가입 요청에 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
      throw err
    } finally {
      setLoading(false)
    }
  }

  // 팀원 초대
  const inviteToTeam = async (teamId: number, userId: number, message: string) => {
    try {
      setLoading(true)
      clearError()
      
      await teamHelpers.inviteUserToTeam(teamId, userId, message)
      
      toast({
        title: "성공",
        description: "팀원 초대가 전송되었습니다"
      })
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '팀원 초대에 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
      throw err
    } finally {
      setLoading(false)
    }
  }

  // // 내 팀 정보 조회 (userStore와 연동 필요)
  // const getMyTeam = (userId: number) => {
  //   // 사용자가 속한 팀을 찾는 로직은 별도로 구현 필요
  //   // 현재는 teamId를 알고 있다는 가정하에 teamStore에서 조회
  //   return null // 추후 구현
  // }

  return {
    // 상태
    isLoading,
    error,
    
    // 조회 함수
    fetchTeamDetail,
    fetchAllTeams,
    getTeamDetailById,
    getTeamMembersById,
    getUserById,

    // CRUD 함수
    createTeam,
    updateTeam,
    deleteTeam,
    leaveTeam,
    
    // 팀원 관련
    requestJoinTeam,
    inviteToTeam,
    
    // 캐시 관리
    invalidateTeamCache,
    
    // 유틸리티
    clearError,
    reset,
    convertTeamDetailToTeam,
  }
}

export default useTeam