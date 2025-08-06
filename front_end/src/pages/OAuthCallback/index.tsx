import { useEffect } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Loader2 } from 'lucide-react'

export default function OAuthCallback() {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()

  useEffect(() => {
    const isSignedUp = searchParams.get('isSignedUp')
    const token = searchParams.get('token')
    const email = searchParams.get('email')

    if (isSignedUp === 'true' && token) {
      // 기존 사용자 - 토큰 저장 후 홈으로 이동
      localStorage.setItem('authToken', token)
      navigate('/', { replace: true })
    } else if (isSignedUp === 'false' && email) {
      // 신규 사용자 - 이메일과 함께 Setup 페이지로 이동
      navigate('/setup', { 
        replace: true, 
        state: { email } 
      })
    } else {
      // 잘못된 파라미터 - 로그인 페이지로 리다이렉트
      console.error('Invalid OAuth callback parameters')
      navigate('/login', { replace: true })
    }
  }, [navigate, searchParams])

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50 flex items-center justify-center p-4">
      <Card className="w-full max-w-md shadow-xl border-0 bg-white/80 backdrop-blur-sm">
        <CardHeader className="text-center pb-6">
          <CardTitle className="text-xl text-gray-800">로그인 처리 중</CardTitle>
        </CardHeader>
        <CardContent className="flex flex-col items-center space-y-4">
          <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
          <p className="text-gray-600 text-center">
            잠시만 기다려주세요...
          </p>
        </CardContent>
      </Card>
    </div>
  )
}