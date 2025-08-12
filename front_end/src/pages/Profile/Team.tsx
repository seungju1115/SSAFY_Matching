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
  // Team/index.tsx와 동일한 구조의 팀 데이터
  const mockTeamInfo = {
    teamName: 'AI 스마트 솔루션팀',
    teamDomain: 'AI/머신러닝',
    teamDescription: '혁신적인 AI 기술로 실생활 문제를 해결하는 서비스를 개발합니다.',
    teamPreference: ['JOB', 'PROFESSIONAL', 'QUALITY'] as ProjectGoalEnum[],
    teamVibe: ['CASUAL', 'COMFY', 'DEMOCRACY', 'AGILE'] as ProjectViveEnum[],
    roleDistribution: {
      backend: 2,
      frontend: 2, 
      ai: 1,
      pm: 1,
      design: 1
    }
  }

  // Team/index.tsx와 동일한 구조의 팀원 데이터
  const mockTeamMembers = [
    { id: 1, name: '김개발', role: 'backend', isLeader: true, avatar: '' },
    { id: 2, name: '박디자인', role: 'design', isLeader: false, avatar: '' },
    { id: 3, name: '이기획', role: 'pm', isLeader: false, avatar: '' },
    { id: 4, name: '최프론트', role: 'frontend', isLeader: false, avatar: '' },
    { id: 5, name: '정AI', role: 'ai', isLeader: false, avatar: '' }
  ]

  const hasTeam = true // 실제로는 API 응답에 따라 결정

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
            <Button>팀 찾기</Button>
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
                {mockTeamInfo.teamName}
              </CardTitle>
              <Badge variant="outline" className="mb-3 text-xs">
                {mockTeamInfo.teamDomain}
              </Badge>
              <p className="text-gray-600 text-sm leading-relaxed">
                {mockTeamInfo.teamDescription}
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
              {mockTeamInfo.teamPreference.map((pref) => (
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
              {mockTeamInfo.teamVibe.map((vibe) => (
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
              {Object.entries(mockTeamInfo.roleDistribution).map(([role, count]) => (
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
            팀원 목록 ({mockTeamMembers.length}명)
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {mockTeamMembers.map((member) => (
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
        <Button className="bg-blue-600 hover:bg-blue-700 text-white">
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
