import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Button } from "@/components/ui/button"
import { Users, Search } from "lucide-react"
import Header from "@/components/layout/Header"
import TeamSection from "@/components/features/home/TeamSection"
import DeveloperSection from "@/components/features/home/DeveloperSection"
import TeamsModal from "@/components/features/home/TeamsModal"
import DevelopersModal from "@/components/features/home/DevelopersModal"
import TeamDetailModal from "@/components/features/home/TeamDetailModal"
import UserProfileModal from "@/components/features/home/UserProfileModal"
import { useTeam, type Team } from "@/hooks/useTeam"
import { useUser } from "@/hooks/useUser"
import type { UserSearchResponse } from "@/types/user"

// Home 페이지 (메인페이지)
export default function Home() {
  const navigate = useNavigate();
  const [isTeamsModalOpen, setIsTeamsModalOpen] = useState(false)
  const [isDevelopersModalOpen, setIsDevelopersModalOpen] = useState(false)
  const [isTeamDetailModalOpen, setIsTeamDetailModalOpen] = useState(false)
  const [selectedTeam, setSelectedTeam] = useState<Team | null>(null)
  
  // 중첩 모달 상태 관리
  const [isProfileModalOpen, setIsProfileModalOpen] = useState(false)
  const [isTeamDetailFromModalOpen, setIsTeamDetailFromModalOpen] = useState(false)
  
  // 프로필 모달 상태
  const [selectedUser, setSelectedUser] = useState<UserSearchResponse | null>(null)
  
  const { requestJoinTeam, inviteToTeam, fetchTeamDetail, convertTeamDetailToTeam } = useTeam()
  const { getUserProfile } = useUser()

  // 팀 상세보기 핸들러
  const handleViewTeam = async (teamId: number) => {
    try {
      console.log('팀 보기:', teamId)
      const teamDetail = await fetchTeamDetail(teamId)
      
      if (teamDetail) {
        const convertedTeam = convertTeamDetailToTeam(teamDetail)
        setSelectedTeam(convertedTeam)
        setIsTeamDetailModalOpen(true)
        setIsTeamDetailFromModalOpen(true)
      }
    } catch (error) {
      console.error('팀 상세 정보 로딩 실패:', error)
    }
  }

  // 프로필 보기 핸들러
  const handleViewProfile = async (developerId: number) => {
    try {
      console.log('프로필 보기:', developerId)
      const userProfile = await getUserProfile(developerId)
      
      if (userProfile) {
        const userSearchResponse: UserSearchResponse = {
          id: userProfile.id,
          userName: userProfile.userName,
          userProfile: userProfile.userProfile,
          major: userProfile.major,
          lastClass: userProfile.lastClass,
          wantedPosition: userProfile.wantedPosition,
          techStack: userProfile.techStack,
          projectGoal: userProfile.projectGoal,
          projectVive: userProfile.projectVive,
          projectExp: userProfile.projectExp,
          qualification: userProfile.qualification
        }
        setSelectedUser(userSearchResponse)
        setIsProfileModalOpen(true)
      }
    } catch (error) {
      console.error('사용자 프로필 로딩 실패:', error)
    }
  }

  // 팀 가입 신청 핸들러
  const handleJoinTeam = async (teamId: number) => {
    try {
      // 임시로 userId는 1로 설정 (실제로는 현재 사용자 ID를 사용해야 함)
      await requestJoinTeam(teamId, 1, "팀에 참여하고 싶습니다.")
    } catch (error) {
      console.error('팀 가입 신청 실패:', error)
    }
  }

  // 사용자 초대 핸들러
  const handleInviteUser = async (userId: number) => {
    try {
      // 임시로 teamId는 1로 설정 (실제로는 현재 사용자의 팀 ID를 사용해야 함)
      await inviteToTeam(1, userId, "저희 팀에 참여해주세요.")
    } catch (error) {
      console.error('사용자 초대 실패:', error)
    }
  }

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
          onViewTeam={handleViewTeam}
        />

        {/* Developer Section */}
        <DeveloperSection 
          onRegister={() => navigate('/profile-setup')}
          onFilter={() => console.log('필터 클릭')}
          onViewAll={() => setIsDevelopersModalOpen(true)}
          onViewProfile={handleViewProfile}
        />
      </div>

      {/* 모달들 */}
      <TeamsModal
        isOpen={isTeamsModalOpen}
        onClose={() => setIsTeamsModalOpen(false)}
        onViewTeam={handleViewTeam}
        isDetailOpen={isTeamDetailFromModalOpen}
      />

      <DevelopersModal
        isOpen={isDevelopersModalOpen}
        onClose={() => setIsDevelopersModalOpen(false)}
        onViewProfile={handleViewProfile}
        isProfileOpen={isProfileModalOpen}
      />

      {/* 팀 상세보기 모달 */}
      <TeamDetailModal
        isOpen={isTeamDetailModalOpen}
        onClose={() => {
          setIsTeamDetailModalOpen(false)
          setIsTeamDetailFromModalOpen(false)
        }}
        team={selectedTeam}
        onJoinRequest={handleJoinTeam}
      />

      {/* 사용자 프로필 모달 */}
      <UserProfileModal
        isOpen={isProfileModalOpen}
        onClose={() => setIsProfileModalOpen(false)}
        user={selectedUser}
        onInvite={handleInviteUser}
      />
    </div>
  )
}