import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Button } from '@/components/ui/button'
import { Users, Search } from 'lucide-react'
import Header from '@/components/layout/Header'
import TeamSection from '@/components/features/home/TeamSection'
import DeveloperSection from '@/components/features/home/DeveloperSection'
import TeamsModal from '@/components/features/home/TeamsModal'
import DevelopersModal from '@/components/features/home/DevelopersModal'
import UserProfileModal from '@/components/features/home/UserProfileModal'
import TeamDetailModal from '@/components/features/home/TeamDetailModal'
import { mockTeams, mockDevelopers } from '@/data/mockData'
import type { Developer } from '@/components/features/home/DeveloperSection'
import type { Team } from '@/components/features/home/TeamSection'

// Developer를 User 타입으로 변환하는 함수
const convertDeveloperToUser = (developer: Developer) => ({
  id: developer.id.toString(),
  name: developer.name,
  mainPosition: developer.role,
  subPosition: developer.positions?.[0] || '개발자',
  domain: developer.domain || '웹 개발',
  techStack: developer.techStack?.map(tech => tech.name) || [],
  projectPreferences: developer.projectPreferences || ['혁신적인', '사용자 중심'],
  personalPreferences: developer.personalPreferences || ['소통 활발', '책임감 강함'],
  introduction: '함께 성장할 수 있는 프로젝트에 참여하고 싶습니다. 새로운 기술을 배우는 것을 좋아하며, 팀원들과의 협업을 통해 더 나은 결과를 만들어내고 싶습니다.'
})

// User 타입 정의 (UserProfileModal과 동일)
interface User {
  id: string
  name: string
  mainPosition: string
  subPosition: string
  domain: string
  techStack: string[]
  projectPreferences: string[]
  personalPreferences: string[]
  introduction: string
}

// Home 페이지 (메인페이지)
export default function Home() {
  const navigate = useNavigate()
  const [isTeamsModalOpen, setIsTeamsModalOpen] = useState(false)
  const [isDevelopersModalOpen, setIsDevelopersModalOpen] = useState(false)
  const [isUserProfileModalOpen, setIsUserProfileModalOpen] = useState(false)
  const [isTeamDetailModalOpen, setIsTeamDetailModalOpen] = useState(false)
  const [selectedUser, setSelectedUser] = useState<User | null>(null)
  const [selectedTeam, setSelectedTeam] = useState<Team | null>(null)

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      {/* 분리된 Header 컴포넌트 사용 */}
      <Header />

      {/* Hero Section - 모바일 최적화 */}
      <section className="py-12 sm:py-16 lg:py-20 px-4 text-center">
        <div className="max-w-4xl mx-auto">
          <h2 className="text-3xl sm:text-4xl lg:text-5xl font-bold text-gray-900 mb-4 sm:mb-6 leading-tight">
            완벽한 개발 파트너를<br className="sm:hidden" /> 찾아보세요
          </h2>
          <p className="text-lg sm:text-xl text-gray-600 mb-6 sm:mb-8 leading-relaxed">
            함께 성장할 팀원을 찾거나, 꿈의 프로젝트에 참여하세요.<br className="hidden sm:block" /> 
            Match SSAFY에서 최고의 개발자들과 연결되어보세요.
          </p>
          <div className="flex flex-col sm:flex-row justify-center gap-3 sm:gap-4">
            <Button size="lg" className="text-base sm:text-lg px-6 sm:px-8 py-3 w-full sm:w-auto">
              <Users className="mr-2 h-4 w-4 sm:h-5 sm:w-5" />
              팀 만들기
            </Button>
            <Button size="lg" variant="outline" className="text-base sm:text-lg px-6 sm:px-8 py-3 w-full sm:w-auto">
              <Search className="mr-2 h-4 w-4 sm:h-5 sm:w-5" />
              개발자 찾기
            </Button>
          </div>
        </div>
      </section>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pb-12 sm:pb-20">
        {/* Team Section */}
        <TeamSection 
          teams={mockTeams}
          onCreateTeam={() => navigate('/make-team') }
          onViewAll={() => setIsTeamsModalOpen(true)}
          onViewTeam={(teamId) => {
            const team = mockTeams.find(t => t.id === teamId)
            if (team) {
              setSelectedTeam(team)
              setIsTeamDetailModalOpen(true)
            }
          }}
        />

        {/* Developer Section */}
        <DeveloperSection 
          developers={mockDevelopers}
          onRegister={() => navigate('/profile-setup')}
          onFilter={() => console.log('필터 클릭')}
          onViewAll={() => setIsDevelopersModalOpen(true)}
          onViewProfile={(developerId) => {
            const developer = mockDevelopers.find(dev => dev.id === developerId)
            if (developer) {
              setSelectedUser(convertDeveloperToUser(developer))
              setIsUserProfileModalOpen(true)
            }
          }}
        />
      </div>

      {/* 모달들 */}
      <TeamsModal
        isOpen={isTeamsModalOpen}
        onClose={() => setIsTeamsModalOpen(false)}
        teams={mockTeams}
        isDetailOpen={isTeamDetailModalOpen}
        onViewTeam={(teamId) => {
          const team = mockTeams.find(t => t.id === teamId)
          if (team) {
            setSelectedTeam(team)
            setIsTeamDetailModalOpen(true)
            // 중첩 모달 처리를 위해 전체보기 모달을 닫지 않음
            // setIsTeamsModalOpen(false)
          }
        }}
      />

      <DevelopersModal
        isOpen={isDevelopersModalOpen}
        onClose={() => setIsDevelopersModalOpen(false)}
        developers={mockDevelopers}
        isProfileOpen={isUserProfileModalOpen}
        onViewProfile={(developerId) => {
          const developer = mockDevelopers.find(dev => dev.id === developerId)
          if (developer) {
            setSelectedUser(convertDeveloperToUser(developer))
            setIsUserProfileModalOpen(true)
          }
        }}
      />

      <UserProfileModal
        isOpen={isUserProfileModalOpen}
        onClose={() => setIsUserProfileModalOpen(false)}
        user={selectedUser}
        onInvite={(userId) => {
          console.log('초대하기 클릭:', userId)
          // 여기에 초대 로직 구현
        }}
      />

      <TeamDetailModal
        isOpen={isTeamDetailModalOpen}
        onClose={() => setIsTeamDetailModalOpen(false)}
        team={selectedTeam}
        onJoinRequest={(teamId) => {
          console.log('참여 신청 클릭:', teamId)
          // 여기에 참여 신청 로직 구현
        }}
      />
    </div>
  )
}