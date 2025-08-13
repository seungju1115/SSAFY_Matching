import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTeamStore } from '@/stores/teamStore';
import useUserStore from '@/stores/userStore';
import { useTeam } from '@/hooks/useTeam';
import { teamAPI } from '@/api/team';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Input } from '@/components/ui/input';
import { Separator } from '@/components/ui/separator';
import Header from '@/components/layout/Header';
import { Crown, MessageCircle, Send, UserPlus, LogOut } from 'lucide-react';
import { cn } from '@/lib/utils';
import type { ProjectGoalEnum, ProjectViveEnum } from '@/types/team';
import type { UserDetailResponse } from '@/types/user';

// 기존 상수들은 그대로 유지합니다.
const projectGoalLabels: Record<ProjectGoalEnum, string> = {
  JOB: '취업우선',
  AWARD: '수상목표', 
  PORTFOLIO: '포트폴리오중심',
  STUDY: '학습중심',
  IDEA: '아이디어실현',
  PROFESSIONAL: '실무경험',
  QUICK: '빠른개발',
  QUALITY: '완성도추구'
};

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
};

const roleColors = {
  backend: 'bg-slate-100 text-slate-700 border-slate-200',
  frontend: 'bg-emerald-100 text-emerald-700 border-emerald-200',
  ai: 'bg-violet-100 text-violet-700 border-violet-200',
  pm: 'bg-amber-100 text-amber-700 border-amber-200',
  design: 'bg-rose-100 text-rose-700 border-rose-200'
};

