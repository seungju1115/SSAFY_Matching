import { useState } from 'react'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import { Search, Filter, ChevronDown } from 'lucide-react'
import type { Developer } from './DeveloperSection'
import DeveloperCard from './DeveloperCard'

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
  onViewProfile,
}: DevelopersModalProps) {
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedSkills, setSelectedSkills] = useState<string[]>([])
  const [selectedRole, setSelectedRole] = useState<string>('')
  const [isFilterOpen, setIsFilterOpen] = useState(false)

  // 모든 포지션과 역할 추출
  const allPositions = Array.from(new Set(developers.flatMap(dev => dev.positions || [dev.role])))
  const allRoles = Array.from(new Set(developers.map(dev => dev.role)))

  // 필터링된 개발자 목록
  const filteredDevelopers = developers.filter(dev => {
    const matchesSearch = dev.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         dev.role.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         (dev.positions?.some(pos => pos.toLowerCase().includes(searchTerm.toLowerCase())) || false)
    
    const matchesSkills = selectedSkills.length === 0 || 
                         selectedSkills.some(skill => dev.positions?.includes(skill) || dev.role === skill)
    
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

              {/* 포지션 필터 */}
              <div className="space-y-2">
                <p className="text-sm font-medium text-gray-700">포지션</p>
                <div className="flex flex-wrap gap-2">
                  {allPositions.slice(0, 12).map((position: string) => (
                    <Badge
                      key={position}
                      variant={selectedSkills.includes(position) ? "default" : "outline"}
                      className="cursor-pointer"
                      onClick={() => toggleSkill(position)}
                    >
                      {position}
                    </Badge>
                  ))}
                </div>
              </div>
            </div>
          )}
        </div>

        {/* 개발자 목록 */}
        <div className="flex-1 overflow-y-auto">
          <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-4 p-1">
            {filteredDevelopers.map((dev) => (
              <DeveloperCard 
                key={dev.id}
                developer={dev}
                onClick={(developerId) => onViewProfile?.(developerId)}
              />
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
