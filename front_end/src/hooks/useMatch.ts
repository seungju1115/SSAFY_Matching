// 매칭 관련 커스텀 훅
import { useMatchStore } from '../stores/matchStore'

export const useMatch = () => {
  const { matches, setMatches } = useMatchStore()
  
  const findMatches = () => {
    // 매칭 찾기 로직
    setMatches([]) // 임시
  }
  
  return { matches, findMatches }
} 