const TeamPage: React.FC = () => {
  const navigate = useNavigate();

  const {
    isLoading,
    error,
    getTeamDetailById,
    setTeamDetail,
    setLoading,
    setError,
  } = useTeamStore();
  const { user } = useUserStore();
  const { leaveTeam } = useTeam();

  // userStore에서 teamId 가져오기
  const teamId = user.teamId;

  const [chatMessage, setChatMessage] = useState('');
  const [chatMessages, setChatMessages] = useState<any[]>([]);

  const teamInfo = teamId ? getTeamDetailById(teamId) : null;
  // The store now holds the members, let's get them from there.
  const teamMembers: UserDetailResponse[] = teamInfo?.members || [];

  useEffect(() => {
    const fetchTeamData = async () => {
      if (!teamId) return;
      if (getTeamDetailById(teamId)) return;
      
      setLoading(true);
      try {
        const response = await teamAPI.getTeamDetail(teamId);
        if (response.data.status === 200) {
          setTeamDetail(response.data.data);
        } else {
          throw new Error(response.data.message);
        }
      } catch (err) {
        console.error("Failed to fetch team details:", err);
        setError('팀 정보를 불러오는 데 실패했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchTeamData();
  }, [teamId, getTeamDetailById, setTeamDetail, setLoading, setError]);

  useEffect(() => {
    if (teamInfo) {
      const welcomeMessage = {
        id: Date.now(),
        sender: 'System',
        message: `팀 '${teamInfo.teamName}'에 오신 것을 환영합니다.`,
        time: new Date().toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })
      };
      setChatMessages([welcomeMessage]);
    }
  }, [teamInfo]);

  const handleSendMessage = () => {
    if (chatMessage.trim() && user) {
      const newMessage = {
        id: Date.now(),
        sender: user.userName || '나',
        message: chatMessage,
      };
      setChatMessages([...chatMessages, newMessage]);
      setChatMessage('');
    }
  };

  const handleLeaveTeam = async () => {
    if (user && user.id !== null && confirm('정말로 팀을 나가시겠습니까?')) {
      try {
        await leaveTeam(user.id);
        navigate('/matching');
      } catch (error) {
        console.error("Failed to leave team:", error);
      }
    }
  };

  const handleRecommendMember = () => {
    navigate('/matching?recommend=true');
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <div className="w-8 h-8 border-4 border-blue-200 border-t-blue-600 rounded-full animate-spin mx-auto mb-4" />
          <p className="text-gray-600">팀 정보를 불러오는 중...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center text-red-500">
          <p>{error}</p>
          <Button onClick={() => navigate('/')} className="mt-4">돌아가기</Button>
        </div>
      </div>
    );
  }

  // 팀이 없는 경우 처리
  if (!teamId) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Header />
        <div className="flex items-center justify-center pt-20">
          <div className="text-center max-w-md">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">소속된 팀이 없습니다</h2>
            <p className="text-gray-600 mb-6">새로운 팀을 만들거나 기존 팀에 참여해보세요.</p>
            <div className="space-y-3">
              <Button onClick={() => navigate('/make-team')} className="w-full">
                팀 만들기
              </Button>
              <Button onClick={() => navigate('/matching')} variant="outline" className="w-full">
                팀 찾기
              </Button>
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (!teamInfo) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <p className="text-gray-600">팀 정보를 찾을 수 없습니다.</p>
          <Button onClick={() => navigate('/matching')} className="mt-4">돌아가기</Button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      <div className="max-w-7xl mx-auto p-4 sm:p-6 lg:p-8">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-1 space-y-6">
            <Card className="bg-white border border-gray-200 shadow-sm">
              <CardHeader>
                <CardTitle className="text-lg font-medium text-gray-900">{teamInfo.teamName}</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div>
                  <h3 className="text-sm font-medium text-gray-700 mb-2">팀 소개</h3>
                  <p className="text-sm text-gray-600 bg-gray-50 p-3 rounded-lg">
                    {teamInfo.teamDescription}
                  </p>
                </div>
                <Separator />
                <div>
                  <h3 className="text-sm font-medium text-gray-700 mb-2">모집 역할</h3>
                  <div className="flex flex-wrap gap-2">
                    <Badge variant="outline" className={cn("text-xs", roleColors.backend)}>BACKEND {teamInfo.backendCount}명</Badge>
                    <Badge variant="outline" className={cn("text-xs", roleColors.frontend)}>FRONTEND {teamInfo.frontendCount}명</Badge>
                    <Badge variant="outline" className={cn("text-xs", roleColors.ai)}>AI {teamInfo.aiCount}명</Badge>
                    <Badge variant="outline" className={cn("text-xs", roleColors.pm)}>PM {teamInfo.pmCount}명</Badge>
                    <Badge variant="outline" className={cn("text-xs", roleColors.design)}>DESIGN {teamInfo.designCount}명</Badge>
                  </div>
                </div>
                <Separator />
                <div>
                  <h3 className="text-sm font-medium text-gray-700 mb-2">프로젝트 성향</h3>
                  <div className="flex flex-wrap gap-1">
                    {teamInfo.teamPreference?.map((pref) => (
                      <Badge key={pref} variant="secondary" className="text-xs">
                        {projectGoalLabels[pref]}
                      </Badge>
                    ))}
                  </div>
                </div>
                <div>
                  <h3 className="text-sm font-medium text-gray-700 mb-2">팀 분위기</h3>
                  <div className="flex flex-wrap gap-1">
                    {teamInfo.teamVive?.map((vibe) => (
                      <Badge key={vibe} variant="secondary" className="text-xs">
                        {projectVibeLabels[vibe]}
                      </Badge>
                    ))}
                  </div>
                </div>
                <Separator />
                <div>
                  <h3 className="text-sm font-medium text-gray-700 mb-2">Raw Team Info (Debug)</h3>
                  <pre className="text-xs bg-gray-100 p-2 rounded-md overflow-auto">
                    {JSON.stringify(teamInfo, null, 2)}
                  </pre>
                </div>
              </CardContent>
            </Card>

            <Card className="bg-white border border-gray-200 shadow-sm">
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle className="text-lg font-medium text-gray-900">팀원 목록</CardTitle>
                  <Button onClick={handleRecommendMember} size="sm" className="bg-blue-600 hover:bg-blue-700 text-white">
                    <UserPlus className="w-4 h-4 mr-1" />
                    추천
                  </Button>
                </div>
              </CardHeader>
              <CardContent>
                <div className="space-y-3">
                  {teamMembers.map((member) => (
                    <div key={member.id} className="flex items-center gap-3 p-2 rounded-lg hover:bg-gray-50">
                      <Avatar className="w-8 h-8">
                        <AvatarImage src={undefined} />
                        <AvatarFallback className="bg-gray-200 text-gray-600 text-sm">
                          {member.userName.charAt(0)}
                        </AvatarFallback>
                      </Avatar>
                      <div className="flex-1 min-w-0">
                        <div className="flex items-center gap-2">
                          <span className="text-sm font-medium text-gray-900 truncate">
                            {member.userName}
                          </span>
                          {member.id === teamInfo.leader.id && (
                            <Crown className="w-3 h-3 text-amber-500 flex-shrink-0" />
                          )}
                        </div>
                        <Badge variant="outline" className={cn("text-xs mt-1", roleColors[member.role.toLowerCase() as keyof typeof roleColors])}>
                          {member.role}
                        </Badge>
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          </div>

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
                    <Button onClick={handleSendMessage} className="bg-blue-600 hover:bg-blue-700 text-white">
                      <Send className="w-4 h-4" />
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
            <div className="w-full flex justify-end mt-4">
              <Button onClick={handleLeaveTeam} variant="destructive" className="w-full lg:w-auto">
                <LogOut className="w-4 h-4 mr-2" />
                탈퇴하기
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TeamPage;