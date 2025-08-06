// Match 상태 관리
import { create } from 'zustand'

interface MatchState {
  matches: any[]
  setMatches: (matches: any[]) => void
}

export const useMatchStore = create<MatchState>((set) => ({
  matches: [],
  setMatches: (matches) => set({ matches }),
})) 