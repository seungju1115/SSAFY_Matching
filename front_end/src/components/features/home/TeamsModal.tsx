import { useState } from 'react'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import { Search, Filter, ChevronDown } from 'lucide-react'
import type { Team } from './TeamSection'
import TeamCard from './TeamCard'

interface TeamsModalProps {
  isOpen: boolean
  onClose: () => void
  teams: Team[]
  onViewTeam?: (teamId: number) => void
}

export default function TeamsModal({ isOpen, onClose, teams, onViewTeam }: TeamsModalProps) {
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedTech, setSelectedTech] = useState<string[]>([])
  const [isFilterOpen, setIsFilterOpen] = useState(false)

  // 모든 기술 스택 추출
  const allTech = Array.from(new Set(teams.flatMap(team => team.tech)))

  // 필터링된 팀 목록
  const filteredTeams = teams.filter(team => {
    const matchesSearch = team.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         team.description.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesTech = selectedTech.length === 0 || 
                       selectedTech.some(tech => team.tech.includes(tech))
    return matchesSearch && matchesTech
  })

  const toggleTech = (tech: string) => {
    setSelectedTech(prev => 
      prev.includes(tech) 
        ? prev.filter(t => t !== tech)
        : [...prev, tech]
    )
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-6xl max-h-[90vh] overflow-hidden flex flex-col">
        <DialogHeader className="flex-shrink-0">
          <DialogTitle className="text-2xl font-bold">팀 전체보기</DialogTitle>
        </DialogHeader>

        {/* 검색 및 필터 */}
        <div className="flex-shrink-0 space-y-4 pb-4 border-b">
          <div className="flex gap-3">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
              <Input
                placeholder="팀 이름이나 설명으로 검색..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
            <Button 
              variant="outline" 
              className="flex items-center gap-2"
              onClick={() => setIsFilterOpen(!isFilterOpen)}
            >
              <Filter className="h-4 w-4" />
              필터
              <ChevronDown className={`h-4 w-4 transition-transform ${isFilterOpen ? 'rotate-180' : ''}`} />
            </Button>
          </div>

          {/* 기술 스택 필터 */}
          {isFilterOpen && (
            <div className="space-y-2">
              <p className="text-sm font-medium text-gray-700">기술 스택</p>
              <div className="flex flex-wrap gap-2">
                {allTech.slice(0, 10).map(tech => (
                  <Badge
                    key={tech}
                    variant={selectedTech.includes(tech) ? "default" : "outline"}
                    className="cursor-pointer"
                    onClick={() => toggleTech(tech)}
                  >
                    {tech}
                  </Badge>
                ))}
              </div>
            </div>
          )}
        </div>

        {/* 팀 목록 */}
        <div className="flex-1 overflow-y-auto">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 p-1">
            {filteredTeams.map((team) => {
              // TeamSection과 동일한 데모 역할 분배 적용 (없을 경우)
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
                domain: team.domain ?? '웹 서비스',
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

          {filteredTeams.length === 0 && (
            <div className="text-center py-12">
              <p className="text-gray-500">검색 조건에 맞는 팀이 없습니다.</p>
            </div>
          )}
        </div>

        {/* 하단 정보 */}
        <div className="flex-shrink-0 pt-4 border-t">
          <p className="text-sm text-gray-600 text-center">
            총 {filteredTeams.length}개의 팀이 있습니다.
          </p>
        </div>
      </DialogContent>
    </Dialog>
  )
}
