import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Avatar, AvatarFallback } from '@/components/ui/avatar'
import { Separator } from '@/components/ui/separator'
import { 
  Users, 
  ExternalLink,
  Crown,
  Target,
  Heart
} from 'lucide-react'
import { cn } from '@/lib/utils'
import type { ProjectGoalEnum, ProjectViveEnum } from '@/types/team'
import useUserStore from '@/stores/userStore'
import { useTeamStore } from '@/stores/teamStore'
import { useNavigate } from 'react-router-dom'
import { useTeam } from '@/hooks/useTeam'
import { useEffect } from 'react'

// 프로젝트 성향 라벨 매핑
const projectGoalLabels: Record<ProjectGoalEnum, string> = {
  JOB: '취업우선',
  AWARD: '수상목표', 
  PORTFOLIO: '포트폴리오중심',
  STUDY: '학습중심',
  IDEA: '아이디어실현',
  PROFESSIONAL: '실무경험',
  QUICK: '빠른개발',
  QUALITY: '완성도추구'
}

// 팀 분위기 라벨 매핑
const projectVibeLabels: Record<ProjectViveEnum, string> = {
  CASUAL: '반말 지향',
  FORMAL: '존대 지향',
  COMFY: '편한 분위기',
  RULE: '규칙적인 분위기',
  LEADER: '리더 중심',
  DEMOCRACY: '합의 중심',
  BRANDNEW: '새로운 주제',
  STABLE: '안정적인 주제',
  AGILE: '애자일 방식',
  WATERFALL: '워터폴 방식'
}

// 역할별 색상 매핑
const roleColors = {
  backend: 'bg-slate-100 text-slate-700 border-slate-200',
  frontend: 'bg-emerald-100 text-emerald-700 border-emerald-200',
  ai: 'bg-violet-100 text-violet-700 border-violet-200',
  pm: 'bg-amber-100 text-amber-700 border-amber-200',
  design: 'bg-rose-100 text-rose-700 border-rose-200'
}

