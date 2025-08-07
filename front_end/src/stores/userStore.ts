import { create } from 'zustand'
import { persist, createJSONStorage } from 'zustand/middleware'

interface User {
  id: number | null
  userName: string | null
  role: string | null
  email: string | null
  major: boolean | null
  lastClass: number | null
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
        major: null,
        lastClass: null,
        isSigned: false,
      },
      isLoggedIn: false,
      token: null,
      setUser: (user) => set({ user, isLoggedIn: true }),
      setToken: (token) => set({ token }),
      login: (user, token) => set({ user, token, isLoggedIn: true }),
      logout: () => set({ 
        user: { id: null, userName: null, role: null, email: null, major: null, lastClass: null, isSigned: false },
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
