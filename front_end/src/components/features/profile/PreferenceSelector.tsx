import { useState } from 'react'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { X, Plus } from 'lucide-react'


interface PreferenceSelectorProps {
  title: string
  description: string
  selectedPreferences: string[]
  onChange: (preferences: string[]) => void
  suggestions: string[]
  color: 'blue' | 'green' | 'purple' | 'orange' | 'pink'
}

// 색상 설정
const colorStyles = {
  blue: {
    badge: 'bg-blue-100 text-blue-800 hover:bg-blue-200',
    button: 'text-blue-700 hover:text-blue-900',
    suggestion: 'hover:bg-blue-50',
    actionButton: 'bg-blue-500 hover:bg-blue-600',
    tipBg: 'bg-blue-50',
    tipTitle: 'text-blue-800',
    tipText: 'text-blue-700'
  },
  green: {
    badge: 'bg-green-100 text-green-800 hover:bg-green-200',
    button: 'text-green-700 hover:text-green-900',
    suggestion: 'hover:bg-green-50',
    actionButton: 'bg-green-500 hover:bg-green-600',
    tipBg: 'bg-green-50',
    tipTitle: 'text-green-800',
    tipText: 'text-green-700'
  },
  purple: {
    badge: 'bg-purple-100 text-purple-800 hover:bg-purple-200',
    button: 'text-purple-700 hover:text-purple-900',
    suggestion: 'hover:bg-purple-50',
    actionButton: 'bg-purple-500 hover:bg-purple-600',
    tipBg: 'bg-purple-50',
    tipTitle: 'text-purple-800',
    tipText: 'text-purple-700'
  },
  orange: {
    badge: 'bg-orange-100 text-orange-800 hover:bg-orange-200',
    button: 'text-orange-700 hover:text-orange-900',
    suggestion: 'hover:bg-orange-50',
    actionButton: 'bg-orange-500 hover:bg-orange-600',
    tipBg: 'bg-orange-50',
    tipTitle: 'text-orange-800',
    tipText: 'text-orange-700'
  },
  pink: {
    badge: 'bg-pink-100 text-pink-800 hover:bg-pink-200',
    button: 'text-pink-700 hover:text-pink-900',
    suggestion: 'hover:bg-pink-50',
    actionButton: 'bg-pink-500 hover:bg-pink-600',
    tipBg: 'bg-pink-50',
    tipTitle: 'text-pink-800',
    tipText: 'text-pink-700'
  }
}

export default function PreferenceSelector({ 
  title,
  description,
  selectedPreferences, 
  onChange, 
  suggestions,
  color = 'blue'
}: PreferenceSelectorProps) {
  const [inputValue, setInputValue] = useState('')
  const styles = colorStyles[color]

  // 선호도 추가
  const addPreference = (preference: string) => {
    if (!preference.trim()) return
    if (!selectedPreferences.includes(preference)) {
      onChange([...selectedPreferences, preference])
    }
    setInputValue('')
  }

  // 선호도 제거
  const removePreference = (preference: string) => {
    onChange(selectedPreferences.filter(p => p !== preference))
  }

  // 추천 항목 필터링 (이미 선택된 항목 제외)
  const filteredSuggestions = suggestions.filter(
    preference => !selectedPreferences.includes(preference) && 
    preference.toLowerCase().includes(inputValue.toLowerCase())
  )

  return (
    <div className="space-y-6">
      {/* 선택된 선호도 표시 */}
      <div className="flex flex-wrap gap-2 min-h-12">
        {selectedPreferences.map(preference => (
          <Badge 
            key={preference}
            className={`px-3 py-1.5 text-sm ${styles.badge}`}
          >
            {preference}
            <button 
              className={`ml-2 ${styles.button}`}
              onClick={() => removePreference(preference)}
            >
              <X className="h-3 w-3" />
            </button>
          </Badge>
        ))}
        {selectedPreferences.length === 0 && (
          <p className="text-sm text-gray-500">선택된 {title}이 없습니다. 아래에서 선택하거나 직접 입력해주세요.</p>
        )}
      </div>

      {/* 입력 */}
      <div className="flex gap-2">
        <Input
          placeholder={`${title} 입력`}
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          className="flex-1"
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              e.preventDefault()
              addPreference(inputValue)
            }
          }}
        />
        <Button 
          onClick={() => addPreference(inputValue)}
          disabled={!inputValue.trim()}
          className={styles.actionButton}
        >
          <Plus className="h-4 w-4" />
          추가
        </Button>
      </div>

      {/* 추천 항목 */}
      <div>
        <h3 className="text-sm font-medium text-gray-700 mb-2">추천 {title}</h3>
        <div className="flex flex-wrap gap-2">
          {filteredSuggestions.slice(0, 15).map(preference => (
            <Badge 
              key={preference}
              variant="outline" 
              className={`px-3 py-1.5 text-sm cursor-pointer ${styles.suggestion}`}
              onClick={() => addPreference(preference)}
            >
              {preference}
            </Badge>
          ))}
        </div>
      </div>

      {/* 설명 */}
      <div className={`${styles.tipBg} p-4 rounded-md`}>
        <h3 className={`text-sm font-medium ${styles.tipTitle} mb-2`}>{title} 선택 팁</h3>
        <p className={`text-sm ${styles.tipText}`}>
          {description}
        </p>
      </div>
    </div>
  )
}