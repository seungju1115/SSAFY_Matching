// 매칭 관련 커스텀 훅
import { useMatchStore } from '../store/matchStore'

export const useMatch = () => {
  const { matches, setMatches } = useMatchStore()
  
  const findMatches = () => {
    // 매칭 찾기 로직
  }
  
  return { matches, findMatches }
} 