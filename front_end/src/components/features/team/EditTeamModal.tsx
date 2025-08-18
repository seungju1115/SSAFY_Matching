import { useEffect, useMemo, useState } from 'react'
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter } from '@/components/ui/dialog'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Textarea } from '@/components/ui/textarea'
import { Separator } from '@/components/ui/separator'
import { Globe, Target, Heart, Briefcase, Code, MessageSquare, Plus, Minus } from 'lucide-react'
import type { TeamDetailResponse, ProjectGoalEnum, ProjectViveEnum, TeamRequest } from '@/types/team'
import { useTeam } from '@/hooks/useTeam'

interface EditTeamModalProps {
  isOpen: boolean
  onClose: () => void
  team: TeamDetailResponse
}

// 로컬 폼 상태 타입 (팀 생성 페이지와 동일 구조)
interface TeamData {
  domains: string[]
  projectPreferences: string[]
  teamAtmosphere: string[]
  roleDistribution: {
    backend: number
    frontend: number
    ai: number
    design: number
    pm: number
  }
  introduction: string
}

export default function EditTeamModal({ isOpen, onClose, team }: EditTeamModalProps) {
  const { updateTeam, isLoading } = useTeam()

  // 선택지 (팀 생성 페이지와 동일)
  const domainSuggestions = [
    '웹기술', '웹디자인', 'AI/IoT', '모바일', '게임개발',
    '블록체인', '핀테크', '헬스케어', '교육', '커머스',
    '소셜미디어', '데이터분석', '보안', '클라우드'
  ]

  const projectPreferenceSuggestions = [
    '취업우선', '수상목표', '포트폴리오중심', '학습중심', '아이디어실현', '실무경험', '빠른개발', '완성도추구'
  ]
 
  // UI -> Enum 매핑 (팀 생성 페이지와 동일)
  const projectPreferenceToEnumMapping: Record<string, ProjectGoalEnum> = {
    '취업우선': 'JOB',
    '수상목표': 'AWARD',
    '포트폴리오중심': 'PORTFOLIO',
    '학습중심': 'STUDY',
    '아이디어실현': 'IDEA',
    '실무경험': 'PROFESSIONAL',
    '빠른개발': 'QUICK',
    '완성도추구': 'QUALITY',
  }

  const atmosphereToEnumMapping: Record<string, ProjectViveEnum> = {
    '반말 지향': 'CASUAL',
    '존대 지향': 'FORMAL',
    '편한 분위기': 'COMFY',
    '규칙적인 분위기': 'RULE',
    '리더 중심': 'LEADER',
    '합의 중심': 'DEMOCRACY',
    '새로운 주제': 'BRANDNEW',
    '안정적인 주제': 'STABLE',
    '애자일 방식': 'AGILE',
    '워터폴 방식': 'WATERFALL',
  }

  // Enum -> UI 역매핑 (초기값 셋업용)
  const enumToProjectPreferenceLabel: Record<ProjectGoalEnum, string> = useMemo(() => {
    const entries = Object.entries(projectPreferenceToEnumMapping) as [string, ProjectGoalEnum][]
    return entries.reduce((acc, [label, en]) => { acc[en] = label; return acc }, {} as Record<ProjectGoalEnum, string>)
  }, [])

  const enumToAtmosphereLabel: Record<ProjectViveEnum, string> = useMemo(() => {
    const entries = Object.entries(atmosphereToEnumMapping) as [string, ProjectViveEnum][]
    return entries.reduce((acc, [label, en]) => { acc[en] = label; return acc }, {} as Record<ProjectViveEnum, string>)
  }, [])

  const [teamData, setTeamData] = useState<TeamData>({
    domains: [],
    projectPreferences: [],
    teamAtmosphere: [],
    roleDistribution: { backend: 0, frontend: 0, ai: 0, design: 0, pm: 0 },
    introduction: ''
  })

  useEffect(() => {
    if (!isOpen || !team) return
    // team.teamDomain 은 문자열(예: '웹기술, 교육') → 배열로 변환
    const domains = team.teamDomain ? team.teamDomain.split(',').map(s => s.trim()).filter(Boolean) : []

    setTeamData({
      domains,
      projectPreferences: (team.teamPreference || []).map(pref => enumToProjectPreferenceLabel[pref]).filter(Boolean),
      teamAtmosphere: (team.teamVive || []).map(v => enumToAtmosphereLabel[v]).filter(Boolean),
      roleDistribution: {
        backend: team.backendCount || 0,
        frontend: team.frontendCount || 0,
        ai: team.aiCount || 0,
        design: team.designCount || 0,
        pm: team.pmCount || 0,
      },
      introduction: team.teamDescription || ''
    })
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isOpen, team?.teamId])

  const toggleTag = (category: 'domains' | 'projectPreferences' | 'teamAtmosphere', tag: string) => {
    setTeamData(prev => ({
      ...prev,
      [category]: prev[category].includes(tag)
        ? prev[category].filter(t => t !== tag)
        : [...prev[category], tag]
    }))
  }

  const adjustRole = (role: keyof TeamData['roleDistribution'], increment: boolean) => {
    setTeamData(prev => ({
      ...prev,
      roleDistribution: {
        ...prev.roleDistribution,
        [role]: Math.max(0, Math.min(10, prev.roleDistribution[role] + (increment ? 1 : -1)))
      }
    }))
  }

  const totalMembers = useMemo(() => Object.values(teamData.roleDistribution).reduce((s, v) => s + v, 0), [teamData.roleDistribution])

  // TeamData를 TeamRequest로 변환
  const mapTeamDataToRequest = (data: TeamData): TeamRequest => {
    // enum으로 매핑
    const mappedPreferences = data.projectPreferences
      .map(pref => projectPreferenceToEnumMapping[pref])
      .filter(Boolean)
    
    const mappedAtmosphere = data.teamAtmosphere
      .map(atm => atmosphereToEnumMapping[atm])
      .filter(Boolean)

    return {
      teamId: team.teamId,
      leaderId: team.leader.id,
      teamDomain: data.domains.join(', '), // 도메인들을 문자열로 결합
      teamDescription: data.introduction,
      teamPreference: mappedPreferences,
      teamVive: mappedAtmosphere,
      backendCount: data.roleDistribution.backend,
      frontendCount: data.roleDistribution.frontend,
      aiCount: data.roleDistribution.ai,
      pmCount: data.roleDistribution.pm,
      designCount: data.roleDistribution.design
    }
  }

  const handleSave = async () => {
    try {
      const teamRequest = mapTeamDataToRequest(teamData)
      console.log('팀 수정 요청 데이터:', teamRequest)

      await updateTeam(teamRequest)
      console.log('팀 수정 완료')

      onClose()
    } catch (error) {
      console.error('팀 수정 실패:', error)
    }
  }

  return (
    <Dialog open={isOpen} onOpenChange={(open) => { if (!open && !isLoading) onClose() }}>
      <DialogContent className="w-[calc(100vw-2rem)] sm:max-w-xl md:max-w-2xl lg:max-w-3xl p-0 max-h-[85vh] overflow-y-auto">
        <DialogHeader className="px-6 pt-6">
          <DialogTitle className="text-2xl">팀 정보 수정</DialogTitle>
          <DialogDescription>팀 생성 페이지와 동일한 항목으로 편집합니다.</DialogDescription>
        </DialogHeader>

        <div className="px-6 pb-2 space-y-6">
          {/* 도메인 선택 */}
          <Card className="border-0 bg-gradient-to-br from-white to-emerald-50/40">
            <CardHeader className="pb-3">
              <div className="flex items-center gap-3">
                <div className="p-2 bg-gradient-to-r from-emerald-500 to-green-600 rounded-lg">
                  <Globe className="w-4 h-4 text-white" />
                </div>
                <CardTitle className="text-lg">도메인 선택</CardTitle>
              </div>
            </CardHeader>
            <Separator className="mx-6" />
            <CardContent className="pt-4">
              <div className="flex flex-wrap gap-2 mb-3">
                {teamData.domains.map(domain => (
                  <Badge key={domain} className="px-2.5 py-1 text-xs bg-emerald-100 text-emerald-800 cursor-pointer" onClick={() => toggleTag('domains', domain)}>
                    {domain} ×
                  </Badge>
                ))}
              </div>
              <div className="flex flex-wrap gap-2">
                {domainSuggestions.filter(d => !teamData.domains.includes(d)).map(domain => (
                  <Badge key={domain} variant="outline" className="px-2.5 py-1 text-xs cursor-pointer hover:bg-emerald-50" onClick={() => toggleTag('domains', domain)}>
                    {domain}
                  </Badge>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* 프로젝트 성향 */}
          <Card className="border-0 bg-gradient-to-br from-white to-blue-50/40">
            <CardHeader className="pb-3">
              <div className="flex items-center gap-3">
                <div className="p-2 bg-gradient-to-r from-blue-500 to-purple-600 rounded-lg">
                  <Target className="w-4 h-4 text-white" />
                </div>
                <CardTitle className="text-lg">프로젝트 성향</CardTitle>
              </div>
            </CardHeader>
            <Separator className="mx-6" />
            <CardContent className="pt-4">
              <div className="flex flex-wrap gap-2 mb-3">
                {teamData.projectPreferences.map(pref => (
                  <Badge key={pref} className="px-2.5 py-1 text-xs bg-blue-100 text-blue-800 cursor-pointer" onClick={() => toggleTag('projectPreferences', pref)}>
                    {pref} ×
                  </Badge>
                ))}
              </div>
              <div className="flex flex-wrap gap-2">
                {projectPreferenceSuggestions.filter(p => !teamData.projectPreferences.includes(p)).map(pref => (
                  <Badge key={pref} variant="outline" className="px-2.5 py-1 text-xs cursor-pointer hover:bg-blue-50" onClick={() => toggleTag('projectPreferences', pref)}>
                    {pref}
                  </Badge>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* 역할 비율 */}
          <Card className="border-0 bg-gradient-to-br from-white to-purple-50/40">
            <CardHeader className="pb-3">
              <div className="flex items-center gap-3">
                <div className="p-2 bg-gradient-to-r from-purple-500 to-pink-600 rounded-lg">
                  <Briefcase className="w-4 h-4 text-white" />
                </div>
                <CardTitle className="text-lg">역할 비율 설정 (총 {totalMembers}명)</CardTitle>
              </div>
            </CardHeader>
            <Separator className="mx-6" />
            <CardContent className="pt-4">
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                {[
                  { key: 'backend', label: '백엔드', icon: Code },
                  { key: 'frontend', label: '프론트엔드', icon: Globe },
                  { key: 'ai', label: 'AI', icon: MessageSquare },
                  { key: 'design', label: '디자인', icon: Heart },
                  { key: 'pm', label: 'PM', icon: Briefcase },
                ].map(({ key, label, icon: Icon }) => (
                  <div key={key} className="flex items-center justify-between p-3 bg-white/60 rounded-lg border">
                    <div className="flex items-center gap-2">
                      <div className="p-2 rounded-lg bg-gray-100">
                        <Icon className="w-4 h-4" />
                      </div>
                      <span className="text-sm font-medium">{label}</span>
                    </div>
                    <div className="flex items-center gap-2">
                      <Button variant="outline" size="sm" onClick={() => adjustRole(key as keyof TeamData['roleDistribution'], false)} disabled={teamData.roleDistribution[key as keyof TeamData['roleDistribution']] === 0}>
                        <Minus className="w-3 h-3" />
                      </Button>
                      <span className="w-8 text-center font-semibold text-sm">{teamData.roleDistribution[key as keyof TeamData['roleDistribution']]}</span>
                      <Button variant="outline" size="sm" onClick={() => adjustRole(key as keyof TeamData['roleDistribution'], true)} disabled={teamData.roleDistribution[key as keyof TeamData['roleDistribution']] === 10}>
                        <Plus className="w-3 h-3" />
                      </Button>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* 팀 한줄 소개 */}
          <Card className="border-0 bg-gradient-to-br from-white to-teal-50/40">
            <CardHeader className="pb-3">
              <div className="flex items-center gap-3">
                <div className="p-2 bg-gradient-to-r from-teal-500 to-cyan-600 rounded-lg">
                  <MessageSquare className="w-4 h-4 text-white" />
                </div>
                <CardTitle className="text-lg">팀 한줄 소개</CardTitle>
              </div>
            </CardHeader>
            <Separator className="mx-6" />
            <CardContent className="pt-4">
              <Textarea
                placeholder="예: 함께 성장하며 혁신적인 서비스를 만들어갈 팀원을 찾습니다!"
                value={teamData.introduction}
                onChange={(e) => setTeamData(prev => ({ ...prev, introduction: e.target.value }))}
                maxLength={100}
                className="min-h-[100px] resize-none"
              />
              <div className="text-right text-xs text-gray-500 mt-2">{teamData.introduction.length}/100</div>
            </CardContent>
          </Card>
        </div>

        <DialogFooter className="px-6 py-4 flex gap-2">
          <Button onClick={handleSave} className="bg-blue-600 hover:bg-blue-700 text-white">수정</Button>
          <Button variant="outline" onClick={onClose}>닫기</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
