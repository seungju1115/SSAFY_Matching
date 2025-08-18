import { Button } from "@/components/ui/button"
import { Plus } from "lucide-react"
import TeamCard from "./TeamCard"

interface TeamMember {
  name: string
  avatar: string
  role: string
}

interface Team {
  id: number
  name: string
  description: string
  introduction?: string
  tech: string[]
  members: number
  maxMembers: number
  deadline: string
  leader: TeamMember
  domains?: string[]
  domain?: string
  projectPreferences?: string[]
  teamAtmosphere?: string[]
  roleDistribution?: {
    backend: number
    frontend: number
    ai: number
    design: number
    pm: number
  }
  roleCurrent?: {
    backend: number
    frontend: number
    ai: number
    design: number
    pm: number
  }
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
          <h3 className="text-2xl sm:text-3xl font-bold text-gray-900 mb-2">팀 대기 현황</h3>
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
        {teams.map((team) => {
          // 데모용 역할 비율 생성: maxMembers 합과 일치하도록 순환 분배
          const demoDistribution = (() => {
            if (team.roleDistribution) return team.roleDistribution
            const order = ['frontend', 'backend', 'ai', 'design', 'pm'] as const
            const dist: NonNullable<Team['roleDistribution']> = {
              backend: 0, frontend: 0, ai: 0, design: 0, pm: 0
            }
            for (let i = 0; i < Math.max(team.maxMembers, 1); i++) {
              const role = order[i % order.length]
              dist[role]++
            }
            return dist
          })()

          const withDemo: Team = {
            ...team,
            domain: team.domain ?? ((team.domains && team.domains.length > 0) ? team.domains[0] : '웹 서비스'),
            projectPreferences: team.projectPreferences ?? ['포트폴리오', '실무경험'],
            roleDistribution: demoDistribution,
          }

          return (
            <TeamCard 
              key={team.id}
              team={withDemo}
              onClick={(teamId) => onViewTeam?.(teamId)}
            />
          )
        })}
      </div>
    </section>
  )
}

// 타입 export
export type { Team, TeamMember, TeamSectionProps }