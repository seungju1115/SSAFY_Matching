import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Bell } from 'lucide-react'
import { toast } from '@/hooks/use-toast'
import { teamAPI } from '@/api/team'
import useUserStore from '@/stores/userStore'
import Header from '@/components/layout/Header'
import InvitationList from '@/components/features/invitation/InvitationList'
import type { Team } from '@/components/features/home/TeamSection'
import type { TeamMembershipResponse, TeamDetailResponse, TeamInviteRequest } from '@/types/team'
import TeamDetailModal from '@/components/features/home/TeamDetailModal'

export default function TeamInvitation() {
  const navigate = useNavigate()
  const { user } = useUserStore()
  const [isLoading, setIsLoading] = useState(false)
  const [, setMembershipResponses] = useState<TeamMembershipResponse[]>([])
  const [invitations, setInvitations] = useState<Team[]>([])
  const [isDetailOpen, setIsDetailOpen] = useState(false)
  const [selectedTeam, setSelectedTeam] = useState<Team | null>(null)

  // TeamMembershipResponse를 Team 타입으로 변환하는 함수
  const convertMembershipToTeam = async (membership: TeamMembershipResponse): Promise<Team | null> => {
    try {
      const teamResponse = await teamAPI.getTeamDetail(membership.teamId)
      const teamDetail: TeamDetailResponse = teamResponse.data.data
      
      // TeamDetailResponse를 Team 타입으로 변환
      const team: Team = {
        id: teamDetail.teamId,
        name: teamDetail.teamName,
        description: teamDetail.teamDescription || '',
        introduction: teamDetail.teamDescription,
        tech: [], // TeamDetailResponse에는 기술 스택 정보가 없으므로 빈 배열
        members: teamDetail.members.length + 1, // 리더 + 멤버들
        maxMembers: teamDetail.backendCount + teamDetail.frontendCount + teamDetail.aiCount + teamDetail.pmCount + teamDetail.designCount,
        deadline: '', // TeamDetailResponse에는 마감일 정보가 없음
        leader: {
          name: teamDetail.leader.userName || '',
          avatar: '',
          role: teamDetail.leader.wantedPosition?.[0] || 'UNKNOWN'
        },
        domains: teamDetail.teamDomain ? teamDetail.teamDomain.split(',').map(d => d.trim()) : [],
        domain: teamDetail.teamDomain,
        roleDistribution: {
          backend: teamDetail.backendCount,
          frontend: teamDetail.frontendCount,
          ai: teamDetail.aiCount,
          design: teamDetail.designCount,
          pm: teamDetail.pmCount
        }
      }
      
      return team
    } catch (error) {
      console.error(`팀 ${membership.teamId} 정보 조회 실패:`, error)
      return null
    }
  }

  // 사용자의 팀 초대 요청 목록 가져오기
  useEffect(() => {
    const fetchInvitations = async () => {
      if (!user.id) {
        console.log('user.id가 없습니다:', user)
        return
      }

      console.log('사용자 ID로 초대 목록 조회:', user.id)

      setIsLoading(true)
      try {
        const response = await teamAPI.getUserRequests(user.id)
        // INVITE 타입이고 PENDING 상태인 요청들만 필터링
        const inviteRequests = response.data.data.filter(
          request => request.requestType === 'INVITE' && request.status === 'PENDING'
        )
        
        setMembershipResponses(inviteRequests)
        
        // 각 멤버십 응답을 Team 타입으로 변환
        const teamPromises = inviteRequests.map(convertMembershipToTeam)
        const teams = await Promise.all(teamPromises)
        const validTeams = teams.filter((team): team is Team => team !== null)
        
        setInvitations(validTeams)
      } catch (error) {
        console.error('초대 목록 로딩 실패:', error)
        toast({
          title: "오류 발생",
          description: "초대 목록을 불러오는 중 문제가 발생했습니다.",
          variant: "destructive"
        })
      } finally {
        setIsLoading(false)
      }
    }

    fetchInvitations()
  }, [user.id])

  const handleAccept = async (teamId: number) => {
    if (!user.id) {
      toast({
        title: "오류",
        description: "사용자 정보가 없어 초대를 수락할 수 없습니다.",
        variant: "destructive",
      })
      return
    }

    setIsLoading(true)
    try {
      const inviteRequest: TeamInviteRequest = {
        teamId: teamId,
        userId: user.id,
      }
      await teamAPI.inviteMemberToTeam(inviteRequest)
      
      const team = invitations.find(inv => inv.id === teamId)
      // 목록에서 제거
      setInvitations(prev => prev.filter(inv => inv.id !== teamId))
      setMembershipResponses((prev: TeamMembershipResponse[]) => prev.filter((resp: TeamMembershipResponse) => resp.teamId !== teamId))
      // 상세 모달이 해당 팀을 보고 있으면 닫기
      if (selectedTeam?.id === teamId) {
        setIsDetailOpen(false)
      }
      toast({
        title: "초대 수락 완료",
        description: `${team?.leader.name} 팀에 성공적으로 참여했습니다.`,
      })
      
      navigate('/team') // 팀 페이지로 이동
    } catch (error) {
      toast({
        title: "오류 발생",
        description: "초대 수락 중 문제가 발생했습니다. 다시 시도해주세요.",
        variant: "destructive"
      })
    } finally {
      setIsLoading(false)
    }
  }

  const handleReject = async (teamId: number) => {
    setIsLoading(true)
    try {
      await teamAPI.rejectOffer(teamId)
      
      const team = invitations.find(inv => inv.id === teamId)
      toast({
        title: "초대 거절 완료",
        description: `${team?.leader.name ?? '해당'} 팀 초대를 거절했습니다.`,
      })
      
      // 목록에서 제거
      setInvitations(prev => prev.filter(inv => inv.id !== teamId))
      setMembershipResponses((prev: TeamMembershipResponse[]) => prev.filter((resp: TeamMembershipResponse) => resp.teamId !== teamId))
      // 상세 모달이 해당 팀을 보고 있으면 닫기
      if (selectedTeam?.id === teamId) {
        setIsDetailOpen(false)
      }
    } catch (error) {
      toast({
        title: "오류 발생",
        description: "초대 거절 중 문제가 발생했습니다. 다시 시도해주세요.",
        variant: "destructive"
      })
    } finally {
      setIsLoading(false)
    }
  }

  const handleViewTeam = (teamId: number) => {
    // 상세 모달 열기
    const team = invitations.find(inv => inv.id === teamId) || null
    setSelectedTeam(team)
    setIsDetailOpen(true)
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      {/* 헤더 */}
      <Header />

      {/* 메인 컨텐츠 */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* 페이지 헤더 */}
        <div className="mb-8">
          <div className="flex items-center gap-3 mb-4">
            <Bell className="h-6 w-6 text-blue-600" />
            <h1 className="text-2xl sm:text-3xl font-bold text-gray-900">팀 초대</h1>
          </div>
          <p className="text-gray-600">
            받은 팀 초대를 확인하고 참여 여부를 결정하세요.
          </p>
        </div>

        {/* 초대 목록 */}
        <InvitationList
          invitations={invitations}
          onAccept={handleAccept}
          onReject={handleReject}
          isLoading={isLoading}
          onViewTeam={handleViewTeam}
          onNavigateToTeams={() => navigate('/teams')}
          onNavigateToMakeTeam={() => navigate('/make-team')}
        />
        
        <TeamDetailModal
          isOpen={isDetailOpen}
          onClose={() => setIsDetailOpen(false)}
          team={selectedTeam}
          onJoinRequest={(teamId) => handleAccept(teamId)}
        />
        
        {/* 하단 안내 */}
        <div className="mt-8 text-center">
          <p className="text-sm text-gray-500">
            초대를 수락하면 해당 팀의 멤버가 되어 프로젝트에 참여할 수 있습니다.
          </p>
        </div>
      </div>
    </div>
  )
}
