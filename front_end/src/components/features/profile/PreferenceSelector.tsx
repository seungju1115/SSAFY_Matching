import { Badge } from '@/components/ui/badge'
import { X } from 'lucide-react'

interface PreferenceSelectorProps {
  title: string
  description: string
  selectedPreferences: string[]
  onChange: (preferences: string[]) => void
  suggestions: string[]
  color: 'blue' | 'green' | 'purple' | 'orange' | 'pink'
}

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
  selectedPreferences, 
  onChange, 
  suggestions,
  color = 'blue'
}: PreferenceSelectorProps) {

  const styles = colorStyles[color]

  const addPreference = (preference: string) => {
    if (!preference.trim()) return
    if (!selectedPreferences.includes(preference)) {
      onChange([...selectedPreferences, preference])
    }
  }

  const removePreference = (preference: string) => {
    onChange(selectedPreferences.filter(p => p !== preference))
  }

  const filteredSuggestions = suggestions.filter(
    preference => !selectedPreferences.includes(preference)
  )

  return (
    <div className="space-y-6">
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
          <p className="text-sm text-gray-500">선택된 선호가 없습니다. 아래에서 선택해주세요.</p>
        )}
      </div>

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
        <h3 className="text-sm font-medium text-gray-700 mb-2">선택 팁</h3>
        <ul className="list-disc pl-5 space-y-1">
          <li className="text-sm text-gray-600">
            추후 AI 추천에서 사용될 데이터입니다.
          </li>
          <li className="text-sm text-gray-600">
            자신의 성향을 잘 나타내는 단어를 선택해주세요.
          </li>
        </ul>
      </div>
    </div>
  )
}