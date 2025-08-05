import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { GoogleSignInButton } from '@/components/features/auth/GoogleSignInButton'
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card'
import { Separator } from '@/components/ui/separator'
import Header from '@/components/layout/Header'

export default function Signup() {
  const navigate = useNavigate()

  const [error, setError] = useState<string | null>(null)

  const handleSignupSuccess = () => {
    // 회원가입 성공 시 상세 설정 페이지로 이동
    navigate('/setup')
  }

  const handleSignupError = (error: Error) => {
    setError(error.message)
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <Header />
      
      <main className="container max-w-md mx-auto px-4 py-8 sm:py-12 md:py-16 flex flex-col items-center justify-center">
        <Card className="w-full shadow-lg">
          <CardHeader className="space-y-1 text-center">
            <CardTitle className="text-2xl sm:text-3xl font-bold">회원가입</CardTitle>
            <CardDescription className="text-sm sm:text-base">
              Match SSAFY에 오신 것을 환영합니다!
            </CardDescription>
          </CardHeader>
          
          <CardContent className="space-y-4">
            {error && (
              <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md text-sm">
                {error}
              </div>
            )}
            
            <div className="space-y-4">
              <GoogleSignInButton 
                className="py-6 text-base sm:text-lg"
                onSuccess={handleSignupSuccess}
                onError={handleSignupError}
              />
            </div>
            
            <div className="relative my-6">
              <div className="absolute inset-0 flex items-center">
                <Separator className="w-full" />
              </div>
              <div className="relative flex justify-center text-xs uppercase">
                <span className="bg-white px-2 text-muted-foreground">
                  회원가입 시
                </span>
              </div>
            </div>
            
            <div className="text-xs sm:text-sm text-center text-gray-600">
              <p>회원가입 시 Match SSAFY의 <a href="#" className="text-blue-600 hover:underline">서비스 이용약관</a>과 <a href="#" className="text-blue-600 hover:underline">개인정보처리방침</a>에 동의하게 됩니다.</p>
            </div>
          </CardContent>
          
          <CardFooter className="flex flex-col space-y-4">
            <div className="text-sm text-center">
              이미 계정이 있으신가요?{' '}
              <a 
                href="/login" 
                className="text-blue-600 hover:underline font-medium"
                onClick={(e) => {
                  e.preventDefault()
                  navigate('/login')
                }}
              >
                로그인하기
              </a>
            </div>
          </CardFooter>
        </Card>
        
        <div className="mt-8 text-center text-xs sm:text-sm text-gray-500">
          <p>© 2025 Match SSAFY. All rights reserved.</p>
        </div>
      </main>
    </div>
  )
} 