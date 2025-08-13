// Team 관련 컴포넌트 사용 예시
import { useEffect } from 'react'
import { useTeam } from '@/hooks/useTeam'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Users, User, MessageSquare } from 'lucide-react'

export default function TeamList() {
  const {
    teams,
    isLoading,
    error,
    fetchAllTeams,
    requestJoinTeam
  } = useTeam()

  useEffect(() => {
    fetchAllTeams()
  }, [])

  const handleJoinRequest = async (teamId: number, userId: number) => {
    try {
      await requestJoinTeam(teamId, userId, '팀에 참여하고 싶습니다!')
    } catch (error) {
      console.error('팀 가입 요청 실패:', error)
    }
  }

  if (isLoading) {
    return (
      <div className="flex items-center justify-center p-8">
        <div className="text-lg">팀 목록을 불러오는 중...</div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="flex items-center justify-center p-8">
        <div className="text-red-500">오류: {error}</div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-bold">팀 목록</h2>
        <Button onClick={fetchAllTeams}>새로고침</Button>
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {teams.map((team) => (
          <Card key={team.teamId} className="hover:shadow-lg transition-shadow">
            <CardHeader>
              <div className="flex items-center justify-between">
                <CardTitle className="text-lg">{team.teamName}</CardTitle>
                <Badge variant="secondary">
                  <Users className="w-4 h-4 mr-1" />
                  {team.memberCount || 0}명
                </Badge>
              </div>
            </CardHeader>
            
            <CardContent className="space-y-4">
              {team.teamDescription && (
                <p className="text-sm text-gray-600 line-clamp-3">
                  {team.teamDescription}
                </p>
              )}
              
              {team.teamDomain && (
                <div>
                  <span className="text-sm font-medium">분야: </span>
                  <span className="text-sm">{team.teamDomain}</span>
                </div>
              )}
              
              {team.teamPreference && team.teamPreference.length > 0 && (
                <div className="flex flex-wrap gap-1">
                  {team.teamPreference.slice(0, 3).map((pref, index) => (
                    <Badge key={index} variant="outline" className="text-xs">
                      {pref}
                    </Badge>
                  ))}
                  {team.teamPreference.length > 3 && (
                    <Badge variant="outline" className="text-xs">
                      +{team.teamPreference.length - 3}
                    </Badge>
                  )}
                </div>
              )}
              
              <div className="flex space-x-2 pt-2">
                <Button 
                  size="sm" 
                  className="flex-1"
                  onClick={() => handleJoinRequest(team.teamId, 1)} // userId는 실제 로그인한 사용자 ID
                >
                  <User className="w-4 h-4 mr-1" />
                  가입 신청
                </Button>
                <Button size="sm" variant="outline">
                  <MessageSquare className="w-4 h-4 mr-1" />
                  채팅
                </Button>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
      
      {teams.length === 0 && !isLoading && (
        <div className="text-center p-8 text-gray-500">
          등록된 팀이 없습니다.
        </div>
      )}
    </div>
  )
}