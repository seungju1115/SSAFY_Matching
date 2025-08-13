// Team 관련 커스텀 훅
import { useState } from 'react'
import { useTeamStore } from '@/stores/teamStore'
import { teamAPI, teamHelpers } from '@/api/team'
import type { TeamRequest, TeamCreateRequest } from '@/types/team'
import { useToast } from '@/hooks/use-toast'

export const useTeam = () => {
  const {
    teams,
    currentTeam,
    teamMembers,
    myTeam,
    isLoading,
    error,
    setTeams,
    setCurrentTeam,
    setTeamMembers,
    setMyTeam,
    addTeam,
    removeTeam,
    updateTeam,
    setLoading,
    setError,
    clearError,
    reset
  } = useTeamStore()

  const { toast } = useToast()

  // 전체 팀 목록 조회
  const fetchAllTeams = async () => {
    try {
      setLoading(true)
      clearError()
      const response = await teamAPI.getAllTeams()
      setTeams(response.data)
      return response.data
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

  // 팀 상세 정보 조회
  const fetchTeamDetail = async (teamId: number) => {
    try {
      setLoading(true)
      clearError()
      const response = await teamAPI.getTeamDetail(teamId)
      setCurrentTeam(response.data)
      return response.data
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

  // 팀 생성
  const createTeam = async (teamData: TeamCreateRequest) => {
    try {
      setLoading(true)
      clearError()
      const response = await teamAPI.createTeam(teamData)
      addTeam(response.data as any) // TeamResponse를 Team으로 변환 필요
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

  // 팀 수정
  const updateTeamInfo = async (teamData: TeamRequest) => {
    try {
      setLoading(true)
      clearError()
      const response = await teamAPI.updateTeam(teamData)
      if (teamData.teamId) {
        updateTeam(teamData.teamId, response.data)
      }
      setCurrentTeam(response.data)
      toast({
        title: "성공",
        description: "팀 정보가 수정되었습니다"
      })
      return response.data
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
      removeTeam(teamId)
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

  // 팀원 목록 조회
  const fetchTeamMembers = async (teamId: number) => {
    try {
      setLoading(true)
      clearError()
      const members = await teamAPI.getTeamMembers(teamId)
      setTeamMembers(members)
      return members
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '팀원 목록을 불러오는데 실패했습니다'
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

  // 팀 검색
  const searchTeams = async (searchCriteria: TeamRequest) => {
    try {
      setLoading(true)
      clearError()
      const response = await teamAPI.searchTeams(searchCriteria)
      return response.data
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '팀 검색에 실패했습니다'
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

  // 팀 가입 요청
  const requestJoinTeam = async (teamId: number, userId: number, message?: string) => {
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
  const inviteToTeam = async (teamId: number, userId: number, message?: string) => {
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

  return {
    // 상태
    teams,
    currentTeam,
    teamMembers,
    myTeam,
    isLoading,
    error,
    
    // 액션
    fetchAllTeams,
    fetchTeamDetail,
    createTeam,
    updateTeamInfo,
    deleteTeam,
    fetchTeamMembers,
    searchTeams,
    requestJoinTeam,
    inviteToTeam,
    
    // 유틸리티
    clearError,
    reset,
    
    // 직접 상태 설정 (필요시)
    setCurrentTeam,
    setMyTeam
  }
}

// 특정 팀 정보만 관리하는 경량 훅
export const useTeamDetail = (teamId?: number) => {
  const [team, setTeam] = useState<any>(null)
  const [members, setMembers] = useState<any[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const { toast } = useToast()

  const fetchTeam = async (id: number = teamId!) => {
    if (!id) return
    
    try {
      setLoading(true)
      setError(null)
      const [teamData, membersData] = await Promise.all([
        teamAPI.getTeamDetail(id),
        teamAPI.getTeamMembers(id)
      ])
      setTeam(teamData.data)
      setMembers(membersData)
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '팀 정보를 불러오는데 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
    } finally {
      setLoading(false)
    }
  }

  return {
    team,
    members,
    loading,
    error,
    fetchTeam,
    refetch: () => fetchTeam(teamId!)
  }
}