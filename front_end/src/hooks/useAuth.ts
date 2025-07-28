// 인증 관련 커스텀 훅
import { useUserStore } from '../store/userStore'

export const useAuth = () => {
  const { user, setUser } = useUserStore()
  
  const login = (_credentials: any) => {
    // 로그인 로직
    setUser(null) // 임시
  }
  
  const logout = () => {
    // 로그아웃 로직
  }
  
  return { user, login, logout }
} 