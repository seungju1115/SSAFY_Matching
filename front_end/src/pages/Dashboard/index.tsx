import { useState } from 'react'
import Header from "@/components/layout/Header"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Progress } from "@/components/ui/progress"
import { Badge } from "@/components/ui/badge"
import { Separator } from "@/components/ui/separator"
import TeamDomainChart from "@/components/features/dashboard/TeamDomainChart"
import TeamPositionChart from "@/components/features/dashboard/TeamPositionChart"
import PositionDonutChart from "@/components/features/dashboard/PositionDonutChart"
import MajorMinorPieChart from "@/components/features/dashboard/MajorMinorPieChart"
import TechStackTreemap from "@/components/features/dashboard/TechStackTreemap"
import { mockDevelopers } from "@/data/mockData"

export default function Dashboard() {
  const [selectedDomain, setSelectedDomain] = useState<string>("all")
  
  // 모든 대기자 수와 매칭된 인원 수 (예시 데이터)
  const totalWaitingUsers = 1250
  const matchedUsers = 875
  const matchingRate = (matchedUsers / totalWaitingUsers) * 100

  // 전공/비전공 비율을 mockDevelopers에서 산출해 전체 인원에 투영
  const majorCountSample = mockDevelopers.filter((d) => d.isMajor).length
  const totalSample = mockDevelopers.length || 1
  const majorRatio = majorCountSample / totalSample
  const majorTotal = Math.round(totalWaitingUsers * majorRatio)
  const nonMajorTotal = totalWaitingUsers - majorTotal

  // 전공/비전공 매칭 인원은 전체 매칭 인원을 동일 비율로 분배 (데모)
  const matchedMajors = Math.min(majorTotal, Math.round(matchedUsers * majorRatio))
  const matchedNonMajors = Math.max(0, matchedUsers - matchedMajors)
  const majorMatchingRate = majorTotal ? (matchedMajors / majorTotal) * 100 : 0
  const nonMajorMatchingRate = nonMajorTotal ? (matchedNonMajors / nonMajorTotal) * 100 : 0

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <Header />
      
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">대시보드</h1>
          <p className="text-gray-600">팀 매칭 현황과 사용자 통계를 한눈에 확인하세요</p>
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
                  <TeamDomainChart />
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
                      <SelectItem value="web">웹 개발</SelectItem>
                      <SelectItem value="mobile">모바일</SelectItem>
                      <SelectItem value="ai">AI/ML</SelectItem>
                      <SelectItem value="game">게임</SelectItem>
                      <SelectItem value="iot">IoT</SelectItem>
                    </SelectContent>
                  </Select>
                </CardHeader>
                <CardContent>
                  <TeamPositionChart selectedDomain={selectedDomain} />
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
                  <PositionDonutChart defaultView="primary" />
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
                  <MajorMinorPieChart />
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
                  <TechStackTreemap />
                </CardContent>
              </Card>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
