import React, { useState, useEffect } from 'react'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Input } from "@/components/ui/input"
import { Search, Filter, ChevronDown } from "lucide-react"
import type { Team } from './TeamSection'
import { teamAPI } from '@/api/team'
import type { TeamDetailResponse } from '@/types/team'
import TeamCard from './TeamCard'
import { useEnumMapper } from '@/hooks/useEnumMapper'

interface TeamsModalProps {
  isOpen: boolean
  onClose: () => void
  onViewTeam?: (teamId: number) => void
  // 팀 상세 모달 열림 여부 (중첩 모달 시 ESC/바깥 클릭 무시용)
  isDetailOpen?: boolean
}


export default function TeamsModal({ isOpen, onClose, onViewTeam, isDetailOpen = false }: TeamsModalProps) {
  const [teams, setTeams] = useState<Team[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedTech, setSelectedTech] = useState<string[]>([])
  const [selectedDomains, setSelectedDomains] = useState<string[]>([])
  const [isFilterOpen, setIsFilterOpen] = useState(false)
  const { mapTechStackArray, mapProjectGoalArray } = useEnumMapper()

  // 팀 목록 로드
  useEffect(() => {
    const loadTeams = async () => {
      if (!isOpen) return
      
      setIsLoading(true)
      try {
        const response = await teamAPI.getAllTeams()
        const teamData = response.data.data
        
        // TeamDetailResponse[]를 Team[]로 변환
        const convertedTeams: Team[] = teamData.map((team: TeamDetailResponse) => ({
          id: team.teamId,
          name: team.teamName || '',
          description: team.teamDescription || '',
          // 팀원들의 기술스택을 합쳐 팀 기술스택으로 사용
          tech: Array.from(new Set((team.members || []).flatMap((m: any) => mapTechStackArray(m.techStack as any)))),
          members: team.members.length,
          maxMembers: team.backendCount + team.frontendCount + team.aiCount + team.designCount + team.pmCount,
          deadline: '', // 마감일 정보가 없으므로 빈 문자열
          domain: team.teamDomain,
          // 팀 목표(선호도) 표시는 ProjectGoal 기준으로 매핑
          projectPreferences: mapProjectGoalArray(team.teamPreference as any) || [],
          leader: {
            name: team.leader.userName,
            avatar: '',
            role: team.leader.wantedPosition?.[0] || ''
          },
          roleDistribution: {
            backend: team.backendCount,
            frontend: team.frontendCount,
            ai: team.aiCount,
            design: team.designCount,
            pm: team.pmCount
          },
          roleCurrent: {
            backend: team.members.filter((m: any) => m.wantedPosition?.includes('BACKEND')).length,
            frontend: team.members.filter((m: any) => m.wantedPosition?.includes('FRONTEND')).length,
            ai: team.members.filter((m: any) => m.wantedPosition?.includes('AI')).length,
            design: team.members.filter((m: any) => m.wantedPosition?.includes('DESIGN')).length,
            pm: team.members.filter((m: any) => m.wantedPosition?.includes('PM')).length
          }
        }))
        
        setTeams(convertedTeams)
      } catch (error) {
        console.error('팀 목록 로드 실패:', error)
      } finally {
        setIsLoading(false)
      }
    }

    loadTeams()
  }, [isOpen])

  // 모든 기술 스택 추출
  const allTech: string[] = Array.from(new Set(teams.flatMap(team => team.tech)))

  // 모든 도메인 추출 (domains 우선, 없으면 domain)
  const allDomains: string[] = Array.from(
    new Set(
      teams.flatMap(team => {
        if (team.domains && team.domains.length > 0) return team.domains
        return team.domain ? [team.domain] : []
      })
    )
  )

  // 필터링된 팀 목록
  const filteredTeams = teams.filter(team => {
    const nameLc = (team.name || '').toLowerCase()
    const descLc = (team.description || '').toLowerCase()
    const termLc = (searchTerm || '').toLowerCase()
    const matchesSearch = nameLc.includes(termLc) || descLc.includes(termLc)
    const matchesTech = selectedTech.length === 0 || 
                       selectedTech.some(tech => team.tech.includes(tech))
    const teamDomains = team.domains && team.domains.length > 0
      ? team.domains
      : (team.domain ? [team.domain] : [])
    const matchesDomain = selectedDomains.length === 0 ||
      selectedDomains.some(d => teamDomains.includes(d))
    return matchesSearch && matchesTech && matchesDomain
  })

  const toggleTech = (tech: string) => {
    setSelectedTech(prev => 
      prev.includes(tech) 
        ? prev.filter(t => t !== tech)
        : [...prev, tech]
    )
  }

  const toggleDomain = (domain: string) => {
    setSelectedDomains(prev =>
      prev.includes(domain)
        ? prev.filter(d => d !== domain)
        : [...prev, domain]
    )
  }

  return (
    <Dialog open={isOpen} onOpenChange={(open: boolean) => {
      if (!open && isDetailOpen) return
      onClose()
    }} modal={false}>
      <DialogContent aria-describedby={undefined} overlayClassName={isDetailOpen ? 'pointer-events-none' : undefined} className={`max-w-7xl h-[90vh] flex flex-col`}>
        <div className="flex flex-col h-full min-h-0 overflow-hidden pointer-events-auto">
        <DialogHeader className="flex-shrink-0">
          <div className="flex items-center justify-between">
            <DialogTitle className="text-2xl font-bold">팀 전체보기</DialogTitle>
          </div>
        </DialogHeader>

        {/* 검색 및 필터 */}
        <div className="flex-shrink-0 space-y-4 pb-4 border-b">
          <div className="flex gap-3">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
              <Input
                placeholder="팀 이름이나 설명으로 검색..."
                value={searchTerm}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) => setSearchTerm(e.target.value)}
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
                {allTech.slice(0, 10).map((tech: string) => (
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

          {/* 도메인 필터 */}
          {isFilterOpen && allDomains.length > 0 && (
            <div className="space-y-2">
              <p className="text-sm font-medium text-gray-700">도메인</p>
              <div className="flex flex-wrap gap-2">
                {allDomains.map((domain: string) => (
                  <Badge
                    key={domain}
                    variant={selectedDomains.includes(domain) ? "default" : "outline"}
                    className="cursor-pointer"
                    onClick={() => toggleDomain(domain)}
                  >
                    {domain}
                  </Badge>
                ))}
              </div>
            </div>
          )}
        </div>

        {/* 팀 목록 */}
        <div className="flex-1 min-h-0 overflow-y-auto">
          {isLoading ? (
            <div className="flex justify-center items-center h-32">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            </div>
          ) : (
            <>
              <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-4 p-1">
                {filteredTeams.map((team) => (
                  <TeamCard
                    key={team.id}
                    team={team}
                    onClick={(teamId) => onViewTeam?.(teamId)}
                  />
                ))}
              </div>

              {filteredTeams.length === 0 && (
                <div className="text-center py-12">
                  <p className="text-gray-500">검색 조건에 맞는 팀이 없습니다.</p>
                </div>
              )}
            </>
          )}
        </div>

        {/* 하단 정보 */}
        <div className="flex-shrink-0 pt-4 border-t">
          <p className="text-sm text-gray-600 text-center">
            총 {filteredTeams.length}개의 팀이 있습니다.
          </p>
        </div>
        </div>
      </DialogContent>
    </Dialog>
  )
}
