import { useState, useEffect } from 'react'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import { Search, Filter, ChevronDown } from 'lucide-react'
import type { Developer } from './DeveloperSection'
import type { UserSearchResponse } from '@/types/user'
import { userAPI } from '@/api/user'
import { useEnumMapper } from '@/hooks/useEnumMapper'
import DeveloperCard from './DeveloperCard'

interface DevelopersModalProps {
  isOpen: boolean
  onClose: () => void
  onViewProfile?: (developerId: number) => void
  // 프로필 모달 열림 여부 (중첩 모달 시 ESC/바깥 클릭 무시용)
  isProfileOpen?: boolean
}

export default function DevelopersModal({ 
  isOpen, 
  onClose, 
  onViewProfile,
  isProfileOpen = false
}: DevelopersModalProps) {
  const [developers, setDevelopers] = useState<Developer[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedSkills, setSelectedSkills] = useState<string[]>([])
  const [selectedRole, setSelectedRole] = useState<string>('')
  const [isFilterOpen, setIsFilterOpen] = useState(false)
  const { mapTechStackArray, mapPositionArray, mapProjectGoalArray } = useEnumMapper()

  // 대기중인 사용자 목록 로드
  useEffect(() => {
    const loadWaitingUsers = async () => {
      if (!isOpen) return
      
      setIsLoading(true)
      try {
        const response = await userAPI.getWaitingUsers()
        const userData = response.data.data
        
        // UserSearchResponse[]를 Developer[]로 변환
        const convertedDevelopers: Developer[] = userData.map((user: UserSearchResponse) => ({
          id: user.id,
          name: user.userName,
          role: user.wantedPosition?.[0] ? mapPositionArray(user.wantedPosition as any)[0] : '미정',
          positions: mapPositionArray(user.wantedPosition as any),
          // 카드 컴포넌트에서 기대하는 형태({ name, level })로 변환
          techStack: mapTechStackArray(user.techStack as any).map((name: string) => ({ name, level: 3 })),
          // 카드 하단 배지와 기본값(취업중심/학습열정)에 맞춰 ProjectGoal을 표시
          projectPreferences: mapProjectGoalArray(user.projectGoal as any),
          experience: user.projectExp || 0,
          isMajor: user.major,
          avatar: '' // 아바타 정보가 없으므로 빈 문자열
        }))
        
        setDevelopers(convertedDevelopers)
      } catch (error) {
        console.error('대기중인 사용자 목록 로드 실패:', error)
        setDevelopers([]) // 오류 시 빈 배열로 설정
      } finally {
        setIsLoading(false)
      }
    }

    loadWaitingUsers()
  }, [isOpen]) // 매핑 함수는 안정적이므로 의존성에서 제거

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
    <Dialog open={isOpen} onOpenChange={(open: boolean) => {
      // 프로필 모달이 열려있을 때는 DevelopersModal을 닫지 않음
      if (!open && isProfileOpen) return
      onClose()
    }} modal={false}>
      <DialogContent overlayClassName={isProfileOpen ? 'pointer-events-none' : undefined} className={`max-w-7xl h-[90vh] flex flex-col`}>
        <div className="flex flex-col h-full min-h-0 overflow-hidden pointer-events-auto">
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
        <div className="flex-1 min-h-0 overflow-y-auto">
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

              {filteredDevelopers.length === 0 && (
                <div className="text-center py-12">
                  <p className="text-gray-500">검색 조건에 맞는 개발자가 없습니다.</p>
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
        </div>
      </DialogContent>
    </Dialog>
  )
}
