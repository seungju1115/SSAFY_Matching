import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Button } from '@/components/ui/button'
import { User, LogOut, Settings, ChevronDown, BarChart3 } from 'lucide-react'
import useUserStore from '@/stores/userStore'

export default function Header() {
  const navigate = useNavigate()
  const { user, isLoggedIn, logout } = useUserStore()
  const [isDropdownOpen, setIsDropdownOpen] = useState(false)

  const handleLogout = () => {
    logout()
    localStorage.removeItem('authToken')
    setIsDropdownOpen(false)
    navigate('/')
  }

  const getUserInitial = (name: string | null) => {
    if (!name) return 'U'
    return name.charAt(0).toUpperCase()
  }

  return (
    <header className="bg-white shadow-sm border-b">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          {/* 로고 */}
          <div className="flex items-center">
            <button
              onClick={() => navigate('/')}
              className="text-2xl font-bold text-blue-600 hover:text-blue-700 transition-colors"
            >
              Match SSAFY
            </button>
          </div>

          {/* 네비게이션 메뉴 */}
          <nav className="hidden md:flex items-center space-x-8">
            <button
              onClick={() => navigate('/dashboard')}
              className="flex items-center space-x-1 text-gray-700 hover:text-blue-600 transition-colors"
            >
              <BarChart3 className="h-4 w-4" />
              <span>대시보드</span>
            </button>
            <button
              onClick={() => navigate('/teams')}
              className="text-gray-700 hover:text-blue-600 transition-colors"
            >
              팀 찾기
            </button>
            <button
              onClick={() => navigate('/make-team')}
              className="text-gray-700 hover:text-blue-600 transition-colors"
            >
              팀 만들기
            </button>
          </nav>

          {/* 사용자 메뉴 */}
          <div className="flex items-center space-x-4">
            {isLoggedIn && user.userName ? (
              <div className="relative">
                <Button
                  variant="ghost"
                  onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                  className="flex items-center space-x-2 text-gray-700 hover:text-blue-600"
                >
                  <div className="w-8 h-8 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center text-sm font-medium">
                    {getUserInitial(user.userName)}
                  </div>
                  <span>{user.userName}</span>
                  <ChevronDown className="h-4 w-4" />
                </Button>
                
                {isDropdownOpen && (
                  <div className="absolute right-0 mt-2 w-56 bg-white rounded-md shadow-lg border border-gray-200 z-50">
                    <div className="p-3 border-b border-gray-100">
                      <p className="font-medium text-gray-900">{user.userName}</p>
                      <p className="text-sm text-gray-500 truncate">{user.email}</p>
                    </div>
                    <div className="py-1">
                      <button
                        onClick={() => {
                          navigate('/profile')
                          setIsDropdownOpen(false)
                        }}
                        className="flex items-center w-full px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                      >
                        <User className="mr-3 h-4 w-4" />
                        프로필
                      </button>
                      <button
                        onClick={() => {
                          navigate('/settings')
                          setIsDropdownOpen(false)
                        }}
                        className="flex items-center w-full px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                      >
                        <Settings className="mr-3 h-4 w-4" />
                        설정
                      </button>
                      <hr className="my-1" />
                      <button
                        onClick={handleLogout}
                        className="flex items-center w-full px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                      >
                        <LogOut className="mr-3 h-4 w-4" />
                        로그아웃
                      </button>
                    </div>
                  </div>
                )}
              </div>
            ) : (
              <div className="flex items-center space-x-3">
                <Button
                  variant="ghost"
                  onClick={() => navigate('/login')}
                  className="text-gray-700 hover:text-blue-600"
                >
                  로그인
                </Button>
                <Button
                  onClick={() => navigate('/signup')}
                  className="bg-blue-600 hover:bg-blue-700 text-white"
                >
                  회원가입
                </Button>
              </div>
            )}
          </div>
        </div>
      </div>
    </header>
  )
}
