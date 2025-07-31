import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
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
          <h3 className="text-2xl sm:text-3xl font-bold text-gray-900 mb-2">등록자 현황</h3>
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

      <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-4 sm:gap-6">
        {developers.map((dev) => (
          <Card key={dev.id} className="hover:shadow-lg transition-shadow duration-200">
            <CardHeader className="pb-3">
              <div className="flex items-start space-x-3">
                <Avatar className="h-12 w-12 sm:h-16 sm:w-16 flex-shrink-0">
                  <AvatarImage src={dev.avatar} />
                  <AvatarFallback className="text-sm">{dev.name[0]}</AvatarFallback>
                </Avatar>
                <div className="flex-1 min-w-0">
                  <CardTitle className="text-base sm:text-lg truncate">{dev.name}</CardTitle>
                  <CardDescription className="text-sm">{dev.role}</CardDescription>
                  <div className="flex flex-col sm:flex-row sm:items-center gap-1 sm:gap-4 mt-1 sm:mt-2 text-xs sm:text-sm text-gray-600">
                    <span>경력 {dev.experience}</span>
                    <div className="flex items-center space-x-1">
                      <MapPin className="h-3 w-3" />
                      <span>{dev.location}</span>
                    </div>
                  </div>
                </div>
              </div>
            </CardHeader>
            <CardContent className="pt-0">
              <div className="space-y-3 sm:space-y-4">
                <p className="text-xs sm:text-sm text-gray-600 leading-relaxed">{dev.bio}</p>
                
                <div className="flex flex-wrap gap-1.5 sm:gap-2">
                  {dev.skills.slice(0, 3).map((skill) => (
                    <Badge key={skill} variant="outline" className="text-xs">{skill}</Badge>
                  ))}
                  {dev.skills.length > 3 && (
                    <Badge variant="secondary" className="text-xs">+{dev.skills.length - 3}</Badge>
                  )}
                </div>

                <div className="flex items-center justify-between text-xs sm:text-sm">
                  <div className="flex items-center space-x-1">
                    <Star className="h-3 w-3 sm:h-4 sm:w-4 text-yellow-400 fill-current" />
                    <span>{dev.rating}</span>
                  </div>
                  <span>{dev.projects}개 프로젝트 완료</span>
                </div>

                <Button 
                  className="w-full" 
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