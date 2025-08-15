import { useCallback } from 'react'
import { useAIStore } from '@/stores/aiStore'
import { aiApi } from '@/api/ai'
import { useToast } from '@/hooks/use-toast'
import { useEnumMapper } from '@/hooks/useEnumMapper'
import type { CandidateDto, CandidateDtoDisplay, RecommendCandidatesParams } from '@/types/ai'

export const useAI = () => {
  const { toast } = useToast()
  const { 
    mapProjectGoalArray, 
    mapProjectVibeArray, 
    mapTechStackArray,
    mapPosition
  } = useEnumMapper()
  
  const {
    candidates,
    candidatesDisplay,
    currentTeamId,
    isLoadingCandidates,
    candidatesError,
    setCandidates,
    setCandidatesDisplay,
    setCurrentTeamId,
    setLoadingCandidates,
    setCandidatesError,
    clearCandidates
  } = useAIStore()

  // CandidateDto를 화면 표시용으로 변환
  const convertCandidatesToDisplay = useCallback((candidates: CandidateDto[]): CandidateDtoDisplay[] => {
    return candidates.map(candidate => ({
      userId: candidate.userId,
      userName: candidate.userName,
      mainPos: mapPosition(candidate.mainPos),
      subPos: mapPosition(candidate.subPos),
      userProfile: candidate.userProfile,
      goals: mapProjectGoalArray(candidate.goals),
      vives: mapProjectVibeArray(candidate.vives),
      techs: mapTechStackArray(candidate.techs)
    }))
  }, [mapProjectGoalArray, mapProjectVibeArray, mapTechStackArray, mapPosition])

  // 팀을 위한 후보자 추천
  const fetchCandidateRecommendations = useCallback(async ({ teamId, all = false }: RecommendCandidatesParams) => {
    try {
      setLoadingCandidates(true)
      setCandidatesError(null)
      setCurrentTeamId(teamId)
      
      const response = await aiApi.getCandidateRecommendations({ teamId, all })
      setCandidates(response)
      
      // 한글 변환된 데이터도 저장
      const displayData = convertCandidatesToDisplay(response)
      setCandidatesDisplay(displayData)
      
    } catch (err: any) {
      const errorMessage = err?.response?.data?.message || err?.message || '후보자 추천을 불러오는데 실패했습니다'
      setCandidatesError(errorMessage)
      
      toast({
        title: '오류',
        description: errorMessage,
        variant: 'destructive'
      })
    } finally {
      setLoadingCandidates(false)
    }
  }, [])

  // 기본 추천 (제한된 결과)
  const getBasicRecommendations = useCallback(async (teamId: number) => {
    await fetchCandidateRecommendations({ teamId, all: false })
  }, [fetchCandidateRecommendations])

  // 전체 추천 (모든 가능한 매치)
  const getAllRecommendations = useCallback(async (teamId: number) => {
    await fetchCandidateRecommendations({ teamId, all: true })
  }, [fetchCandidateRecommendations])

  // 추천 새로고침
  const refreshRecommendations = useCallback(async () => {
    if (currentTeamId) {
      await fetchCandidateRecommendations({ teamId: currentTeamId, all: false })
    }
  }, [currentTeamId, fetchCandidateRecommendations])

  return {
    // 데이터
    candidates,           // 원본 데이터 (enum)
    candidatesDisplay,    // 화면 표시용 데이터 (한글 변환)
    currentTeamId,
    
    // 상태
    isLoadingCandidates,
    candidatesError,
    
    // 액션
    getBasicRecommendations,    // 기본 추천
    getAllRecommendations,      // 전체 추천
    refreshRecommendations,     // 새로고침
    clearCandidates,           // 데이터 초기화
    
    // 유틸리티
    convertCandidatesToDisplay  // 수동 변환 함수
  }
}