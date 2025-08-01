import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Star, MapPin, Plus, Filter } from "lucide-react"

interface Developer {
  id: number
  name: string
  role: string
  experience: string
  skills: string[]
  location: string
  rating: number
  projects: number
  avatar: string
  bio: string
}

interface DeveloperSectionProps {
  developers: Developer[]
  onRegister?: () => void
  onFilter?: () => void
  onViewAll?: () => void
  onViewProfile?: (developerId: number) => void
}

export default function DeveloperSection({ 
  developers, 
  onRegister, 
  onFilter, 
  onViewAll, 
  onViewProfile 
}: DeveloperSectionProps) {
  return (
    <section>
      <div className="flex flex-col sm:flex-row sm:justify-between sm:items-center mb-6 sm:mb-8 gap-4">
        <div>
          <h3 className="text-2xl sm:text-3xl font-bold text-gray-900 mb-2">대기자 현황</h3>
          <p className="text-gray-600 text-sm sm:text-base">프로젝트 참여를 원하는 개발자들</p>
        </div>
        <div className="flex flex-col sm:flex-row gap-2 sm:gap-3">
          <Button className="w-full sm:w-auto" onClick={onRegister}>
            <Plus className="mr-2 h-4 w-4" />
            등록
          </Button>
          <div className="flex gap-2">
            <Button variant="outline" className="flex-1 sm:flex-none" onClick={onFilter}>
              <Filter className="mr-2 h-4 w-4" />
              필터
            </Button>
            <Button variant="outline" className="flex-1 sm:flex-none" onClick={onViewAll}>
              전체보기
            </Button>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-3 sm:gap-4">
        {developers.map((dev) => (
          <Card key={dev.id} className="hover:shadow-md transition-shadow duration-200 h-fit">
            <CardContent className="p-4">
              <div className="space-y-3">
                {/* 개발자 기본 정보 */}
                <div className="flex items-center space-x-3">
                  <Avatar className="h-10 w-10 flex-shrink-0">
                    <AvatarImage src={dev.avatar} />
                    <AvatarFallback className="text-sm">{dev.name[0]}</AvatarFallback>
                  </Avatar>
                  <div className="flex-1 min-w-0">
                    <h4 className="font-medium text-sm truncate">{dev.name}</h4>
                    <p className="text-xs text-gray-600">{dev.role}</p>
                  </div>
                  <div className="flex items-center space-x-1 text-xs text-gray-500">
                    <Star className="h-3 w-3 text-yellow-400 fill-current" />
                    <span>{dev.rating}</span>
                  </div>
                </div>

                {/* 기술 스택 */}
                <div className="flex flex-wrap gap-1">
                  {dev.skills.slice(0, 2).map((skill) => (
                    <Badge key={skill} variant="outline" className="text-xs px-2 py-0.5">{skill}</Badge>
                  ))}
                  {dev.skills.length > 2 && (
                    <Badge variant="secondary" className="text-xs px-2 py-0.5">+{dev.skills.length - 2}</Badge>
                  )}
                </div>

                {/* 경력 및 위치 */}
                <div className="flex items-center justify-between text-xs text-gray-600">
                  <span>경력 {dev.experience}</span>
                  <div className="flex items-center space-x-1">
                    <MapPin className="h-3 w-3" />
                    <span>{dev.location}</span>
                  </div>
                </div>

                {/* 프로필 보기 버튼 */}
                <Button 
                  className="w-full h-8" 
                  size="sm" 
                  variant="outline"
                  onClick={() => onViewProfile?.(dev.id)}
                >
                  프로필 보기
                </Button>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </section>
  )
}

// 타입 export
export type { Developer, DeveloperSectionProps }