import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Button } from "@/components/ui/button"
import { Code, Menu, X } from "lucide-react"

// Header 컴포넌트
export default function Header() {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)
  const navigate = useNavigate()

  return (
    <header className="bg-white shadow-sm border-b sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-14 sm:h-16">
          {/* 로고 */}
          <div className="flex items-center space-x-2">
            <Link to="/" className="flex items-center space-x-2">
              <Code className="h-6 w-6 sm:h-8 sm:w-8 text-blue-600" />
              <h1 className="text-lg sm:text-2xl font-bold text-gray-900">Match SSAFY</h1>
            </Link>
          </div>
          
          {/* 데스크톱 네비게이션 */}
          <nav className="hidden lg:flex space-x-8">
            <Link to="/" className="text-gray-700 hover:text-blue-600 font-medium transition-colors">팀 찾기</Link>
            <Link to="/" className="text-gray-700 hover:text-blue-600 font-medium transition-colors">개발자 찾기</Link>
            <Link to="/" className="text-gray-700 hover:text-blue-600 font-medium transition-colors">프로젝트</Link>
            <Link to="/" className="text-gray-700 hover:text-blue-600 font-medium transition-colors">커뮤니티</Link>
          </nav>

          {/* 데스크톱 버튼들 */}
          <div className="hidden sm:flex items-center space-x-3">
            <Button 
              variant="ghost" 
              size="sm"
              onClick={() => navigate('/login')}
            >
              로그인
            </Button>
            <Button 
              size="sm"
              onClick={() => navigate('/signup')}
            >
              회원가입
            </Button>
          </div>

          {/* 모바일 메뉴 버튼 */}
          <button
            className="lg:hidden p-2 rounded-md text-gray-600 hover:text-gray-900 hover:bg-gray-100 transition-colors"
            onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
          >
            {mobileMenuOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
          </button>
        </div>

        {/* 모바일 메뉴 */}
        {mobileMenuOpen && (
          <div className="lg:hidden border-t bg-white">
            <div className="px-2 pt-2 pb-3 space-y-1">
              <Link 
                to="/" 
                className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50 rounded-md transition-colors"
                onClick={() => setMobileMenuOpen(false)}
              >
                팀 찾기
              </Link>
              <Link 
                to="/" 
                className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50 rounded-md transition-colors"
                onClick={() => setMobileMenuOpen(false)}
              >
                개발자 찾기
              </Link>
              <Link 
                to="/" 
                className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50 rounded-md transition-colors"
                onClick={() => setMobileMenuOpen(false)}
              >
                프로젝트
              </Link>
              <Link 
                to="/" 
                className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50 rounded-md transition-colors"
                onClick={() => setMobileMenuOpen(false)}
              >
                커뮤니티
              </Link>
            </div>
            <div className="px-2 pb-3 flex space-x-3 sm:hidden">
              <Button 
                variant="ghost" 
                size="sm" 
                className="flex-1"
                onClick={() => {
                  setMobileMenuOpen(false)
                  navigate('/login')
                }}
              >
                로그인
              </Button>
              <Button 
                size="sm" 
                className="flex-1"
                onClick={() => {
                  setMobileMenuOpen(false)
                  navigate('/signup')
                }}
              >
                회원가입
              </Button>
            </div>
          </div>
        )}
      </div>
    </header>
  )
} 