import React, { useState, useEffect, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import { useTeamStore } from '@/stores/teamStore'
import useUserStore from '@/stores/userStore'
import { useSocket } from '@/hooks/useSocket'
import { chatAPI } from '@/api/chat'
import type { Message } from '@/types/chat'
import type { ChatMessageRequest } from '@/api/chat'
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
import { type IMessage } from '@stomp/stompjs'
import { teamAPI } from '@/api/team' // teamAPI 임포트

// --- (기존 라벨, 색상 매핑 데이터는 동일) ---
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
const roleColors = {
  backend: 'bg-slate-100 text-slate-700 border-slate-200',
  frontend: 'bg-emerald-100 text-emerald-700 border-emerald-200',
  ai: 'bg-violet-100 text-violet-700 border-violet-200',
  pm: 'bg-amber-100 text-amber-700 border-amber-200',
  design: 'bg-rose-100 text-rose-700 border-rose-200'
}
// --- (여기까지 동일) ---

const TeamPage: React.FC = () => {
  const navigate = useNavigate()
  const { isLoading, currentTeam, teamMembers, setLoading, setCurrentTeam, setTeamMembers, setError } = useTeamStore()
  const { user } = useUserStore()
  const { isConnected, subscribeToRoom, sendChatMessage } = useSocket()
  const scrollAreaRef = useRef<HTMLDivElement>(null)

  const [newMessage, setNewMessage] = useState('')
  const [messages, setMessages] = useState<Message[]>([])

  // 팀 데이터 불러오기
  useEffect(() => {
    const fetchTeamData = async () => {
      if (!user.teamId) {
        setError('팀 ID가 없습니다.')
        setLoading(false)
        return
      }

      setLoading(true)
      try {
        const [teamDetailResponse, teamMembersResponse] = await Promise.all([
          teamAPI.getTeamDetail(user.teamId),
          teamAPI.getTeamMembers(user.teamId)
        ])
        
        setCurrentTeam(teamDetailResponse.data)
        setTeamMembers(teamMembersResponse.data) // teamMembersResponse.data가 TeamMember[] 타입이라고 가정
      } catch (err) {
        console.error('팀 데이터 불러오기 실패:', err)
        setError('팀 데이터를 불러오는 데 실패했습니다.')
      } finally {
        setLoading(false)
      }
    }

    fetchTeamData()
  }, [user.teamId, setCurrentTeam, setTeamMembers, setLoading, setError])


  // 채팅 메시지 불러오기 및 웹소켓 구독
  useEffect(() => {
    if (!isConnected || !user.teamId) return

    const initChat = async () => {
      try {
        const response = await chatAPI.getMessages(user.teamId!)
        // TODO: API 응답 타입 확인 후 수정 필요
        setMessages(response.data || []) 
      } catch (error) {
        console.error('Error fetching chat history:', error)
        setMessages([])
      }
    }

    initChat()

    const unsubscribe = subscribeToRoom(user.teamId, (message: IMessage) => {
      const incomingMessage: Message = JSON.parse(message.body)
      setMessages((prevMessages) => [...prevMessages, incomingMessage])
    })

    return () => {
      if (unsubscribe) {
        unsubscribe()
      }
    }
  }, [isConnected, user.teamId, subscribeToRoom])

  // 메시지 리스트가 업데이트될 때마다 스크롤을 맨 아래로 이동
  useEffect(() => {
    if (scrollAreaRef.current) {
      scrollAreaRef.current.scrollTo({ top: scrollAreaRef.current.scrollHeight, behavior: 'smooth' })
    }
  }, [messages])

  const handleSendMessage = () => {
    if (newMessage.trim() && user.id && user.teamId) {
      const messagePayload: ChatMessageRequest = {
        type: 'CHAT',
        roomId: user.teamId,
        senderId: user.id,
        message: newMessage,
      }
      sendChatMessage(messagePayload)
      setNewMessage('')
    }
  }

  // --- (기존 핸들러 함수들은 동일) ---
  const handleLeaveTeam = async () => {
    if (confirm('정말로 팀을 나가시겠습니까?')) {
      try {
        if (user.id) {
          await teamAPI.leaveTeam(user.id)
          alert('팀을 성공적으로 나갔습니다.')
          navigate('/matching')
        } else {
          alert('사용자 정보를 찾을 수 없습니다.')
        }
      } catch (error) {
        console.error('팀 나가기 실패:', error)
        alert('팀 나가기에 실패했습니다. 다시 시도해주세요.')
      }
    }
  }
  const handleRecommendMember = () => {
    navigate('/matching?recommend=true')
  }
  // --- (여기까지 동일) ---

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
      <Header />
      <div className="max-w-7xl mx-auto p-4 sm:p-6 lg:p-8">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* --- (팀 정보, 팀원 목록 섹션 JSX는 동일) --- */}
          <div className="lg:col-span-1 space-y-6">
            {currentTeam && (
              <Card className="bg-white border border-gray-200 shadow-sm">
                <CardHeader>
                  <CardTitle className="text-xl font-semibold text-gray-900">{currentTeam.teamName}</CardTitle>
                  <p className="text-sm text-gray-500">{currentTeam.teamDomain}</p>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div>
                    <h3 className="text-md font-medium text-gray-700 mb-2">팀 설명</h3>
                    <p className="text-sm text-gray-600">{currentTeam.teamDescription}</p>
                  </div>
                  <div>
                    <h3 className="text-md font-medium text-gray-700 mb-2">프로젝트 목표</h3>
                    <div className="flex flex-wrap gap-2">
                      {currentTeam.teamPreference?.map((goal) => (
                        <Badge key={goal} variant="outline" className="bg-blue-50 text-blue-700 border-blue-200">
                          {projectGoalLabels[goal]}
                        </Badge>
                      ))}
                    </div>
                  </div>
                  <div>
                    <h3 className="text-md font-medium text-gray-700 mb-2">팀 분위기</h3>
                    <div className="flex flex-wrap gap-2">
                      {currentTeam.teamVibe?.map((vibe) => (
                        <Badge key={vibe} variant="outline" className="bg-green-50 text-green-700 border-green-200">
                          {projectVibeLabels[vibe]}
                        </Badge>
                      ))}
                    </div>
                  </div>
                  <div>
                    <h3 className="text-md font-medium text-gray-700 mb-2">역할 분배</h3>
                    <div className="grid grid-cols-2 gap-2 text-sm text-gray-600">
                      {Object.entries(currentTeam.roleDistribution || {}).map(([role, count]) => (
                        <div key={role} className="flex justify-between">
                          <span>{role.charAt(0).toUpperCase() + role.slice(1)}:</span>
                          <span>{count}명</span>
                        </div>
                      ))}
                    </div>
                  </div>
                </CardContent>
              </Card>
            )}

            <Card className="bg-white border border-gray-200 shadow-sm">
              <CardHeader>
                <CardTitle className="text-xl font-semibold text-gray-900">팀원 목록</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  {teamMembers.length > 0 ? (
                    teamMembers.map((member) => (
                      <div key={member.id} className="flex items-center space-x-3">
                        <Avatar>
                          <AvatarImage src={member.avatar || `https://api.dicebear.com/7.x/initials/svg?seed=${member.name}`} />
                          <AvatarFallback>{member.name.charAt(0)}</AvatarFallback>
                        </Avatar>
                        <div className="flex-1">
                          <div className="flex items-center gap-2">
                            <span className="font-medium text-gray-800">{member.name}</span>
                            {member.isLeader && <Crown className="w-4 h-4 text-yellow-500" />}
                          </div>
                          <Badge className={cn("text-xs", roleColors[member.role as keyof typeof roleColors])}>
                            {member.role}
                          </Badge>
                        </div>
                        {/* TODO: 팀원 프로필 보기, 강퇴 등 액션 버튼 추가 */}
                      </div>
                    ))
                  ) : (
                    <p className="text-gray-500">팀원이 없습니다.</p>
                  )}
                </div>
                <Separator className="my-4" />
                <Button 
                  onClick={handleRecommendMember}
                  className="w-full bg-purple-600 hover:bg-purple-700 text-white"
                >
                  <UserPlus className="w-4 h-4 mr-2" />
                  팀원 추천받기
                </Button>
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
                <ScrollArea className="flex-1 px-6" ref={scrollAreaRef}>
                  <div className="space-y-4 py-4">
                    {messages.map((msg) => {
                      const sender = teamMembers.find(member => member.id === msg.senderId);
                      const senderName = sender ? sender.name : `User ${msg.senderId}`;
                      return (
                        <div key={msg.id} className="">
                          <div className="flex items-center gap-2 mb-1">
                            <span className="text-sm font-medium text-gray-900">{senderName}</span>
                            <span className="text-xs text-gray-500">
                              {new Date(msg.timestamp).toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })}
                            </span>
                          </div>
                          <div className="bg-gray-50 p-3 rounded-lg max-w-md">
                            <p className="text-sm text-gray-700">{msg.content}</p>
                          </div>
                        </div>
                      );
                    })}
                  </div>
                </ScrollArea>
                <div className="flex-shrink-0 p-4 border-t border-gray-200">
                  <div className="flex gap-2">
                    <Input
                      value={newMessage}
                      onChange={(e) => setNewMessage(e.target.value)}
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