// 팀 정보 페이지
export default function ProfileTeam() {
  const navigate = useNavigate()
  const { user } = useUserStore()
  const { getTeamDetailById } = useTeamStore()
  const { fetchTeamDetail, isLoading, invalidateTeamCache } = useTeam()
  
  // userStore에서 teamId 가져와서 팀 정보 조회
  const teamId = user.teamId
  const teamDetail = teamId ? getTeamDetailById(teamId) : null
  const hasTeam = !!teamDetail

  // 페이지 진입시 팀 정보 API 호출하여 팀스토어에 저장
  useEffect(() => {
    if (teamId) {
      // 캐시 무효화 후 새로운 데이터 가져오기
      invalidateTeamCache(teamId)
      fetchTeamDetail(teamId)
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [teamId])

  // teamStore 데이터를 UI에 맞게 변환
  const teamInfo = teamDetail ? {
    teamName: teamDetail.teamName,
    teamDomain: teamDetail.teamDomain,
    teamDescription: teamDetail.teamDescription || '팀 소개가 없습니다.',
    teamPreference: teamDetail.teamPreference || [],
    teamVibe: teamDetail.teamVive || [],
    roleDistribution: {
      backend: teamDetail.backendCount,
      frontend: teamDetail.frontendCount,
      ai: teamDetail.aiCount,
      pm: teamDetail.pmCount,
      design: teamDetail.designCount
    }
  } : null

  // 팀원 데이터 (리더 + 멤버들, 단 일반 멤버에서는 리더 제외)
  const teamMembers = teamDetail ? [
    // 팀장 먼저
    {
      id: teamDetail.leader.id,
      name: teamDetail.leader.userName,
      role: (() => {
        console.log('🔍 Leader Debug:')
        console.log('teamDetail.leader:', teamDetail.leader)
        console.log('wantedPosition:', teamDetail.leader.wantedPosition)
        console.log('wantedPosition[0]:', teamDetail.leader.wantedPosition?.[0])
        const role = teamDetail.leader.wantedPosition?.[0]?.toLowerCase() || 'leader'
        console.log('final role:', role)
        return role
      })(),
      isLeader: true,
      avatar: ''
    },
    // 일반 멤버들 (리더 제외)
    ...teamDetail.members
      .filter(member => member.id !== teamDetail.leader.id)
      .map(member => ({
        id: member.id,
        name: member.userName,
        role: member.wantedPosition?.[0]?.toLowerCase() || 'member',
        isLeader: false,
        avatar: ''
      }))
  ] : []

  // 로딩 상태 처리
  if (isLoading) {
    return (
      <div className="space-y-6">
        <div>
          <h2 className="text-2xl font-bold text-gray-900 mb-2">내 팀 정보</h2>
          <p className="text-gray-600">팀 정보를 불러오는 중...</p>
        </div>
        <Card>
          <CardContent className="p-8 text-center">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
          </CardContent>
        </Card>
      </div>
    )
  }

  if (!hasTeam) {
    return (
      <div className="space-y-6">
        <div>
          <h2 className="text-2xl font-bold text-gray-900 mb-2">내 팀 정보</h2>
          <p className="text-gray-600">현재 참여 중인 팀이 없습니다.</p>
        </div>
        
        <Card>
          <CardContent className="p-8 text-center">
            <Users className="h-12 w-12 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">팀에 참여해보세요</h3>
            <p className="text-gray-600 mb-4">
              다른 사람들과 함께 학습하고 프로젝트를 진행할 수 있습니다.
            </p>
            <Button onClick={() => navigate('/matching')}>팀 찾기</Button>
          </CardContent>
        </Card>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-gray-900 mb-2">내 팀 정보</h2>
        <p className="text-gray-600">현재 참여 중인 팀의 정보를 확인할 수 있습니다.</p>
      </div>

      {/* 팀 기본 정보 카드 */}
      <Card className="bg-white border border-gray-200 shadow-sm">
        <CardHeader>
          <div className="flex items-start justify-between">
            <div className="flex-1">
              <CardTitle className="text-xl font-semibold text-gray-900 mb-2">
                {teamInfo?.teamName || '팀 이름을 정해주세요 !'}
              </CardTitle>
              <Badge variant="outline" className="mb-3 text-xs">
                {teamInfo?.teamDomain}
              </Badge>
              <p className="text-gray-600 text-sm leading-relaxed">
                {teamInfo?.teamDescription}
              </p>
            </div>
          </div>
        </CardHeader>
        <CardContent className="space-y-6">
          {/* 팀 성향 */}
          <div>
            <div className="flex items-center gap-2 mb-3">
              <Target className="w-4 h-4 text-blue-600" />
              <h3 className="text-sm font-medium text-gray-900">프로젝트 성향</h3>
            </div>
            <div className="flex flex-wrap gap-2">
              {teamInfo?.teamPreference.map((pref) => (
                <Badge key={pref} variant="secondary" className="text-xs">
                  {projectGoalLabels[pref]}
                </Badge>
              ))}
            </div>
          </div>

          <Separator />

          {/* 팀 분위기 */}
          <div>
            <div className="flex items-center gap-2 mb-3">
              <Heart className="w-4 h-4 text-pink-600" />
              <h3 className="text-sm font-medium text-gray-900">팀 분위기</h3>
            </div>
            <div className="flex flex-wrap gap-2">
              {teamInfo?.teamVibe.map((vibe) => (
                <Badge key={vibe} variant="outline" className="text-xs">
                  {projectVibeLabels[vibe]}
                </Badge>
              ))}
            </div>
          </div>

          <Separator />

          {/* 역할 분배 */}
          <div>
            <div className="flex items-center gap-2 mb-3">
              <Users className="w-4 h-4 text-green-600" />
              <h3 className="text-sm font-medium text-gray-900">역할 분배</h3>
            </div>
            <div className="flex flex-wrap gap-2">
              {Object.entries(teamInfo?.roleDistribution || {})
                .filter(([, count]) => count > 0)
                .map(([role, count]) => (
                  <Badge key={role} variant="outline" className="text-xs">
                    {role.toUpperCase()} {count}명
                  </Badge>
                ))}
            </div>
          </div>
        </CardContent>
      </Card>

      {/* 팀원 목록 카드 */}
      <Card className="bg-white border border-gray-200 shadow-sm">
        <CardHeader>
          <CardTitle className="text-lg font-medium text-gray-900 flex items-center gap-2">
            <Users className="w-5 h-5" />
            팀원 목록 ({teamMembers.length}명)
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {teamMembers.map((member) => (
              <div key={member.id} className="flex items-center gap-3 p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors">
                <Avatar className="w-10 h-10">
                  <AvatarFallback className="bg-gray-200 text-gray-600 text-sm">
                    {member.name.charAt(0)}
                  </AvatarFallback>
                </Avatar>
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2">
                    <span className="text-sm font-medium text-gray-900 truncate">
                      {member.name}
                    </span>
                    {member.isLeader && (
                      <Crown className="w-3 h-3 text-amber-500 flex-shrink-0" />
                    )}
                  </div>
                  <Badge 
                    variant="outline"
                    className={cn("text-xs mt-1", roleColors[member.role as keyof typeof roleColors])}
                  >
                    {member.role.toUpperCase()}
                  </Badge>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      {/* 액션 버튼들 */}
      <div className="flex flex-wrap gap-3">
        <Button 
          className="bg-blue-600 hover:bg-blue-700 text-white"
          onClick={() => navigate('/team')}
        >
          <ExternalLink className="h-4 w-4 mr-2" />
          팀 페이지 보기
        </Button>
        <Button variant="outline">팀 설정</Button>
        <Button variant="outline" className="text-red-600 hover:text-red-700 hover:bg-red-50">
          팀 탈퇴
        </Button>
      </div>
    </div>
  )
}
