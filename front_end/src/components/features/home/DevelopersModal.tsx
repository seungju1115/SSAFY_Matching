import { useState } from 'react'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import { Card, CardContent } from '@/components/ui/card'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Star, MapPin, Search, Filter, ChevronDown } from 'lucide-react'
import type { Developer } from './DeveloperSection'

interface DevelopersModalProps {
  isOpen: boolean
  onClose: () => void
  developers: Developer[]
  onViewProfile?: (developerId: number) => void
}

export default function DevelopersModal({ 
  isOpen, 
  onClose, 
  developers, 
  onViewProfile 
}: DevelopersModalProps) {
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedSkills, setSelectedSkills] = useState<string[]>([])
  const [selectedRole, setSelectedRole] = useState<string>('')
  const [isFilterOpen, setIsFilterOpen] = useState(false)

  // 모든 스킬과 역할 추출
  const allSkills = Array.from(new Set(developers.flatMap(dev => dev.skills)))
  const allRoles = Array.from(new Set(developers.map(dev => dev.role)))

  // 필터링된 개발자 목록
  const filteredDevelopers = developers.filter(dev => {
    const matchesSearch = dev.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         dev.role.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         dev.skills.some(skill => skill.toLowerCase().includes(searchTerm.toLowerCase()))
    const matchesSkills = selectedSkills.length === 0 || 
                         selectedSkills.some(skill => dev.skills.includes(skill))
    const matchesRole = !selectedRole || dev.role === selectedRole
    return matchesSearch && matchesSkills && matchesRole
  })

  const toggleSkill = (skill: string) => {
    setSelectedSkills(prev => 
      prev.includes(skill) 
        ? prev.filter(s => s !== skill)
        : [...prev, skill]
    )
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-7xl max-h-[90vh] overflow-hidden flex flex-col">
        <DialogHeader className="flex-shrink-0">
          <div className="flex items-center justify-between">
            <DialogTitle className="text-2xl font-bold">대기자 전체보기</DialogTitle>
          </div>
        </DialogHeader>

        {/* 검색 및 필터 */}
        <div className="flex-shrink-0 space-y-4 pb-4 border-b">
          <div className="flex gap-3">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
              <Input
                placeholder="개발자 이름, 역할, 기술스택으로 검색..."
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

          {/* 필터 옵션 */}
          {isFilterOpen && (
            <div className="space-y-4">
              {/* 역할 필터 */}
              <div className="space-y-2">
                <p className="text-sm font-medium text-gray-700">역할</p>
                <div className="flex flex-wrap gap-2">
                  <Badge
                    variant={!selectedRole ? "default" : "outline"}
                    className="cursor-pointer"
                    onClick={() => setSelectedRole('')}
                  >
                    전체
                  </Badge>
                  {allRoles.map(role => (
                    <Badge
                      key={role}
                      variant={selectedRole === role ? "default" : "outline"}
                      className="cursor-pointer"
                      onClick={() => setSelectedRole(role)}
                    >
                      {role}
                    </Badge>
                  ))}
                </div>
              </div>

              {/* 기술 스택 필터 */}
              <div className="space-y-2">
                <p className="text-sm font-medium text-gray-700">기술 스택</p>
                <div className="flex flex-wrap gap-2">
                  {allSkills.slice(0, 12).map(skill => (
                    <Badge
                      key={skill}
                      variant={selectedSkills.includes(skill) ? "default" : "outline"}
                      className="cursor-pointer"
                      onClick={() => toggleSkill(skill)}
                    >
                      {skill}
                    </Badge>
                  ))}
                </div>
              </div>
            </div>
          )}
        </div>

        {/* 개발자 목록 */}
        <div className="flex-1 overflow-y-auto">
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-3 p-1">
            {filteredDevelopers.map((dev) => (
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

          {filteredDevelopers.length === 0 && (
            <div className="text-center py-12">
              <p className="text-gray-500">검색 조건에 맞는 개발자가 없습니다.</p>
            </div>
          )}
        </div>

        {/* 하단 정보 */}
        <div className="flex-shrink-0 pt-4 border-t">
          <p className="text-sm text-gray-600 text-center">
            총 {filteredDevelopers.length}명의 개발자가 있습니다.
          </p>
        </div>
      </DialogContent>
    </Dialog>
  )
}
