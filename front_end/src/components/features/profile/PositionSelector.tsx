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
  // 주 포지션과 부 포지션 구분
  const mainPosition = selectedPositions.length > 0 ? selectedPositions[0] : null;
  const subPosition = selectedPositions.length > 1 ? selectedPositions[1] : null;

  // 포지션 추가
  const addPosition = (position: string) => {
    if (!position.trim()) return
    
    // 이미 선택된 포지션이면 무시
    if (selectedPositions.includes(position)) return
    
    // 주 포지션이 없으면 주 포지션으로 추가
    if (selectedPositions.length === 0) {
      onChange([position])
    }
    // 부 포지션이 없으면 부 포지션으로 추가
    else if (selectedPositions.length === 1) {
      onChange([selectedPositions[0], position])
    }
    // 이미 두 개가 선택되어 있으면 부 포지션만 교체
    else {
      onChange([selectedPositions[0], position])
    }
  }

  // 포지션 제거
  const removePosition = (position: string) => {
    // 주 포지션을 제거하면 부 포지션이 주 포지션으로 승격
    if (position === mainPosition && subPosition) {
      onChange([subPosition])
    } else {
      onChange(selectedPositions.filter(p => p !== position))
    }
  }

  // 추천 포지션 필터링 (이미 선택된 항목 제외)
  const filteredSuggestions = SUGGESTED_POSITIONS.filter(
    position => !selectedPositions.includes(position)
  )

  return (
    <div className="space-y-5 sm:space-y-6">
      {/* 선택된 포지션 표시 */}
      <div className="flex flex-col gap-3 sm:gap-4">
        {/* 주 포지션 */}
        <div>
          <h3 className="text-xs sm:text-sm font-medium text-gray-700 mb-1.5">주 포지션</h3>
          <div className="flex flex-wrap gap-1.5 sm:gap-2 min-h-10 sm:min-h-12">
            {mainPosition ? (
              <Badge 
                key={mainPosition}
                className="px-2 sm:px-3 py-1 sm:py-1.5 text-xs sm:text-sm bg-blue-100 text-blue-800 hover:bg-blue-200"
              >
                {mainPosition}
                <button 
                  className="ml-1 sm:ml-2 text-blue-700 hover:text-blue-900 p-0.5 touch-manipulation"
                  onClick={() => removePosition(mainPosition)}
                  aria-label={`${mainPosition} 삭제`}
                >
                  <X className="h-2.5 w-2.5 sm:h-3 sm:w-3" />
                </button>
              </Badge>
            ) : (
              <p className="text-xs sm:text-sm text-gray-500">주 포지션을 선택해주세요.</p>
            )}
          </div>
        </div>
        
        {/* 부 포지션 */}
        <div>
          <h3 className="text-xs sm:text-sm font-medium text-gray-700 mb-1.5">부 포지션</h3>
          <div className="flex flex-wrap gap-1.5 sm:gap-2 min-h-10 sm:min-h-12">
            {subPosition ? (
              <Badge 
                key={subPosition}
                className="px-2 sm:px-3 py-1 sm:py-1.5 text-xs sm:text-sm bg-green-100 text-green-800 hover:bg-green-200"
              >
                {subPosition}
                <button 
                  className="ml-1 sm:ml-2 text-green-700 hover:text-green-900 p-0.5 touch-manipulation"
                  onClick={() => removePosition(subPosition)}
                  aria-label={`${subPosition} 삭제`}
                >
                  <X className="h-2.5 w-2.5 sm:h-3 sm:w-3" />
                </button>
              </Badge>
            ) : (
              <p className="text-xs sm:text-sm text-gray-500">{mainPosition ? "부 포지션을 선택해주세요." : "주 포지션을 먼저 선택해주세요."}</p>
            )}
          </div>
        </div>
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
      <div className="bg-blue-50 p-3 sm:p-4 rounded-md">
        <h3 className="text-xs sm:text-sm font-medium text-blue-800 mb-1 sm:mb-2">포지션 선택 팁</h3>
        <p className="text-xs sm:text-sm text-blue-700">
          주 포지션은 가장 전문적인 분야를, 부 포지션은 보조 역할이나 관심 분야를 선택해주세요.
          주 포지션과 부 포지션을 각각 1개씩만 선택할 수 있습니다.
        </p>
      </div>
    </div>
  )
}