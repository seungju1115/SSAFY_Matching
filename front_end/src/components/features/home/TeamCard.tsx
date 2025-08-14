import { Badge } from "@/components/ui/badge"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Users } from "lucide-react"
import type { Team } from "@/hooks/useTeam"

interface TeamCardProps {
  team: Team
  onClick?: (teamId: number) => void
  className?: string
}

export default function TeamCard({ 
  team, 
  onClick,
  className = ""
}: TeamCardProps) {
  return (
    <Card className={`hover:shadow-md transition-shadow duration-200 cursor-pointer bg-white border border-gray-100 rounded-lg h-full flex flex-col ${className}`} 
          onClick={() => onClick?.(team.id)}>
      <CardHeader className="pb-3">
        <div className="flex items-start gap-3">
          <div className="flex-1">
            <CardTitle className="text-lg leading-tight font-bold">{team.leader.name} 팀장</CardTitle>
            <Badge variant="secondary" className="text-xs mt-2">
              {team.members}/{team.maxMembers}명 모집
            </Badge>
          </div>
        </div>
      </CardHeader>
      <CardContent className="pt-0 flex-1 flex flex-col">
        <div className="flex flex-col gap-4 flex-1">
          {/* 도메인 섹션 */}
          <div className="bg-gray-50 rounded-lg p-3">
            <div className="flex items-center gap-2 mb-2">
              <Users className="h-4 w-4 text-gray-600" />
              <span className="text-xs font-semibold text-gray-700">프로젝트 도메인</span>
            </div>
            <div className="text-sm font-medium text-gray-800">
              {team.domain || '웹 개발'}
            </div>
          </div>
          
          {/* 역할별 비율 정보 */}
          <div className="grid grid-cols-1 gap-3 text-xs">
            <div className="bg-white rounded-lg p-3 border border-gray-100">
              <div className="text-gray-500 mb-2 flex items-center justify-between">
                <span>역할 비율</span>
                <span className="font-medium text-gray-800">
                  {team.members}/{team.maxMembers}명
                </span>
              </div>
              
              {team.roleDistribution ? (
                <div className="space-y-2">
                  {/* 역할별 충원율: 현재(추정)/목표 */}
                  {team.roleDistribution.BACKEND > 0 && (
                    (() => {
                      const target = team.roleDistribution!.BACKEND
                      const estimated = Math.floor((team.members * target) / Math.max(team.maxMembers, 1))
                      const provided = team.roleCurrent?.BACKEND ?? estimated
                      const current = Math.min(target, provided)
                      const pct = target ? Math.round((current / target) * 100) : 0
                      return (
                        <div className="flex items-center gap-2" title={`백엔드 ${current}/${target}`}>
                          <div className="w-8 text-[10px] text-gray-600">BE</div>
                          <div className="flex-1 bg-gray-200 rounded-full h-2 overflow-hidden">
                            <div className="h-2 bg-blue-400 transition-all duration-300" style={{ width: `${pct}%` }} />
                          </div>
                          <span className="text-[11px] text-gray-700 font-medium w-14 text-right">{current}/{target}</span>
                        </div>
                      )
                    })()
                  )}
                  {team.roleDistribution.FRONTEND > 0 && (
                    (() => {
                      const target = team.roleDistribution!.FRONTEND
                      const estimated = Math.floor((team.members * target) / Math.max(team.maxMembers, 1))
                      const provided = team.roleCurrent?.FRONTEND ?? estimated
                      const current = Math.min(target, provided)
                      const pct = target ? Math.round((current / target) * 100) : 0
                      return (
                        <div className="flex items-center gap-2" title={`프론트엔드 ${current}/${target}`}>
                          <div className="w-8 text-[10px] text-gray-600">FE</div>
                          <div className="flex-1 bg-gray-200 rounded-full h-2 overflow-hidden">
                            <div className="h-2 bg-green-400 transition-all duration-300" style={{ width: `${pct}%` }} />
                          </div>
                          <span className="text-[11px] text-gray-700 font-medium w-14 text-right">{current}/{target}</span>
                        </div>
                      )
                    })()
                  )}
                  {team.roleDistribution.AI > 0 && (
                    (() => {
                      const target = team.roleDistribution!.AI
                      const estimated = Math.floor((team.members * target) / Math.max(team.maxMembers, 1))
                      const provided = team.roleCurrent?.AI ?? estimated
                      const current = Math.min(target, provided)
                      const pct = target ? Math.round((current / target) * 100) : 0
                      return (
                        <div className="flex items-center gap-2" title={`AI ${current}/${target}`}>
                          <div className="w-8 text-[10px] text-gray-600">AI</div>
                          <div className="flex-1 bg-gray-200 rounded-full h-2 overflow-hidden">
                            <div className="h-2 bg-purple-400 transition-all duration-300" style={{ width: `${pct}%` }} />
                          </div>
                          <span className="text-[11px] text-gray-700 font-medium w-14 text-right">{current}/{target}</span>
                        </div>
                      )
                    })()
                  )}
                  {team.roleDistribution.DESIGN > 0 && (
                    (() => {
                      const target = team.roleDistribution!.DESIGN
                      const estimated = Math.floor((team.members * target) / Math.max(team.maxMembers, 1))
                      const provided = team.roleCurrent?.DESIGN ?? estimated
                      const current = Math.min(target, provided)
                      const pct = target ? Math.round((current / target) * 100) : 0
                      return (
                        <div className="flex items-center gap-2" title={`디자인 ${current}/${target}`}>
                          <div className="w-8 text-[10px] text-gray-600">UI</div>
                          <div className="flex-1 bg-gray-200 rounded-full h-2 overflow-hidden">
                            <div className="h-2 bg-pink-400 transition-all duration-300" style={{ width: `${pct}%` }} />
                          </div>
                          <span className="text-[11px] text-gray-700 font-medium w-14 text-right">{current}/{target}</span>
                        </div>
                      )
                    })()
                  )}
                  {team.roleDistribution.PM > 0 && (
                    (() => {
                      const target = team.roleDistribution!.PM
                      const estimated = Math.floor((team.members * target) / Math.max(team.maxMembers, 1))
                      const provided = team.roleCurrent?.PM ?? estimated
                      const current = Math.min(target, provided)
                      const pct = target ? Math.round((current / target) * 100) : 0
                      return (
                        <div className="flex items-center gap-2" title={`PM ${current}/${target}`}>
                          <div className="w-8 text-[10px] text-gray-600">PM</div>
                          <div className="flex-1 bg-gray-200 rounded-full h-2 overflow-hidden">
                            <div className="h-2 bg-orange-400 transition-all duration-300" style={{ width: `${pct}%` }} />
                          </div>
                          <span className="text-[11px] text-gray-700 font-medium w-14 text-right">{current}/{target}</span>
                        </div>
                      )
                    })()
                  )}
                </div>
              ) : (
                <div className="flex items-center gap-2">
                  <div className="flex-1 bg-gray-200 rounded-full h-2">
                    <div 
                      className="bg-blue-300 h-2 rounded-full transition-all duration-300"
                      style={{ width: `${(team.members / team.maxMembers) * 100}%` }}
                    ></div>
                  </div>
                  <span className="font-medium text-gray-800 text-xs">
                    {team.members}/{team.maxMembers}
                  </span>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* 하단 - 프로젝트 성향 (고정) */}
        <div className="mt-auto pt-3 border-t border-gray-100 flex items-center justify-between">
          <div className="flex flex-wrap gap-1.5">
            <span className="text-xs text-gray-500 mr-1">🎯</span>
            {team.projectPreferences && team.projectPreferences.length > 0 ? (
              team.projectPreferences.slice(0, 2).map((pref) => (
                <Badge
                  key={pref}
                  variant="secondary"
                  className="text-xs font-medium"
                >
                  {pref}
                </Badge>
              ))
            ) : (
              <>
                <Badge variant="secondary" className="text-xs font-medium">
                  포트폴리오
                </Badge>
                <Badge variant="secondary" className="text-xs font-medium">
                  실무경험
                </Badge>
              </>
            )}
          </div>
          <Button 
            size="sm" 
            className="text-xs px-4 h-8"
            onClick={(e) => {
              e.stopPropagation()
              onClick?.(team.id)
            }}
          >
            팀 보기
          </Button>
        </div>
      </CardContent>
    </Card>
  )
}
