export interface DashboardResponse {
  // 전체 사용자 현황
  whole: number
  matchedMajor: number
  mathcedUnmajor: number  // 백엔드 오타 그대로 유지
  unmatchedMajor: number
  unmatchedUnmajor: number
  
  // 메인 포지션별 개발자 수
  ai_main: number
  back_main: number
  front_main: number
  design_main: number
  pm_main: number
  
  // 서브 포지션별 개발자 수
  ai_sub: number
  back_sub: number
  front_sub: number
  design_sub: number
  pm_sub: number
  
  // 도메인별 참여자 수
  domain: Record<string, number>
  
  // 도메인별 포지션 분포 (back, front, ai, design, pm 순서)
  domainPos: Record<string, number[]>
  
  // 기술 스택별 인원 수
  techstacks: Record<string, number>
}

// 계산된 통계 데이터 타입
export interface DashboardStats {
  totalUsers: number
  matchedUsers: number
  unmatchedUsers: number
  matchingRate: number
  
  majorStats: {
    total: number
    matched: number
    unmatched: number
    matchingRate: number
  }
  
  nonMajorStats: {
    total: number
    matched: number
    unmatched: number
    matchingRate: number
  }
  
  positionStats: {
    main: {
      ai: number
      backend: number
      frontend: number
      design: number
      pm: number
    }
    sub: {
      ai: number
      backend: number
      frontend: number
      design: number
      pm: number
    }
  }
}