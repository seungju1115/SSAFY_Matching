import { create } from 'zustand'
import { persist, createJSONStorage } from 'zustand/middleware'

interface User {
  id: number | null
  userName: string | null
  role: string | null
  email: string | null
  userProfile?: string | null // 자기소개
  major: boolean | null
  lastClass: number | null
  wantedPosition?: string[] | null // 희망 포지션
  projectGoal?: string[] | null // 프로젝트 선호도
  projectVive?: string[] | null // 개인 성향
  projectExp?: string | null // 프로젝트 경험
  qualification?: string | null // 자격증
  techStack?: string[] | null // 기술 스택
  teamId?: number | null // 소속 팀 ID
  teamName?: string | null // 소속 팀 이름
  isProfileComplete?: boolean | null // 프로필 완료 여부
  isSigned?: boolean
}

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
