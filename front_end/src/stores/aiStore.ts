import { create } from 'zustand'
import type { CandidateDto, CandidateDtoDisplay } from '@/types/ai'

interface AIStore {
    // 후보자 추천 관련
    candidates: CandidateDto[]
    candidatesDisplay: CandidateDtoDisplay[] // 한글 변환된 데이터
    currentTeamId: number | null
    isLoadingCandidates: boolean
    candidatesError: string | null

    // Actions
    setCandidates: (candidates: CandidateDto[]) => void
    setCandidatesDisplay: (candidatesDisplay: CandidateDtoDisplay[]) => void
    setCurrentTeamId: (teamId: number | null) => void
    setLoadingCandidates: (loading: boolean) => void
    setCandidatesError: (error: string | null) => void
    clearCandidates: () => void
}

export const useAIStore = create<AIStore>((set) => ({
    // 초기 상태
    candidates: [],
    candidatesDisplay: [],
    currentTeamId: null,
    isLoadingCandidates: false,
    candidatesError: null,

    // Actions
    setCandidates: (candidates: CandidateDto[]) => set({ candidates, candidatesError: null }),

    setCandidatesDisplay: (candidatesDisplay: CandidateDtoDisplay[]) => set({ candidatesDisplay }),

    setCurrentTeamId: (currentTeamId: number | null) => set({ currentTeamId }),

    setLoadingCandidates: (isLoadingCandidates: boolean) => set({ isLoadingCandidates }),

    setCandidatesError: (candidatesError: string | null) => set({ candidatesError }),

    clearCandidates: () => set({
        candidates: [],
        candidatesDisplay: [],
        currentTeamId: null,
        isLoadingCandidates: false,
        candidatesError: null
    })
}))