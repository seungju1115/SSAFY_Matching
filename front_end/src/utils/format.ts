// 포맷팅 유틸리티 함수
export const formatDate = (date: Date) => {
  return date.toLocaleDateString()
}

export const formatTime = (date: Date) => {
  return date.toLocaleTimeString()
} 