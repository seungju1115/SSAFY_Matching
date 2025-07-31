// Match 관련 타입 정의
export interface Match {
  id: string
  userId: string
  targetUserId: string
  compatibility: number
  status: 'pending' | 'accepted' | 'rejected'
  createdAt: Date
} 