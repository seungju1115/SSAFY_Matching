import { useState, useEffect } from 'react'
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
import { useTeam } from "@/hooks/useTeam"
import { useUser } from "@/hooks/useUser"
import { teamAPI } from '@/api/team'
import { userAPI } from '@/api/user'
import { useEnumMapper } from '@/hooks/useEnumMapper'
import type { UserSearchResponse, UserDetailResponse } from "@/types/user"
import type { Team } from "@/components/features/home/TeamSection"
import type { Developer } from "@/components/features/home/DeveloperSection"

// Home 페이지 (메인페이지)
export default function Home() {
  const navigate = useNavigate()
  const [isTeamsModalOpen, setIsTeamsModalOpen] = useState(false)
  const [isDevelopersModalOpen, setIsDevelopersModalOpen] = useState(false)
  const [isTeamDetailModalOpen, setIsTeamDetailModalOpen] = useState(false)
  const [selectedTeam, setSelectedTeam] = useState<Team | null>(null)
  
  // 중첩 모달 상태 관리
  const [isProfileModalOpen, setIsProfileModalOpen] = useState(false)
  const [isTeamDetailFromModalOpen, setIsTeamDetailFromModalOpen] = useState(false)
  
  // 프로필 모달 상태
  const [selectedUser, setSelectedUser] = useState<UserSearchResponse | null>(null)
  
  // 데이터 상태
  const [teams, setTeams] = useState<Team[]>([])
  const [developers, setDevelopers] = useState<Developer[]>([])
  
  const { requestJoinTeam, inviteToTeam, fetchTeamDetail } = useTeam()
  const { getUserProfile } = useUser()
  const { mapTechStackArray, mapPositionArray, mapProjectGoalArray } = useEnumMapper()

  // 팀 목록 로드
  useEffect(() => {
    const loadTeams = async () => {
      try {
        const response = await teamAPI.getAllTeams()
        const teamData = response.data.data
        
        // TeamDetailResponse[]를 Team[]로 변환
        const convertedTeams: Team[] = teamData.slice(0, 6).map((team: any) => ({
          id: team.teamId,
          name: team.teamName,
          description: team.teamDescription || '',
          // 팀원들의 기술스택을 합쳐 팀 기술스택으로 사용
          tech: Array.from(new Set((team.members || []).flatMap((m: any) => mapTechStackArray(m.techStack as any)))) ,
          members: team.members.length,
          maxMembers: team.backendCount + team.frontendCount + team.aiCount + team.designCount + team.pmCount,
          deadline: '', // 마감일 정보가 없으므로 빈 문자열
          domain: team.teamDomain,
          // 팀 목표(선호도) 표시는 ProjectGoal 기준으로 매핑
          projectPreferences: mapProjectGoalArray(team.teamPreference as any) || [],
          leader: {
            name: team.leader.userName,
            avatar: '',
            role: team.leader.wantedPosition?.[0] || ''
          },
          roleDistribution: {
            backend: team.backendCount || 0,
            frontend: team.frontendCount || 0,
            ai: team.aiCount || 0,
            design: team.designCount || 0,
            pm: team.pmCount || 0
          },
          roleCurrent: {
            backend: team.members.filter((m: any) => m.wantedPosition?.includes('BACKEND')).length,
            frontend: team.members.filter((m: any) => m.wantedPosition?.includes('FRONTEND')).length,
            ai: team.members.filter((m: any) => m.wantedPosition?.includes('AI')).length,
            design: team.members.filter((m: any) => m.wantedPosition?.includes('DESIGN')).length,
            pm: team.members.filter((m: any) => m.wantedPosition?.includes('PM')).length
          }
        }))
        
        setTeams(convertedTeams)
      } catch (error) {
        console.error('팀 목록 로드 실패:', error)
        setTeams([]) // 오류 시 빈 배열
      }
    }

    loadTeams()
  }, []) // 컴포넌트 마운트 시 한 번만 호출

  // 대기중인 사용자 목록 로드
  useEffect(() => {
    const loadDevelopers = async () => {
      try {
        const response = await userAPI.getWaitingUsers()
        const userData = response.data.data
        
        // UserSearchResponse[]를 Developer[]로 변환 (처음 8명만)
        const convertedDevelopers: Developer[] = userData.slice(0, 8).map((user: any) => ({
          id: user.id,
          name: user.userName,
          role: user.wantedPosition?.[0] ? mapPositionArray(user.wantedPosition as any)[0] : '미정',
          positions: mapPositionArray(user.wantedPosition as any),
          // 카드 컴포넌트에서 기대하는 형태({ name, level })로 변환
          techStack: mapTechStackArray(user.techStack as any).map((name: string) => ({ name, level: 3 })),
          // 카드 하단 배지와 기본값(취업중심/학습열정)에 맞춰 ProjectGoal을 표시
          projectPreferences: mapProjectGoalArray(user.projectGoal as any),
          isMajor: user.major,
          avatar: '' // 아바타 정보가 없으므로 빈 문자열
        }))
        
        setDevelopers(convertedDevelopers)
      } catch (error) {
        console.error('대기중인 사용자 목록 로드 실패:', error)
        setDevelopers([]) // 오류 시 빈 배열
      }
    }

    loadDevelopers()
  }, []) // 컴포넌트 마운트 시 한 번만 호출, 매핑 함수는 안정적이므로 의존성에서 제거

  // 팀 상세보기 핸들러
  const handleViewTeam = async (teamId: number) => {
    try {
      console.log('팀 보기:', teamId)
      const teamDetail = await fetchTeamDetail(teamId)
      
      if (teamDetail) {
        // TeamDetailResponse를 Team 타입으로 변환
        const convertedTeam: Team = {
          id: teamDetail.teamId,
          name: teamDetail.teamName,
          description: teamDetail.teamDescription || '',
          // 상세에서도 팀원들의 기술스택을 집계
          tech: Array.from(new Set((teamDetail.members || []).flatMap((m: UserDetailResponse) => mapTechStackArray(m.techStack as any)))),
          members: teamDetail.members.length,
          maxMembers: teamDetail.backendCount + teamDetail.frontendCount + teamDetail.aiCount + teamDetail.designCount + teamDetail.pmCount,
          deadline: '', // 마감일 정보가 없으므로 빈 문자열
          roleDistribution: {
            backend: teamDetail.backendCount,
            frontend: teamDetail.frontendCount,
            ai: teamDetail.aiCount,
            design: teamDetail.designCount,
            pm: teamDetail.pmCount
          },
          roleCurrent: {
            backend: teamDetail.members.filter((m: UserDetailResponse) => m.wantedPosition?.includes('BACKEND')).length,
            frontend: teamDetail.members.filter((m: UserDetailResponse) => m.wantedPosition?.includes('FRONTEND')).length,
            ai: teamDetail.members.filter((m: UserDetailResponse) => m.wantedPosition?.includes('AI')).length,
            design: teamDetail.members.filter((m: UserDetailResponse) => m.wantedPosition?.includes('DESIGN')).length,
            pm: teamDetail.members.filter((m: UserDetailResponse) => m.wantedPosition?.includes('PM')).length
          },
          leader: {
            name: teamDetail.leader.userName,
            avatar: '',
            role: teamDetail.leader.wantedPosition?.[0] || ''
          },
          domains: [teamDetail.teamDomain],
          teamAtmosphere: teamDetail.teamVive || [],
          projectPreferences: mapProjectGoalArray(teamDetail.teamPreference as any) || [],
          introduction: teamDetail.teamDescription || ''
        }
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
          teams={teams}
          onCreateTeam={() => navigate('/make-team') }
          onViewAll={() => setIsTeamsModalOpen(true)}
          onViewTeam={handleViewTeam}
        />

        {/* Developer Section */}
        <DeveloperSection 
          developers={developers}
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

      {/* 대기자 전체보기 모달 */}
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