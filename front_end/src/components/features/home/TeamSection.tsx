import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Separator } from "@/components/ui/separator"
import { Users, Calendar, Plus } from "lucide-react"

interface TeamMember {
  name: string
  avatar: string
  role: string
}

interface Team {
  id: number
  name: string
  description: string
  tech: string[]
  members: number
  maxMembers: number
  deadline: string
  leader: TeamMember
}

interface TeamSectionProps {
  teams: Team[]
  onCreateTeam?: () => void
  onViewAll?: () => void
  onViewTeam?: (teamId: number) => void
}

export default function TeamSection({ 
  teams, 
  onCreateTeam, 
  onViewAll, 
  onViewTeam 
}: TeamSectionProps) {
  return (
    <section className="mb-12 sm:mb-16">
      <div className="flex flex-col sm:flex-row sm:justify-between sm:items-center mb-6 sm:mb-8 gap-4">
        <div>
          <h3 className="text-2xl sm:text-3xl font-bold text-gray-900 mb-2">팀대기 현황</h3>
          <p className="text-gray-600 text-sm sm:text-base">함께할 팀원을 찾고 있는 프로젝트들</p>
        </div>
        <div className="flex flex-col sm:flex-row gap-2 sm:gap-3">
          <Button className="w-full sm:w-auto" onClick={onCreateTeam}>
            <Plus className="mr-2 h-4 w-4" />
            팀 생성
          </Button>
          <Button variant="outline" className="w-full sm:w-auto" onClick={onViewAll}>
            전체보기
          </Button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-4 sm:gap-6">
        {teams.map((team) => (
          <Card key={team.id} className="hover:shadow-lg transition-shadow duration-200">
            <CardHeader className="pb-3">
              <div className="flex justify-between items-start gap-3">
                <div className="flex-1">
                  <CardTitle className="text-lg leading-tight">{team.name}</CardTitle>
                  <CardDescription className="mt-1 sm:mt-2 text-sm leading-relaxed">
                    {team.description}
                  </CardDescription>
                </div>
                <Badge variant="secondary" className="text-xs whitespace-nowrap">
                  {team.members}/{team.maxMembers}명
                </Badge>
              </div>
            </CardHeader>
            <CardContent className="pt-0">
              <div className="space-y-3 sm:space-y-4">
                <div className="flex flex-wrap gap-1.5 sm:gap-2">
                  {team.tech.map((tech) => (
                    <Badge key={tech} variant="outline" className="text-xs">{tech}</Badge>
                  ))}
                </div>
                
                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 text-xs sm:text-sm text-gray-600">
                  <div className="flex items-center space-x-1">
                    <Calendar className="h-3 w-3 sm:h-4 sm:w-4" />
                    <span>마감: {team.deadline}</span>
                  </div>
                  <div className="flex items-center space-x-1">
                    <Users className="h-3 w-3 sm:h-4 sm:w-4" />
                    <span>{team.maxMembers - team.members}명 모집</span>
                  </div>
                </div>

                <Separator />

                <div className="flex items-center justify-between">
                  <div className="flex items-center space-x-2">
                    <Avatar className="h-7 w-7 sm:h-8 sm:w-8">
                      <AvatarImage src={team.leader.avatar} />
                      <AvatarFallback className="text-xs">{team.leader.name[0]}</AvatarFallback>
                    </Avatar>
                    <div>
                      <p className="text-xs sm:text-sm font-medium">{team.leader.name}</p>
                      <p className="text-xs text-gray-500">{team.leader.role}</p>
                    </div>
                  </div>
                  <Button 
                    size="sm" 
                    className="text-xs px-3"
                    onClick={() => onViewTeam?.(team.id)}
                  >
                    팀 보기
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </section>
  )
}

// 타입 export
export type { Team, TeamMember, TeamSectionProps }