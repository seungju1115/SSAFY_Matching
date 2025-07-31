// User 상태 관리
import { create } from 'zustand'

interface UserState {
  user: null
  setUser: (user: any) => void
}

export const useUserStore = create<UserState>((set) => ({
  user: null,
  setUser: (user) => set({ user }),
})) 