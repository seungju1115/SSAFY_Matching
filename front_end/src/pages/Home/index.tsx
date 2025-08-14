import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Button } from "@/components/ui/button"
import { Users, Search } from "lucide-react"
import Header from "@/components/layout/Header"
import TeamSection from "@/components/features/home/TeamSection"
import DeveloperSection from "@/components/features/home/DeveloperSection"
import TeamsModal from "@/components/features/home/TeamsModal"
import DevelopersModal from "@/components/features/home/DevelopersModal"
import { mockDevelopers } from "@/data/mockData"

// Home 페이지 (메인페이지)
export default function Home() {
  const navigate = useNavigate();
  const [isTeamsModalOpen, setIsTeamsModalOpen] = useState(false)
  const [isDevelopersModalOpen, setIsDevelopersModalOpen] = useState(false)

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
          onCreateTeam={() => navigate('/make-team') }
          onViewAll={() => setIsTeamsModalOpen(true)}
          onViewTeam={(teamId) => console.log('팀 보기 클릭:', teamId)}
        />

        {/* Developer Section */}
        <DeveloperSection 
          developers={mockDevelopers}
          onRegister={() => navigate('/profile-setup')}
          onFilter={() => console.log('필터 클릭')}
          onViewAll={() => setIsDevelopersModalOpen(true)}
          onViewProfile={(developerId) => console.log('프로필 보기 클릭:', developerId)}
        />
      </div>

      {/* 모달들 */}
      <TeamsModal
        isOpen={isTeamsModalOpen}
        onClose={() => setIsTeamsModalOpen(false)}
        onViewTeam={(teamId) => {
          console.log('팀 보기 클릭:', teamId)
          setIsTeamsModalOpen(false)
        }}
      />

      <DevelopersModal
        isOpen={isDevelopersModalOpen}
        onClose={() => setIsDevelopersModalOpen(false)}
        developers={mockDevelopers}
        onViewProfile={(developerId) => {
          console.log('프로필 보기 클릭:', developerId)
          setIsDevelopersModalOpen(false)
        }}
      />
    </div>
  )
}