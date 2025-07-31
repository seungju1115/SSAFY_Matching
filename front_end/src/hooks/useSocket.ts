// 소켓 관련 커스텀 훅
export const useSocket = () => {
  // 소켓 연결 및 이벤트 처리 로직
  
  return {
    connect: () => {},
    disconnect: () => {},
    emit: (_event: string, _data: any) => {},
  }
} 