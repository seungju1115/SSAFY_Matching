// Team 상태 관리
import { create } from 'zustand'
import type { Team, TeamDetail, TeamMember } from '@/types/team'

interface TeamState {
  // 팀 관련 상태
  teams: Team[]
  currentTeam: TeamDetail | null
  teamMembers: TeamMember[]
  myTeam: Team | null
  
  // 로딩 상태
  isLoading: boolean
  error: string | null
  
  // 팀 관련 액션
  setTeams: (teams: Team[]) => void
  setCurrentTeam: (team: TeamDetail | null) => void
  setTeamMembers: (members: TeamMember[]) => void
  setMyTeam: (team: Team | null) => void
  
  // 팀 추가/삭제
  addTeam: (team: Team) => void
  removeTeam: (teamId: number) => void
  updateTeam: (teamId: number, updates: Partial<Team>) => void
  
  // 상태 관리
  setLoading: (loading: boolean) => void
  setError: (error: string | null) => void
  clearError: () => void
  
  // 리셋
  reset: () => void
}

const initialState = {
  teams: [],
  currentTeam: null,
  teamMembers: [],
  myTeam: null,
  isLoading: false,
  error: null,
}

export const useTeamStore = create<TeamState>((set) => ({
  ...initialState,
  
  // 팀 관련 액션
  setTeams: (teams) => set({ teams }),
  setCurrentTeam: (team) => set({ currentTeam: team }),
  setTeamMembers: (members) => set({ teamMembers: members }),
  setMyTeam: (team) => set({ myTeam: team }),
  
  // 팀 추가/삭제/수정
  addTeam: (team) => set((state) => ({ 
    teams: [...state.teams, team] 
  })),
  
  removeTeam: (teamId) => set((state) => ({ 
    teams: state.teams.filter(team => team.teamId !== teamId),
    currentTeam: state.currentTeam?.teamId === teamId ? null : state.currentTeam,
    myTeam: state.myTeam?.teamId === teamId ? null : state.myTeam
  })),
  
  updateTeam: (teamId, updates) => set((state) => ({
    teams: state.teams.map(team => 
      team.teamId === teamId ? { ...team, ...updates } : team
    ),
    currentTeam: state.currentTeam?.teamId === teamId 
      ? { ...state.currentTeam, ...updates } 
      : state.currentTeam,
    myTeam: state.myTeam?.teamId === teamId 
      ? { ...state.myTeam, ...updates } 
      : state.myTeam
  })),
  
  // 상태 관리
  setLoading: (loading) => set({ isLoading: loading }),
  setError: (error) => set({ error }),
  clearError: () => set({ error: null }),
  
  // 리셋
  reset: () => set(initialState),
}))