// Axios 인스턴스 및 설정
import axios from 'axios'

// 현재 브라우저 도메인을 기반으로 baseURL 생성
export const getBaseURL = (path = '/api') => {
  const baseUrl =
    import.meta.env.VITE_API_URL ||
    `${window.location.protocol}//${window.location.host}`
  return `${baseUrl}${path}`
}

const apiClient = axios.create({
  baseURL: getBaseURL(),
  timeout: 10000,
})

// 인증이 필요 없는 API용 별도 인스턴스
export const publicApiClient = axios.create({
  baseURL: getBaseURL(),
  timeout: 10000,
})

// 요청 인터셉터: JWT 토큰을 헤더에 자동 추가
apiClient.interceptors.request.use(
  (config) => {
    // 인증이 필요 없는 요청 판별
    const urlPath = config.url || ''
    const method = (config.method || 'get').toUpperCase()

    // 조건: 1) 구글 로그인 엔드포인트(GET)
    //       2) 회원가입 프로필 생성 엔드포인트(POST /users/profile)
    const isPublicPath = (
      urlPath.includes('/users/login') ||
      (method === 'POST' && urlPath.includes('/users/profile'))
    )
    
    console.log('API Request:', {
      url: config.url,
      isPublicPath,
      hasToken: !!localStorage.getItem('authToken')
    })
    
    // public path가 아니고 토큰이 있을 때만 헤더 추가
    if (!isPublicPath) {
      const token = localStorage.getItem('authToken')
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
        console.log('Token added to request')
      }
    } else {
      console.log('Public path - no token added')
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 응답 인터셉터: 토큰 만료 시 처리
apiClient.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      // 토큰이 만료되었거나 유효하지 않은 경우
      localStorage.removeItem('authToken')
      localStorage.removeItem('user-storage')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default apiClient