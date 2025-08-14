import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Bell } from 'lucide-react'
import { toast } from '@/hooks/use-toast'
import Header from '@/components/layout/Header'
import InvitationList from '@/components/features/invitation/InvitationList'
import type { Team } from '@/components/features/home/TeamSection'

// 임시 데이터 (실제로는 API에서 가져와야 함)
const mockInvitations: Team[] = [
  {
    id: 1,
    name: "혁신적인 웹 서비스 개발팀",
    description: "사용자 중심의 혁신적인 웹 서비스를 개발하는 팀입니다.",
    tech: ["React", "Node.js", "TypeScript"],
    members: 3,
    maxMembers: 6,
    deadline: "2025-09-15",
    leader: {
      name: "김팀장",
      avatar: "",
      role: "팀장"
    },
    domain: "웹 개발",
    projectPreferences: ["포트폴리오", "실무경험"],
    roleDistribution: {
      backend: 2,
      frontend: 2,
      ai: 0,
      design: 1,
      pm: 1
    },
    roleCurrent: {
      backend: 1,
      frontend: 1,
      ai: 0,
      design: 1,
      pm: 0
    }
  },
  {
    id: 2,
    name: "AI 기반 추천 시스템 개발팀",
    description: "머신러닝을 활용한 개인화 추천 시스템을 구축하는 팀입니다.",
    tech: ["Python", "TensorFlow", "FastAPI"],
    members: 2,
    maxMembers: 5,
    deadline: "2025-10-01",
    leader: {
      name: "박개발",
      avatar: "",
      role: "팀장"
    },
    domain: "AI/ML",
    projectPreferences: ["기술 도전", "포트폴리오"],
    roleDistribution: {
      backend: 2,
      frontend: 1,
      ai: 1,
      design: 1,
      pm: 0
    },
    roleCurrent: {
      backend: 1,
      frontend: 0,
      ai: 1,
      design: 0,
      pm: 0
    }
  },
  {
    id: 3,
    name: "모바일 게임 개발팀",
    description: "재미있고 중독성 있는 모바일 게임을 개발하는 팀입니다.",
    tech: ["Unity", "C#", "Firebase"],
    members: 4,
    maxMembers: 6,
    deadline: "2025-08-30",
    leader: {
      name: "이게임",
      avatar: "",
      role: "팀장"
    },
    domain: "게임 개발",
    projectPreferences: ["창의적", "재미"],
    roleDistribution: {
      backend: 1,
      frontend: 2,
      ai: 0,
      design: 2,
      pm: 1
    },
    roleCurrent: {
      backend: 1,
      frontend: 2,
      ai: 0,
      design: 1,
      pm: 0
    }
  }
]

export default function TeamInvitation() {
  const navigate = useNavigate()
  const [isLoading, setIsLoading] = useState(false)
  const invitations = mockInvitations

  const handleAccept = async (teamId: number) => {
    setIsLoading(true)
    try {
      // API 호출 로직
      await new Promise(resolve => setTimeout(resolve, 1000)) // 임시 딜레이
      
      const team = invitations.find(inv => inv.id === teamId)
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
      // API 호출 로직
      await new Promise(resolve => setTimeout(resolve, 1000)) // 임시 딜레이
      
      const team = invitations.find(inv => inv.id === teamId)
      toast({
        title: "초대 거절 완료",
        description: `${team?.leader.name ?? '해당'} 팀 초대를 거절했습니다.`,
      })
      
      // 실제로는 목록에서 해당 초대를 제거하거나 상태를 업데이트
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
    // 현재 공통 사용 경로인 '/team'으로 이동. 팀 상세 라우팅이 준비되면 `/team/${teamId}`로 확장 가능
    navigate('/team', { state: { teamId } })
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
