import { useState } from 'react'
import Header from "@/components/layout/Header"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Progress } from "@/components/ui/progress"
import { Badge } from "@/components/ui/badge"
import { Separator } from "@/components/ui/separator"
import { Button } from "@/components/ui/button"
import { RefreshCw } from "lucide-react"
import TeamDomainChart from "@/components/features/dashboard/TeamDomainChart"
import TeamPositionChart from "@/components/features/dashboard/TeamPositionChart"
import PositionDonutChart from "@/components/features/dashboard/PositionDonutChart"
import MajorMinorPieChart from "@/components/features/dashboard/MajorMinorPieChart"
import TechStackTreemap from "@/components/features/dashboard/TechStackTreemap"
import { useDashboard } from "@/hooks/useDashboard"

export default function Dashboard() {
  const [selectedDomain, setSelectedDomain] = useState<string>("all")
  const { data, stats, isLoading, error, refresh } = useDashboard()

  // API 데이터가 로딩 중이거나 없으면 기본값 사용
  const totalWaitingUsers = stats?.totalUsers || 0
  const matchedUsers = stats?.matchedUsers || 0
  const matchingRate = stats?.matchingRate || 0

  const majorTotal = stats?.majorStats?.total || 0
  const nonMajorTotal = stats?.nonMajorStats?.total || 0
  const matchedMajors = stats?.majorStats?.matched || 0
  const matchedNonMajors = stats?.nonMajorStats?.matched || 0
  const majorMatchingRate = stats?.majorStats?.matchingRate || 0
  const nonMajorMatchingRate = stats?.nonMajorStats?.matchingRate || 0

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <Header />
      
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-gray-900 mb-2">대시보드</h1>
              <p className="text-gray-600">팀 매칭 현황과 사용자 통계를 한눈에 확인하세요</p>
            </div>
            <Button 
              onClick={refresh} 
              disabled={isLoading}
              className="flex items-center gap-2"
            >
              <RefreshCw className={`h-4 w-4 ${isLoading ? 'animate-spin' : ''}`} />
              새로고침
            </Button>
          </div>
          {error && (
            <div className="mt-4 p-3 bg-red-50 border border-red-200 rounded-md">
              <p className="text-red-600 text-sm">{error}</p>
            </div>
          )}
        </div>

        <div className="space-y-8">
          {/* 팀 현황 섹션 */}
          <div>
            <div className="flex items-center gap-3 mb-6">
              <h2 className="text-2xl font-bold text-gray-900">팀 현황</h2>
              <Badge variant="outline" className="text-blue-600 border-blue-200">Team Status</Badge>
            </div>
            
            <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
              {/* 도메인 선택 비율 - Polar Area Chart */}
              <Card className="lg:col-span-1">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <Badge variant="secondary">Polar Area</Badge>
                    도메인 선택 비율
                  </CardTitle>
                  <CardDescription>
                    각 도메인별 팀 분포 현황
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <TeamDomainChart data={data?.domain} />
                </CardContent>
              </Card>

              {/* 매칭 비율 - Progress Bar */}
              <Card className="lg:col-span-1">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <Badge variant="secondary">Progress</Badge>
                    매칭 현황
                  </CardTitle>
                  <CardDescription>
                    전체 대기자 중 팀에 속한 인원 비율
                  </CardDescription>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="space-y-2">
                    <div className="flex justify-between text-sm">
                      <span>매칭된 인원</span>
                      <span className="font-medium">{matchedUsers}명</span>
                    </div>
                    <Progress value={matchingRate} className="h-3" />
                    <div className="flex justify-between text-xs text-gray-500">
                      <span>전체 대기자: {totalWaitingUsers}명</span>
                      <span>{matchingRate.toFixed(1)}%</span>
                    </div>
                  </div>
                  {/* 전공자 매칭 현황 */}
                  <div className="space-y-2">
                    <div className="flex justify-between text-sm">
                      <span>전공자 매칭</span>
                      <span className="font-medium">{matchedMajors}명</span>
                    </div>
                    <Progress value={majorMatchingRate} className="h-3" />
                    <div className="flex justify-between text-xs text-gray-500">
                      <span>전공자 전체: {majorTotal}명</span>
                      <span>{majorMatchingRate.toFixed(1)}%</span>
                    </div>
                  </div>
                  {/* 비전공자 매칭 현황 */}
                  <div className="space-y-2">
                    <div className="flex justify-between text-sm">
                      <span>비전공자 매칭</span>
                      <span className="font-medium">{matchedNonMajors}명</span>
                    </div>
                    <Progress value={nonMajorMatchingRate} className="h-3" />
                    <div className="flex justify-between text-xs text-gray-500">
                      <span>비전공자 전체: {nonMajorTotal}명</span>
                      <span>{nonMajorMatchingRate.toFixed(1)}%</span>
                    </div>
                  </div>
                  <div className="grid grid-cols-2 gap-4 pt-4">
                    <div className="text-center">
                      <div className="text-2xl font-bold text-green-600">{matchedUsers}</div>
                      <div className="text-sm text-gray-500">매칭 완료</div>
                    </div>
                    <div className="text-center">
                      <div className="text-2xl font-bold text-orange-600">{totalWaitingUsers - matchedUsers}</div>
                      <div className="text-sm text-gray-500">대기 중</div>
                    </div>
                  </div>
                </CardContent>
              </Card>

              {/* 포지션 총합 - Distributed Column Chart */}
              <Card className="lg:col-span-2 xl:col-span-1">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <Badge variant="secondary">Column</Badge>
                    포지션 총합
                  </CardTitle>
                  <CardDescription>
                    도메인별 전체 팀 포지션 분포
                  </CardDescription>
                  <Select value={selectedDomain} onValueChange={setSelectedDomain}>
                    <SelectTrigger className="w-full">
                      <SelectValue placeholder="도메인 선택" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">전체 도메인</SelectItem>
                      <SelectItem value="웹기술">웹기술</SelectItem>
                      <SelectItem value="웹디자인">웹디자인</SelectItem>
                      <SelectItem value="AI/IoT">AI/IoT</SelectItem>
                      <SelectItem value="모바일">모바일</SelectItem>
                      <SelectItem value="게임개발">게임개발</SelectItem>
                      <SelectItem value="블록체인">블록체인</SelectItem>
                      <SelectItem value="핀테크">핀테크</SelectItem>
                      <SelectItem value="헬스케어">헬스케어</SelectItem>
                      <SelectItem value="교육">교육</SelectItem>
                      <SelectItem value="커머스">커머스</SelectItem>
                      <SelectItem value="소셜미디어">소셜미디어</SelectItem>
                      <SelectItem value="데이터분석">데이터분석</SelectItem>
                      <SelectItem value="보안">보안</SelectItem>
                      <SelectItem value="클라우드">클라우드</SelectItem>
                    </SelectContent>
                  </Select>
                </CardHeader>
                <CardContent>
                  <TeamPositionChart selectedDomain={selectedDomain} data={data?.domainPos} />
                </CardContent>
              </Card>
            </div>
          </div>

          {/* 구분선 */}
          <Separator className="my-8" />

          {/* 대기자 현황 섹션 */}
          <div>
            <div className="flex items-center gap-3 mb-6">
              <h2 className="text-2xl font-bold text-gray-900">대기자 현황</h2>
              <Badge variant="outline" className="text-green-600 border-green-200">User Status</Badge>
            </div>
            
            <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
              {/* 포지션 비율 - Donut Chart (주/부 토글) */}
              <Card>
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <Badge variant="secondary">Donut Chart</Badge>
                    포지션 분포 (토글)
                  </CardTitle>
                  <CardDescription>
                    주/부 포지션을 선택하여 분포를 확인하세요
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <PositionDonutChart 
                    defaultView="primary" 
                    mainPositions={stats?.positionStats?.main}
                    subPositions={stats?.positionStats?.sub}
                  />
                </CardContent>
              </Card>

              {/* 전공/비전공 비율 - Pie Chart */}
              <Card>
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <Badge variant="secondary">Pie Chart</Badge>
                    전공/비전공 비율
                  </CardTitle>
                  <CardDescription>
                    대기자들의 전공/비전공 구성 비율
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <MajorMinorPieChart 
                    majorCount={majorTotal}
                    nonMajorCount={nonMajorTotal}
                  />
                </CardContent>
              </Card>

              {/* 기술 스택 - Treemap */}
              <Card className="lg:col-span-2 xl:col-span-1">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <Badge variant="secondary">Treemap</Badge>
                    기술 스택 분포
                  </CardTitle>
                  <CardDescription>
                    인기 있는 기술 스택 현황
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <TechStackTreemap data={data?.techstacks} />
                </CardContent>
              </Card>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
