import { create } from 'zustand'
import { persist, createJSONStorage } from 'zustand/middleware'
import type {User} from "@/types/user.ts";

interface UserState {
  user: User
  isLoggedIn: boolean
  token: string | null
  setUser: (user: User) => void
  setToken: (token: string) => void
  login: (user: User, token: string) => void
  logout: () => void
}

const useUserStore = create<UserState>()(
  persist(
    (set) => ({
      user: {
        id: null,
        userName: null,
        role: null,
        email: null,
        userProfile: null,
        major: null,
        lastClass: null,
        wantedPosition: null,
        projectGoal: null,
        projectVive: null,
        projectExp: null,
        qualification: null,
        techStack: null,
        teamId: null,
        teamName: null,
        isProfileComplete: null,
        isSigned: false,
      },
      isLoggedIn: false,
      token: null,
      setUser: (user) => set({ user, isLoggedIn: true }),
      setToken: (token) => set({ token }),
      login: (user, token) => set({ user, token, isLoggedIn: true }),
      logout: () => set({ 
        user: { 
          id: null, 
          userName: null, 
          role: null, 
          email: null, 
          userProfile: null,
          major: null, 
          lastClass: null, 
          wantedPosition: null,
          projectGoal: null,
          projectVive: null,
          projectExp: null,
          qualification: null,
          techStack: null,
          teamId: null,
          teamName: null,
          isProfileComplete: null,
          isSigned: false 
        },
        isLoggedIn: false,
        token: null
      }),
    }),
    {
            name: 'user-storage',
      storage: createJSONStorage(() => sessionStorage),
    }
  )
)

export default useUserStore
