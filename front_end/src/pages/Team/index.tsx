import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useTeamStore } from '@/store/teamStore'
import useUserStore from '@/stores/userStore'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { ScrollArea } from '@/components/ui/scroll-area'
import { Input } from '@/components/ui/input'
import { Separator } from '@/components/ui/separator'
import Header from '@/components/layout/Header'
import { 
  Crown, 
  MessageCircle, 
  Send, 
  UserPlus, 
  LogOut
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

// 역할별 색상 매핑 (모던하고 절제된 색상)
const roleColors = {
  backend: 'bg-slate-100 text-slate-700 border-slate-200',
  frontend: 'bg-emerald-100 text-emerald-700 border-emerald-200',
  ai: 'bg-violet-100 text-violet-700 border-violet-200',
  pm: 'bg-amber-100 text-amber-700 border-amber-200',
  design: 'bg-rose-100 text-rose-700 border-rose-200'
}

const TeamPage: React.FC = () => {
  const navigate = useNavigate()
  const { isLoading } = useTeamStore()
  const { user } = useUserStore()
  
  const [chatMessage, setChatMessage] = useState('')
  const [chatMessages, setChatMessages] = useState([
    { id: 1, sender: '김개발', message: '안녕하세요! 팀에 합류하게 되어 기쁩니다', time: '14:30' },
    { id: 2, sender: '박디자인', message: '반가워요! 함께 멋진 프로젝트 만들어봐요', time: '14:32' },
    { id: 3, sender: '이기획', message: '첫 회의는 언제 할까요?', time: '14:35' }
  ])

  // 모의 팀원 데이터
  const mockTeamMembers = [
    { id: 1, name: '김개발', role: 'backend', isLeader: true, avatar: '' },
    { id: 2, name: '박디자인', role: 'design', isLeader: false, avatar: '' },
    { id: 3, name: '이기획', role: 'pm', isLeader: false, avatar: '' },
    { id: 4, name: '최프론트', role: 'frontend', isLeader: false, avatar: '' },
    { id: 5, name: '정AI', role: 'ai', isLeader: false, avatar: '' }
  ]

  // 모의 팀 정보 데이터
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

  const handleSendMessage = () => {
    if (chatMessage.trim()) {
      const newMessage = {
        id: chatMessages.length + 1,
        sender: user?.userName || '나',
        message: chatMessage,
        time: new Date().toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })
      }
      setChatMessages([...chatMessages, newMessage])
      setChatMessage('')
    }
  }

  const handleLeaveTeam = () => {
    if (confirm('정말로 팀을 나가시겠습니까?')) {
      navigate('/matching')
    }
  }

  const handleRecommendMember = () => {
    navigate('/matching?recommend=true')
  }

  // const handleGoBack = () => {
  //   navigate(-1)
  // }

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <div className="w-8 h-8 border-4 border-blue-200 border-t-blue-600 rounded-full animate-spin mx-auto mb-4" />
          <p className="text-gray-600">팀 정보를 불러오는 중...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* 메인페이지 헤더 */}
      <Header />
      <div className="max-w-7xl mx-auto p-4 sm:p-6 lg:p-8">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* 팀 정보 섹션 */}
          <div className="lg:col-span-1 space-y-6">
            {/* 팀 정보 카드 */}
            <Card className="bg-white border border-gray-200 shadow-sm">
              <CardHeader>
                <CardTitle className="text-lg font-medium text-gray-900">팀 정보</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div>
                  <h3 className="text-sm font-medium text-gray-700 mb-2">팀 소개</h3>
                  <p className="text-sm text-gray-600 bg-gray-50 p-3 rounded-lg">
                    {mockTeamInfo.teamDescription}
                  </p>
                </div>

                <Separator />

                <div>
                  <h3 className="text-sm font-medium text-gray-700 mb-2">모집 역할</h3>
                  <div className="flex flex-wrap gap-2">
                    {Object.entries(mockTeamInfo.roleDistribution).map(([role, count]) => (
                      <Badge 
                        key={role} 
                        variant="outline"
                        className={cn("text-xs", roleColors[role as keyof typeof roleColors])}
                      >
                        {role.toUpperCase()} {count}명
                      </Badge>
                    ))}
                  </div>
                </div>

                <Separator />

                <div>
                  <h3 className="text-sm font-medium text-gray-700 mb-2">프로젝트 성향</h3>
                  <div className="flex flex-wrap gap-1">
                    {mockTeamInfo.teamPreference.map((pref) => (
                      <Badge key={pref} variant="secondary" className="text-xs">
                        {projectGoalLabels[pref]}
                      </Badge>
                    ))}
                  </div>
                </div>

                <div>
                  <h3 className="text-sm font-medium text-gray-700 mb-2">팀 분위기</h3>
                  <div className="flex flex-wrap gap-1">
                    {mockTeamInfo.teamVibe.map((vibe) => (
                      <Badge key={vibe} variant="secondary" className="text-xs">
                        {projectVibeLabels[vibe]}
                      </Badge>
                    ))}
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* 팀원 목록 카드 */}
            <Card className="bg-white border border-gray-200 shadow-sm">
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle className="text-lg font-medium text-gray-900">팀원 목록</CardTitle>
                  <Button 
                    onClick={handleRecommendMember}
                    size="sm"
                    className="bg-blue-600 hover:bg-blue-700 text-white"
                  >
                    <UserPlus className="w-4 h-4 mr-1" />
                    추천
                  </Button>
                </div>
              </CardHeader>
              <CardContent>
                <div className="space-y-3">
                  {mockTeamMembers.map((member) => (
                    <div key={member.id} className="flex items-center gap-3 p-2 rounded-lg hover:bg-gray-50">
                      <Avatar className="w-8 h-8">
                        <AvatarImage src={member.avatar} />
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
          </div>

          {/* 채팅 섹션 */}
          <div className="relative lg:col-span-2 flex flex-col h-full">
            <Card className="bg-white border border-gray-200 shadow-sm h-[600px] lg:h-[700px] flex flex-col">
              <CardHeader className="flex-shrink-0">
                <CardTitle className="flex items-center gap-2 text-lg font-medium text-gray-900">
                  <MessageCircle className="w-5 h-5" />
                  팀 채팅
                </CardTitle>
              </CardHeader>
              <CardContent className="flex-1 flex flex-col p-0">
                <ScrollArea className="flex-1 px-6">
                  <div className="space-y-4 py-4">
                    {chatMessages.map((msg) => (
                      <div key={msg.id} className="">
                        <div className="flex items-center gap-2 mb-1">
                          <span className="text-sm font-medium text-gray-900">{msg.sender}</span>
                          <span className="text-xs text-gray-500">{msg.time}</span>
                        </div>
                        <div className="bg-gray-50 p-3 rounded-lg max-w-md">
                          <p className="text-sm text-gray-700">{msg.message}</p>
                        </div>
                      </div>
                    ))}
                  </div>
                </ScrollArea>
                <div className="flex-shrink-0 p-4 border-t border-gray-200">
                  <div className="flex gap-2">
                    <Input
                      value={chatMessage}
                      onChange={(e) => setChatMessage(e.target.value)}
                      placeholder="메시지를 입력하세요..."
                      className="flex-1"
                      onKeyPress={(e) => e.key === 'Enter' && handleSendMessage()}
                    />
                    <Button 
                      onClick={handleSendMessage}
                      className="bg-blue-600 hover:bg-blue-700 text-white"
                    >
                      <Send className="w-4 h-4" />
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
            {/* 팀 나가기 버튼 - 오른쪽 하단 고정 */}
            <div className="w-full flex justify-end mt-4">
              <Button 
                onClick={handleLeaveTeam}
                variant="destructive"
                className="w-full lg:w-auto"
              >
                <LogOut className="w-4 h-4 mr-2" />
                팀 나가기
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default TeamPage
