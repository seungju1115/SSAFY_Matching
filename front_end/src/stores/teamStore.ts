// Team 상태 관리 - 정규화된 저장소
import { create } from 'zustand'
import type { TeamDetailResponse } from '@/types/team'
import type { UserDetailResponse } from '@/types/user'

interface TeamState {
  // 정규화된 저장소
  teamDetailsById: Record<number, TeamDetailResponse>
  teamMembersById: Record<number, UserDetailResponse[]>
  usersById: Record<number, UserDetailResponse>
  
  // 캐시 관리
  cacheTimestamps: Record<number, number>
  CACHE_TTL: number // 5분
  
  // 로딩 상태
  isLoading: boolean
  error: string | null
  
  // 액션들
  setTeamDetail: (teamDetail: TeamDetailResponse) => void
  getTeamDetailById: (teamId: number) => TeamDetailResponse | undefined
  getTeamMembersById: (teamId: number) => UserDetailResponse[] | undefined
  getUserById: (userId: number) => UserDetailResponse | undefined
  
  // 캐시 관리
  isCacheStale: (teamId: number) => boolean
  invalidateTeamCache: (teamId: number) => void
  
  // 상태 관리
  setLoading: (loading: boolean) => void
  setError: (error: string | null) => void
  clearError: () => void
  
  // 리셋
  reset: () => void
}

const initialState = {
  teamDetailsById: {},
  teamMembersById: {},
  usersById: {},
  cacheTimestamps: {},
  CACHE_TTL: 5 * 60 * 1000, // 5분
  isLoading: false,
  error: null,
}

export const useTeamStore = create<TeamState>((set, get) => ({
  ...initialState,
  
  // TeamDetailResponse를 정규화해서 저장
  setTeamDetail: (teamDetail: TeamDetailResponse) => {
    // console.log('setTeamDetail 호출됨:', teamDetail)
    // console.log('leader:', teamDetail.leader)
    // console.log('members:', teamDetail.members)
    
    const { leader, members } = teamDetail
    
    // members가 배열인지 확인
    if (!Array.isArray(members)) {
      console.error('members가 배열이 아님:', members)
      return
    }
    
    const allUsers = [leader, ...members]
    
    set((state) => {
      // 사용자 정보 저장
      const newUsersById = { ...state.usersById }
      allUsers.forEach(user => {
        newUsersById[user.id] = user
      })
      
      return {
        // 팀 상세 정보 저장
        teamDetailsById: {
          ...state.teamDetailsById,
          [teamDetail.teamId]: teamDetail
        },
        
        // 팀 멤버들 저장 (리더 + 멤버들)
        teamMembersById: {
          ...state.teamMembersById,
          [teamDetail.teamId]: allUsers
        },
        
        // 사용자 정보 저장
        usersById: newUsersById,
        
        // 캐시 타임스탬프 업데이트
        cacheTimestamps: {
          ...state.cacheTimestamps,
          [teamDetail.teamId]: Date.now()
        }
      }
    })
  },
  
  // 팀 상세 정보 조회
  getTeamDetailById: (teamId: number) => {
    return get().teamDetailsById[teamId]
  },
  
  // 팀 멤버들 조회
  getTeamMembersById: (teamId: number) => {
    return get().teamMembersById[teamId]
  },
  
  // 사용자 정보 조회
  getUserById: (userId: number) => {
    return get().usersById[userId]
  },
  
  // 캐시가 만료되었는지 확인
  isCacheStale: (teamId: number) => {
    const state = get()
    const timestamp = state.cacheTimestamps[teamId]
    return !timestamp || (Date.now() - timestamp > state.CACHE_TTL)
  },
  
  // 팀 캐시 무효화
  invalidateTeamCache: (teamId: number) => {
    set((state) => {
      const newTeamDetailsById = { ...state.teamDetailsById }
      const newTeamMembersById = { ...state.teamMembersById }
      const newCacheTimestamps = { ...state.cacheTimestamps }
      
      delete newTeamDetailsById[teamId]
      delete newTeamMembersById[teamId]
      delete newCacheTimestamps[teamId]
      
      return {
        teamDetailsById: newTeamDetailsById,
        teamMembersById: newTeamMembersById,
        cacheTimestamps: newCacheTimestamps
      }
    })
  },
  
  // 상태 관리
  setLoading: (loading) => set({ isLoading: loading }),
  setError: (error) => set({ error }),
  clearError: () => set({ error: null }),
  
  // 리셋
  reset: () => set(initialState),
}))