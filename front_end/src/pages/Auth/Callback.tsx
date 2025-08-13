import { useEffect, useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import useUserStore from '@/stores/userStore'
import apiClient from '@/api/axios'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { AlertCircle, CheckCircle2, Loader2 } from 'lucide-react'
import { useEnumMapper } from '@/hooks/useEnumMapper'

export default function AuthCallback() {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const { login, setToken } = useUserStore()
  const { convertUserData } = useEnumMapper()
  const [status, setStatus] = useState<'loading' | 'success' | 'error' | 'signup-required'>('loading')
  const [message, setMessage] = useState('')

  useEffect(() => {
    const handleCallback = async () => {
      try {
        console.log('AuthCallback mounted. Current URL:', window.location.href)
        // URL 파라미터 추출
        const token = searchParams.get('token')
        const isSignedUpParam = searchParams.get('isSignedUp')
        const emailParam = searchParams.get('email')
        const userDataParam = searchParams.get('user') // 기존 방식 호환
        console.log('Extracted from URL:', { token, isSignedUpParam, emailParam, userDataParam })

        // isSignedUp 파라미터 우선 처리 (신규 백엔드 포맷)
        if (isSignedUpParam === 'false') {
          // 회원가입이 필요
          if (!emailParam) {
            setStatus('error')
            setMessage('이메일 정보가 없습니다.')
            return
          }
          setStatus('signup-required')
          setMessage('회원가입이 필요합니다.')
          setTimeout(() => {
            navigate('/setup', { state: { email: emailParam } })
          }, 1500)
          return
        }

        // isSignedUp=true (신규 포맷)
        if (isSignedUpParam === 'true') {
          if (!token) {
            setStatus('error')
            setMessage('인증 토큰이 없습니다.')
            return
          }
          // 토큰 저장 후 유저 정보 요청
          // sessionStorage.setItem('authToken', token)
          setToken(token)
          try {
            const res = await apiClient.get('/users/profile')
            const userData = res.data?.data
            if (!userData) {
              throw new Error('사용자 정보를 가져오지 못했습니다.')
            }
            
            // Enum → 한글 문자열 변환 (useEnumMapper 훅 사용)
            const convertedUserData = convertUserData(userData)
            
            login(convertedUserData, token)
            setStatus('success')
            setMessage('로그인이 완료되었습니다.')
            setTimeout(() => navigate('/'), 1500)
            return
          } catch (err) {
            console.error(err)
            setStatus('error')
            setMessage('사용자 정보를 가져오는 데 실패했습니다.')
            return
          }
        }

        // 구버전 포맷: token & userParam 필요
        if (!token) {
          setStatus('error')
          setMessage('인증 토큰이 없습니다.')
          return
        }

        if (!userDataParam) {
          setStatus('error')
          setMessage('사용자 정보가 없습니다.')
          return
        }

        // 사용자 정보 파싱 (구버전)
        const userData = JSON.parse(decodeURIComponent(userDataParam))

        // 로그인 처리 (구버전)
        login(userData, token)
        // JWT 토큰 보관
        localStorage.setItem('authToken', token)

        setStatus('success')
        setMessage('로그인이 완료되었습니다.')

        // 메인 페이지로 이동
        setTimeout(() => {
          navigate('/')
        }, 1500)
        
      } catch (error) {
        console.error('Auth callback error:', error)
        setStatus('error')
        setMessage('로그인 처리 중 오류가 발생했습니다.')
      }
    }

    handleCallback()
  }, [searchParams, navigate, login])

  const handleRetry = () => {
    navigate('/login')
  }

  const getStatusIcon = () => {
    switch (status) {
      case 'loading':
        return <Loader2 className="h-8 w-8 animate-spin text-blue-500" />
      case 'success':
        return <CheckCircle2 className="h-8 w-8 text-green-500" />
      case 'signup-required':
        return <AlertCircle className="h-8 w-8 text-orange-500" />
      case 'error':
        return <AlertCircle className="h-8 w-8 text-red-500" />
      default:
        return null
    }
  }

  const getStatusColor = () => {
    switch (status) {
      case 'loading':
        return 'text-blue-600'
      case 'success':
        return 'text-green-600'
      case 'signup-required':
        return 'text-orange-600'
      case 'error':
        return 'text-red-600'
      default:
        return 'text-gray-600'
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
      <Card className="w-full max-w-md shadow-lg">
        <CardHeader className="text-center">
          <div className="flex justify-center mb-4">
            {getStatusIcon()}
          </div>
          <CardTitle className={`text-xl ${getStatusColor()}`}>
            {status === 'loading' && '로그인 처리 중...'}
            {status === 'success' && '로그인 성공!'}
            {status === 'signup-required' && '회원가입 필요'}
            {status === 'error' && '로그인 실패'}
          </CardTitle>
        </CardHeader>
        
        <CardContent className="text-center space-y-4">
          <p className="text-gray-600">{message}</p>
          
          {status === 'signup-required' && (
            <div className="bg-orange-50 border border-orange-200 rounded-lg p-4">
              <p className="text-sm text-orange-800">
                잠시 후 회원가입 페이지로 이동합니다...
              </p>
            </div>
          )}
          
          {status === 'success' && (
            <div className="bg-green-50 border border-green-200 rounded-lg p-4">
              <p className="text-sm text-green-800">
                잠시 후 메인 페이지로 이동합니다...
              </p>
            </div>
          )}
          
          {status === 'error' && (
            <Button onClick={handleRetry} className="w-full">
              다시 시도
            </Button>
          )}
        </CardContent>
      </Card>
    </div>
  )
}
