import type { CandidateDto, RecommendCandidatesParams, RecommendCandidatesResponse } from '@/types/ai'
import apiClient from './axios'

export const aiApi = {
  // 팀을 위한 후보자 추천 (기본)
  recommendCandidates: async (teamId: number): Promise<CandidateDto[]> => {
    const response = await apiClient.get<RecommendCandidatesResponse>(`/ai/recommend/candidates/${teamId}`)
    return response.data
  },

  // 팀을 위한 후보자 추천 (전체)
  recommendCandidatesAll: async (teamId: number): Promise<CandidateDto[]> => {
    const response = await apiClient.get<RecommendCandidatesResponse>(`/ai/recommend/candidates/${teamId}/all`)
    return response.data
  },

  // 통합 추천 함수 (all 파라미터로 구분)
  getCandidateRecommendations: async ({ teamId, all = false }: RecommendCandidatesParams): Promise<CandidateDto[]> => {
    const endpoint = all 
      ? `/ai/recommend/candidates/${teamId}/all`
      : `/ai/recommend/candidates/${teamId}`
    
    const response = await apiClient.get<RecommendCandidatesResponse>(endpoint)
    return response.data
  }
}