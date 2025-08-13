import { Button } from "@/components/ui/button"
import { Plus, Filter } from "lucide-react"
import DeveloperCard from "./DeveloperCard"

interface Developer {
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

interface DeveloperSectionProps {
  developers: Developer[]
  onRegister?: () => void
  onViewAll?: () => void
}

export default function DeveloperSection({ 
  developers, 
  onRegister, 
  onViewAll 
}: DeveloperSectionProps) {
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
          <Button variant="outline" className="w-full sm:w-auto">
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
            onClick={(developerId) => console.log('개발자 프로필 보기:', developerId)}
          />
        ))}
      </div>
    </section>
  )
}

// 타입 export
export type { Developer, DeveloperSectionProps }