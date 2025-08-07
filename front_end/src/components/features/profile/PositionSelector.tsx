import { Badge } from '@/components/ui/badge'
import { X } from 'lucide-react'

interface PositionSelectorProps {
  selectedPositions: string[]
  onChange: (positions: string[]) => void
}

// 추천 포지션 목록
const SUGGESTED_POSITIONS = [
  '백엔드',
  '프론트엔드',
  'AI',
  '디자인',
  'PM',
]

export default function PositionSelector({ selectedPositions, onChange }: PositionSelectorProps) {

  // 포지션 추가
  const addPosition = (position: string) => {
    if (!position.trim()) return
    if (!selectedPositions.includes(position) && selectedPositions.length < 5) {
      onChange([...selectedPositions, position])
    }
  }

  // 포지션 제거
  const removePosition = (position: string) => {
    onChange(selectedPositions.filter(p => p !== position))
  }

  // 추천 포지션 필터링 (이미 선택된 항목 제외)
  const filteredSuggestions = SUGGESTED_POSITIONS.filter(
    position => !selectedPositions.includes(position)
  )

  return (
    <div className="space-y-5 sm:space-y-6">
      {/* 선택된 포지션 표시 */}
      <div className="flex flex-wrap gap-1.5 sm:gap-2 min-h-10 sm:min-h-12">
        {selectedPositions.map(position => (
          <Badge 
            key={position}
            className="px-2 sm:px-3 py-1 sm:py-1.5 text-xs sm:text-sm bg-green-100 text-green-800 hover:bg-green-200"
          >
            {position}
            <button 
              className="ml-1 sm:ml-2 text-green-700 hover:text-green-900 p-0.5 touch-manipulation"
              onClick={() => removePosition(position)}
              aria-label={`${position} 삭제`}
            >
              <X className="h-2.5 w-2.5 sm:h-3 sm:w-3" />
            </button>
          </Badge>
        ))}
        {selectedPositions.length === 0 && (
          <p className="text-xs sm:text-sm text-gray-500">선택된 포지션이 없습니다. 아래에서 선택해주세요.</p>
        )}
      </div>

      {/* 추천 포지션 */}
      <div>
        <h3 className="text-xs sm:text-sm font-medium text-gray-700 mb-1.5 sm:mb-2">추천 포지션</h3>
        <div className="flex flex-wrap gap-1.5 sm:gap-2">
          {filteredSuggestions.map(position => (
            <Badge 
              key={position}
              variant="outline" 
              className="px-2 sm:px-3 py-1 sm:py-1.5 text-xs sm:text-sm cursor-pointer hover:bg-green-50 touch-manipulation"
              onClick={() => addPosition(position)}
            >
              {position}
            </Badge>
          ))}
        </div>
      </div>

      {/* 입력된 포지션 설명 */}
      <div className="bg-green-50 p-3 sm:p-4 rounded-md">
        <h3 className="text-xs sm:text-sm font-medium text-green-800 mb-1 sm:mb-2">포지션 선택 팁</h3>
        <p className="text-xs sm:text-sm text-green-700">
          여러 포지션을 선택할 수 있으며, 주요 포지션을 먼저 입력하는 것이 좋습니다.
          본인의 전문 분야나 관심 있는 역할을 자유롭게 추가해보세요.
        </p>
      </div>
    </div>
  )
}