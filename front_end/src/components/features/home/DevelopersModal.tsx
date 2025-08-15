import { useState, useEffect } from 'react'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import { Search, Filter, ChevronDown } from 'lucide-react'
import type { UserSearchResponse } from '@/types/user'
import DeveloperCard from './DeveloperCard'
import { useUser } from '@/hooks/useUser'

interface DevelopersModalProps {
  isOpen: boolean
  onClose: () => void
  onViewProfile?: (developerId: number) => void
}

export default function DevelopersModal({ 
  isOpen, 
  onClose, 
  onViewProfile,
}: DevelopersModalProps) {
  const [developers, setDevelopers] = useState<UserSearchResponse[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedPositions, setSelectedPositions] = useState<string[]>([])
  const [selectedTechs, setSelectedTechs] = useState<string[]>([])
  const [isFilterOpen, setIsFilterOpen] = useState(false)
  const { getWaitingUsers } = useUser()

  // 모달이 열릴 때 데이터 로드
  useEffect(() => {
    const loadDevelopers = async () => {
      if (!isOpen) return

      try {
        setIsLoading(true)
        const response = await getWaitingUsers()
        setDevelopers(response)
      } catch (error) {
        console.error('대기중인 사용자 데이터 로딩 실패:', error)
      } finally {
        setIsLoading(false)
      }
    }

    loadDevelopers()
  }, [isOpen])

  // 모든 포지션과 기술스택 추출
  const allPositions = Array.from(new Set(developers.flatMap(dev => dev.wantedPosition || [])))
  const allTechs = Array.from(new Set(developers.flatMap(dev => dev.techStack || [])))

  // 필터링된 개발자 목록
  const filteredDevelopers = developers.filter(dev => {
    const matchesSearch = dev.userName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         (dev.wantedPosition?.[0]?.toLowerCase().includes(searchTerm.toLowerCase()) || false) ||
                         (dev.wantedPosition?.some(pos => pos.toLowerCase().includes(searchTerm.toLowerCase())) || false) ||
                         (dev.techStack?.some(tech => tech.toLowerCase().includes(searchTerm.toLowerCase())) || false)
    
    const matchesPositions = selectedPositions.length === 0 || 
                           selectedPositions.some(position => dev.wantedPosition?.includes(position))
    
    const matchesTechs = selectedTechs.length === 0 || 
                        selectedTechs.some(tech => dev.techStack?.includes(tech))
    
    return matchesSearch && matchesPositions && matchesTechs
  })

  const togglePosition = (position: string) => {
    setSelectedPositions(prev => 
      prev.includes(position) 
        ? prev.filter(p => p !== position)
        : [...prev, position]
    )
  }

  const toggleTech = (tech: string) => {
    setSelectedTechs(prev => 
      prev.includes(tech) 
        ? prev.filter(t => t !== tech)
        : [...prev, tech]
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
              {/* 포지션 필터 */}
              <div className="space-y-2">
                <p className="text-sm font-medium text-gray-700">포지션</p>
                <div className="flex flex-wrap gap-2">
                  {allPositions.slice(0, 10).map((position: string) => (
                    <Badge
                      key={position}
                      variant={selectedPositions.includes(position) ? "default" : "outline"}
                      className="cursor-pointer"
                      onClick={() => togglePosition(position)}
                    >
                      {position}
                    </Badge>
                  ))}
                </div>
              </div>

              {/* 기술스택 필터 */}
              <div className="space-y-2">
                <p className="text-sm font-medium text-gray-700">기술 스택</p>
                <div className="flex flex-wrap gap-2">
                  {allTechs.slice(0, 15).map((tech: string) => (
                    <Badge
                      key={tech}
                      variant={selectedTechs.includes(tech) ? "default" : "outline"}
                      className="cursor-pointer"
                      onClick={() => toggleTech(tech)}
                    >
                      {tech}
                    </Badge>
                  ))}
                </div>
              </div>
            </div>
          )}
        </div>

        {/* 개발자 목록 */}
        <div className="flex-1 overflow-y-auto">
          {isLoading ? (
            <div className="flex justify-center items-center h-32">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            </div>
          ) : (
            <>
              <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-4 p-1">
                {filteredDevelopers.map((dev) => (
                  <DeveloperCard 
                    key={dev.id}
                    developer={dev}
                    onClick={(developerId) => onViewProfile?.(developerId)}
                  />
                ))}
              </div>

              {!isLoading && filteredDevelopers.length === 0 && (
                <div className="text-center py-12">
                  <p className="text-gray-500">
                    {developers.length === 0 ? '대기중인 개발자가 없습니다.' : '검색 조건에 맞는 개발자가 없습니다.'}
                  </p>
                </div>
              )}
            </>
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
