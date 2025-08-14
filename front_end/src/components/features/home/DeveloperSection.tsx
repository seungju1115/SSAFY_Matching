import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Plus, Filter } from "lucide-react"
import DeveloperCard from "./DeveloperCard"
import { useUser } from "@/hooks/useUser"
import type { UserSearchResponse } from "@/types/user"

interface DeveloperSectionProps {
  onRegister?: () => void
  onViewAll?: () => void
  onFilter?: () => void
  onViewProfile?: (developerId: number) => void
}

export interface Developer {
  id: number
  name: string
  role: string
  avatar?: string
  domain?: string
  isMajor?: boolean
  projectPreferences?: string[]
  personalPreferences?: string[]
  positions?: string[]
  techStack?: { name: string; level: number }[]
}

export default function DeveloperSection({ 
  onRegister, 
  onViewAll,
  onFilter,
  onViewProfile,
}: DeveloperSectionProps) {
  const [developers, setDevelopers] = useState<UserSearchResponse[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const { getWaitingUsers } = useUser()

  useEffect(() => {
    const loadDevelopers = async () => {
      try {
        setIsLoading(true)
        const response = await getWaitingUsers()
        
        // 최대 6명으로 제한 (TeamSection과 유사하게)
        const waitingUsers = response.slice(0, 6)
        setDevelopers(waitingUsers)
      } catch (error) {
        console.error('대기중인 사용자 데이터 로딩 실패:', error)
      } finally {
        setIsLoading(false)
      }
    }

    loadDevelopers()
  }, [])

  if (isLoading) {
    return (
      <section className="mb-12 sm:mb-16">
        <div className="flex justify-center items-center h-32">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
        </div>
      </section>
    )
  }

  return (
    <section className="mb-12 sm:mb-16">
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
          <Button variant="outline" className="w-full sm:w-auto" onClick={onFilter}>
            <Filter className="mr-2 h-4 w-4" />
            필터
          </Button>
          <Button variant="outline" className="w-full sm:w-auto" onClick={onViewAll}>
            전체보기
          </Button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 sm:gap-6">
        {developers.map((dev) => (
          <DeveloperCard 
            key={dev.id}
            developer={dev}
            onClick={(developerId) => onViewProfile?.(developerId)}
          />
        ))}
      </div>
    </section>
  )
}

// 타입 export
export type { DeveloperSectionProps }