import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Textarea } from '@/components/ui/textarea'
import { Separator } from '@/components/ui/separator'
import { 
  Users, 
  Target, 
  Heart, 
  Briefcase, 
  Code, 
  MessageSquare, 
  Plus, 
  Minus,
  Sparkles,
  Globe
} from 'lucide-react'

// 팀 생성 데이터 타입
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

export default function MakeTeam() {
  const navigate = useNavigate()
  const [teamData, setTeamData] = useState<TeamData>({
    domains: [],
    projectPreferences: [],
    teamAtmosphere: [],
    roleDistribution: {
      backend: 1,
      frontend: 1,
      ai: 0,
      design: 0,
      pm: 0
    },
    introduction: ''
  })

  // 도메인 선택지
  const domainSuggestions = [
    '웹기술', '웹디자인', 'AI/IoT', '모바일', '게임개발', 
    '블록체인', '핀테크', '헬스케어', '교육', '커머스',
    '소셜미디어', '데이터분석', '보안', '클라우드'
  ]

  // 프로젝트 성향 선택지
  const projectPreferenceSuggestions = [
    '취업우선', '수상목표', '개발경험우선', '포트폴리오중심',
    '학습중심', '혁신추구', '실무경험', '창업지향',
    '오픈소스', '사회공헌', '빠른개발', '완성도추구'
  ]

  // 팀 분위기 선택지
  const atmosphereSuggestions = [
    '반말 지향', '존대 지향', '편한 분위기', '체계적 분위기',
    '자유로운', '규칙적인', '소통 활발', '집중 중시',
    '유연한 시간', '정해진 시간', '온라인 중심', '오프라인 중심'
  ]

  // 태그 추가/제거 함수
  const toggleTag = (category: 'domains' | 'projectPreferences' | 'teamAtmosphere', tag: string) => {
    setTeamData(prev => ({
      ...prev,
      [category]: prev[category].includes(tag)
        ? prev[category].filter(t => t !== tag)
        : [...prev[category], tag]
    }))
  }

  // 역할 인원 조정
  const adjustRole = (role: keyof typeof teamData.roleDistribution, increment: boolean) => {
    setTeamData(prev => ({
      ...prev,
      roleDistribution: {
        ...prev.roleDistribution,
        [role]: Math.max(0, Math.min(10, prev.roleDistribution[role] + (increment ? 1 : -1)))
      }
    }))
  }

  // 팀 생성 처리
  const handleCreateTeam = () => {
    console.log('팀 생성 데이터:', teamData)
    // API 호출 로직 추가
    navigate('/')
  }

  // 전체 팀원 수 계산
  const totalMembers = Object.values(teamData.roleDistribution).reduce((sum, count) => sum + count, 0)

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 via-green-50 to-emerald-100 relative overflow-hidden">
      {/* Background decorative elements */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute -top-40 -right-40 w-80 h-80 bg-gradient-to-br from-green-200/30 to-emerald-200/30 rounded-full blur-3xl" />
        <div className="absolute -bottom-40 -left-40 w-80 h-80 bg-gradient-to-br from-blue-200/30 to-green-200/30 rounded-full blur-3xl" />
      </div>
      
      <div className="relative z-10 flex flex-col items-center justify-center min-h-screen p-4 sm:p-6">
        <div className="w-full max-w-4xl mx-auto">
          {/* Header */}
          <div className="text-center mb-12">
            <div className="inline-flex items-center justify-center w-20 h-20 bg-gradient-to-r from-green-500 to-emerald-600 rounded-2xl mb-6 shadow-lg">
              <Users className="w-10 h-10 text-white" />
            </div>
            <h1 className="text-5xl font-bold bg-gradient-to-r from-gray-900 via-green-800 to-emerald-800 bg-clip-text text-transparent mb-4">
              팀 만들기
            </h1>
            <p className="text-xl text-gray-600 max-w-md mx-auto leading-relaxed">
              함께할 팀원들을 위한 완벽한 팀을 만들어보세요 🚀
            </p>
          </div>

          <div className="space-y-8">
            {/* 도메인 선택 */}
            <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-emerald-50/50 backdrop-blur-sm">
              <CardHeader className="pb-4">
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-gradient-to-r from-emerald-500 to-green-600 rounded-lg">
                    <Globe className="w-5 h-5 text-white" />
                  </div>
                  <div>
                    <CardTitle className="text-2xl font-bold bg-gradient-to-r from-gray-800 to-emerald-700 bg-clip-text text-transparent">
                      도메인 선택
                    </CardTitle>
                    <p className="text-sm text-gray-500 mt-1">프로젝트 도메인을 선택해주세요</p>
                  </div>
                </div>
              </CardHeader>
              <Separator className="mx-6" />
              <CardContent className="pt-6">
                <div className="flex flex-wrap gap-2 mb-4">
                  {teamData.domains.map(domain => (
                    <Badge 
                      key={domain}
                      className="px-3 py-1.5 text-sm bg-emerald-100 text-emerald-800 hover:bg-emerald-200 cursor-pointer"
                      onClick={() => toggleTag('domains', domain)}
                    >
                      {domain} ×
                    </Badge>
                  ))}
                </div>
                <div className="flex flex-wrap gap-2">
                  {domainSuggestions.filter(domain => !teamData.domains.includes(domain)).map(domain => (
                    <Badge 
                      key={domain}
                      variant="outline"
                      className="px-3 py-1.5 text-sm cursor-pointer hover:bg-emerald-50"
                      onClick={() => toggleTag('domains', domain)}
                    >
                      {domain}
                    </Badge>
                  ))}
                </div>
              </CardContent>
            </Card>

            {/* 프로젝트 성향 */}
            <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-blue-50/50 backdrop-blur-sm">
              <CardHeader className="pb-4">
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-gradient-to-r from-blue-500 to-purple-600 rounded-lg">
                    <Target className="w-5 h-5 text-white" />
                  </div>
                  <div>
                    <CardTitle className="text-2xl font-bold bg-gradient-to-r from-gray-800 to-blue-700 bg-clip-text text-transparent">
                      프로젝트 성향
                    </CardTitle>
                    <p className="text-sm text-gray-500 mt-1">팀의 프로젝트 목표와 성향을 선택해주세요</p>
                  </div>
                </div>
              </CardHeader>
              <Separator className="mx-6" />
              <CardContent className="pt-6">
                <div className="flex flex-wrap gap-2 mb-4">
                  {teamData.projectPreferences.map(pref => (
                    <Badge 
                      key={pref}
                      className="px-3 py-1.5 text-sm bg-blue-100 text-blue-800 hover:bg-blue-200 cursor-pointer"
                      onClick={() => toggleTag('projectPreferences', pref)}
                    >
                      {pref} ×
                    </Badge>
                  ))}
                </div>
                <div className="flex flex-wrap gap-2">
                  {projectPreferenceSuggestions.filter(pref => !teamData.projectPreferences.includes(pref)).map(pref => (
                    <Badge 
                      key={pref}
                      variant="outline"
                      className="px-3 py-1.5 text-sm cursor-pointer hover:bg-blue-50"
                      onClick={() => toggleTag('projectPreferences', pref)}
                    >
                      {pref}
                    </Badge>
                  ))}
                </div>
              </CardContent>
            </Card>

            {/* 역할 비율 설정 */}
            <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-purple-50/50 backdrop-blur-sm">
              <CardHeader className="pb-4">
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-gradient-to-r from-purple-500 to-pink-600 rounded-lg">
                    <Briefcase className="w-5 h-5 text-white" />
                  </div>
                  <div>
                    <CardTitle className="text-2xl font-bold bg-gradient-to-r from-gray-800 to-purple-700 bg-clip-text text-transparent">
                      역할 비율 설정
                    </CardTitle>
                    <p className="text-sm text-gray-500 mt-1">필요한 역할별 인원 수를 설정해주세요 (총 {totalMembers}명)</p>
                  </div>
                </div>
              </CardHeader>
              <Separator className="mx-6" />
              <CardContent className="pt-6">
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
                  {[
                    { key: 'backend', label: '백엔드', icon: Code, color: 'bg-blue-100 text-blue-800' },
                    { key: 'frontend', label: '프론트엔드', icon: Globe, color: 'bg-green-100 text-green-800' },
                    { key: 'ai', label: 'AI', icon: Sparkles, color: 'bg-purple-100 text-purple-800' },
                    { key: 'design', label: '디자인', icon: Heart, color: 'bg-pink-100 text-pink-800' },
                    { key: 'pm', label: 'PM', icon: Users, color: 'bg-orange-100 text-orange-800' }
                  ].map(({ key, label, icon: Icon, color }) => (
                    <div key={key} className="flex items-center justify-between p-4 bg-white/50 rounded-lg border">
                      <div className="flex items-center space-x-3">
                        <div className={`p-2 rounded-lg ${color}`}>
                          <Icon className="w-4 h-4" />
                        </div>
                        <span className="font-medium">{label}</span>
                      </div>
                      <div className="flex items-center space-x-2">
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => adjustRole(key as keyof typeof teamData.roleDistribution, false)}
                          disabled={teamData.roleDistribution[key as keyof typeof teamData.roleDistribution] === 0}
                        >
                          <Minus className="w-3 h-3" />
                        </Button>
                        <span className="w-8 text-center font-semibold">
                          {teamData.roleDistribution[key as keyof typeof teamData.roleDistribution]}
                        </span>
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => adjustRole(key as keyof typeof teamData.roleDistribution, true)}
                          disabled={teamData.roleDistribution[key as keyof typeof teamData.roleDistribution] === 10}
                        >
                          <Plus className="w-3 h-3" />
                        </Button>
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>

            {/* 팀 분위기 */}
            <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-orange-50/50 backdrop-blur-sm">
              <CardHeader className="pb-4">
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-gradient-to-r from-orange-500 to-red-600 rounded-lg">
                    <Heart className="w-5 h-5 text-white" />
                  </div>
                  <div>
                    <CardTitle className="text-2xl font-bold bg-gradient-to-r from-gray-800 to-orange-700 bg-clip-text text-transparent">
                      팀 분위기
                    </CardTitle>
                    <p className="text-sm text-gray-500 mt-1">원하는 팀 분위기를 선택해주세요</p>
                  </div>
                </div>
              </CardHeader>
              <Separator className="mx-6" />
              <CardContent className="pt-6">
                <div className="flex flex-wrap gap-2 mb-4">
                  {teamData.teamAtmosphere.map(atmosphere => (
                    <Badge 
                      key={atmosphere}
                      className="px-3 py-1.5 text-sm bg-orange-100 text-orange-800 hover:bg-orange-200 cursor-pointer"
                      onClick={() => toggleTag('teamAtmosphere', atmosphere)}
                    >
                      {atmosphere} ×
                    </Badge>
                  ))}
                </div>
                <div className="flex flex-wrap gap-2">
                  {atmosphereSuggestions.filter(atmosphere => !teamData.teamAtmosphere.includes(atmosphere)).map(atmosphere => (
                    <Badge 
                      key={atmosphere}
                      variant="outline"
                      className="px-3 py-1.5 text-sm cursor-pointer hover:bg-orange-50"
                      onClick={() => toggleTag('teamAtmosphere', atmosphere)}
                    >
                      {atmosphere}
                    </Badge>
                  ))}
                </div>
              </CardContent>
            </Card>

            {/* 팀 한줄 소개 */}
            <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-teal-50/50 backdrop-blur-sm">
              <CardHeader className="pb-4">
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-gradient-to-r from-teal-500 to-cyan-600 rounded-lg">
                    <MessageSquare className="w-5 h-5 text-white" />
                  </div>
                  <div>
                    <CardTitle className="text-2xl font-bold bg-gradient-to-r from-gray-800 to-teal-700 bg-clip-text text-transparent">
                      팀 한줄 소개
                    </CardTitle>
                    <p className="text-sm text-gray-500 mt-1">팀을 소개하는 간단한 문구를 작성해주세요 (최대 100자)</p>
                  </div>
                </div>
              </CardHeader>
              <Separator className="mx-6" />
              <CardContent className="pt-6">
                <Textarea
                  placeholder="예: 함께 성장하며 혁신적인 서비스를 만들어갈 팀원을 찾습니다!"
                  value={teamData.introduction}
                  onChange={(e) => setTeamData(prev => ({ ...prev, introduction: e.target.value }))}
                  maxLength={100}
                  className="min-h-[100px] resize-none"
                />
                <div className="text-right text-sm text-gray-500 mt-2">
                  {teamData.introduction.length}/100
                </div>
              </CardContent>
            </Card>
          </div>

          {/* 팀 생성 버튼 */}
          <div className="flex justify-center mt-16">
            <Button
              onClick={handleCreateTeam}
              disabled={totalMembers === 0 || teamData.domains.length === 0 || !teamData.introduction.trim()}
              className="group w-64 h-16 text-lg font-semibold bg-gradient-to-r from-green-600 to-emerald-600 hover:from-green-700 hover:to-emerald-700 text-white shadow-lg hover:shadow-xl transition-all duration-300 transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:scale-100"
            >
              <Users className="h-6 w-6 mr-2" />
              팀 생성하기
            </Button>
          </div>
        </div>
      </div>
    </div>
  )
}
