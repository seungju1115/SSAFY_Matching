import { useState, useEffect } from 'react'
import { useNavigate, useLocation, Outlet } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { 
  User, 
  Calendar, 
  Users, 
  LogOut
} from 'lucide-react'
import Header from '@/components/layout/Header'
import useUserStore from '@/stores/userStore'


// 사이드바 메뉴 아이템 타입
interface SidebarMenuItem {
  id: string
  label: string
  icon: React.ReactNode
  path: string
}

// 프로필 레이아웃 컴포넌트
export default function ProfileLayout() {
  const navigate = useNavigate()
  const location = useLocation()
  const { user, isLoggedIn, logout } = useUserStore()
  const [userProfile, setUserProfile] = useState<{
    id: string
    email: string
    name: string
    profileImage?: string
    semester: string
    classNumber: string
    major: string
    isMajor: boolean
    isProfileComplete: boolean
    createdAt: Date
  } | null>(null)

  // 사용자 정보 로드
  useEffect(() => {
    if (isLoggedIn && user) {
      // 실제로는 API에서 상세 정보를 가져와야 함
      setUserProfile({
        id: String(user.id || '1'),
        email: user.email || 'user@ssafy.com',
        name: user.name || user.userName || '홍길동',
        profileImage: user.profileImage || undefined,
        semester: String(user.semester || 2),
        classNumber: String(user.classNumber || 5),
        major: 'java',
        isMajor: Boolean(user.isMajor ?? user.major ?? true),
        isProfileComplete: Boolean(user.isProfileComplete ?? true),
        createdAt: new Date('2024-01-15')
      })
    }
  }, [isLoggedIn, user])

  // 로그아웃 처리
  const handleLogout = () => {
    logout()
    localStorage.removeItem('authToken')
    navigate('/')
  }

  // 사이드바 메뉴 아이템들
  const sidebarItems: SidebarMenuItem[] = [
    {
      id: 'info',
      label: '내 정보',
      icon: <User className="h-4 w-4" />,
      path: '/profile'
    },
    {
      id: 'waiting',
      label: '대기자 등록 정보',
      icon: <Calendar className="h-4 w-4" />,
      path: '/profile/waiting'
    },
    {
      id: 'team',
      label: '내 팀 정보',
      icon: <Users className="h-4 w-4" />,
      path: '/profile/team'
    }
  ]

  // 사용자 이름의 첫 글자 반환
  const getUserInitial = (name: string) => {
    return name.charAt(0).toUpperCase()
  }

  // 전공/비전공 라벨 반환
  const getMajorLabel = (isMajor: boolean, major?: string) => {
    if (isMajor) {
      return major === 'java' ? 'Java 전공' : '임베디드 전공'
    }
    return major === 'python' ? 'Python 비전공' : 'Java 비전공'
  }

  // 현재 활성 메뉴 확인
  const getActiveMenuItem = () => {
    const currentPath = location.pathname
    return sidebarItems.find(item => item.path === currentPath)?.id || 'info'
  }

  // 로그인하지 않은 경우
  if (!isLoggedIn || !userProfile) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
        <Header />
        <div className="flex items-center justify-center min-h-[calc(100vh-4rem)]">
          <Card className="w-96">
            <CardContent className="p-6 text-center">
              <p className="text-gray-600 mb-4">로그인이 필요한 페이지입니다.</p>
              <button 
                onClick={() => navigate('/login')}
                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
              >
                로그인하기
              </button>
            </CardContent>
          </Card>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <Header />
      
      {/* 사용자 프로필 헤더 */}
      <div className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
          <div className="flex items-center space-x-4">
            <Avatar className="h-16 w-16">
              <AvatarImage src={user.profileImage || userProfile?.profileImage} />
              <AvatarFallback className="bg-blue-100 text-blue-600 text-xl font-semibold">
                {getUserInitial(user.name || user.userName || userProfile?.name || '')}
              </AvatarFallback>
            </Avatar>
            <div>
              <h1 className="text-2xl font-bold text-gray-900">{user.name || user.userName || userProfile?.name}</h1>
              <p className="text-gray-600">{userProfile.email}</p>
              <div className="flex items-center space-x-2 mt-1">
                <Badge variant="outline">
                  {user.semester || userProfile?.semester}학기 {user.classNumber || userProfile?.classNumber}반
                </Badge>
                <Badge variant="secondary">
                  {getMajorLabel(Boolean(user.isMajor ?? user.major ?? userProfile?.isMajor), userProfile?.major || 'java')}
                </Badge>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* 메인 컨텐츠 */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
          {/* 사이드바 */}
          <div className="lg:col-span-1">
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">메뉴</CardTitle>
              </CardHeader>
              <CardContent className="p-0">
                <nav className="space-y-1">
                  {sidebarItems.map((item) => (
                    <button
                      key={item.id}
                      onClick={() => navigate(item.path)}
                      className={`w-full flex items-center space-x-3 px-4 py-3 text-left transition-colors ${
                        getActiveMenuItem() === item.id
                          ? 'bg-blue-50 text-blue-600 border-r-2 border-blue-600'
                          : 'text-gray-700 hover:bg-gray-50'
                      }`}
                    >
                      {item.icon}
                      <span className="font-medium">{item.label}</span>
                    </button>
                  ))}
                  {/* 로그아웃 버튼 */}
                  <button
                    onClick={handleLogout}
                    className="w-full flex items-center space-x-3 px-4 py-3 text-left transition-colors text-red-600 hover:bg-red-50"
                  >
                    <LogOut className="h-4 w-4" />
                    <span className="font-medium">로그아웃</span>
                  </button>
                </nav>
              </CardContent>
            </Card>
          </div>

          {/* 메인 컨텐츠 영역 */}
          <div className="lg:col-span-3">
            <Outlet />
          </div>
        </div>
      </div>
    </div>
  )
}
