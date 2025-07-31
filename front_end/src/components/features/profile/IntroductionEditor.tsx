import { useState, useEffect } from 'react'
import { Textarea } from '@/components/ui/textarea'
import { Progress } from '@/components/ui/progress'
import { AlertCircle, CheckCircle } from 'lucide-react'

interface IntroductionEditorProps {
  value: string
  onChange: (value: string) => void
  maxLength?: number
}

export default function IntroductionEditor({ 
  value, 
  onChange, 
  maxLength = 500 
}: IntroductionEditorProps) {
  const [charCount, setCharCount] = useState(0)
  const [progress, setProgress] = useState(0)

  // 글자 수 및 진행률 업데이트
  useEffect(() => {
    const count = value.length
    setCharCount(count)
    setProgress((count / maxLength) * 100)
  }, [value, maxLength])

  // 글자 수 상태에 따른 색상
  const getStatusColor = () => {
    const ratio = charCount / maxLength
    if (ratio > 0.9) return 'text-red-500'
    if (ratio > 0.7) return 'text-yellow-500'
    return 'text-green-500'
  }

  // 글자 수 제한 처리
  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const newValue = e.target.value
    if (newValue.length <= maxLength) {
      onChange(newValue)
    }
  }

  // 자기소개 작성 팁
  const introductionTips = [
    '본인의 경력과 주요 프로젝트 경험을 간략히 소개해보세요.',
    '기술적 강점과 관심 분야를 언급하면 좋습니다.',
    '협업 스타일이나 개발 철학을 공유해보세요.',
    '성장 목표나 배우고 싶은 기술에 대해 언급해도 좋습니다.',
    '간결하고 명확하게 작성하는 것이 효과적입니다.'
  ]

  return (
    <div className="space-y-4">
      {/* 텍스트 에디터 */}
      <Textarea
        placeholder="자기소개를 작성해주세요..."
        value={value}
        onChange={handleChange}
        className="min-h-[200px] resize-y"
      />

      {/* 글자 수 표시 및 진행률 */}
      <div className="space-y-2">
        <div className="flex justify-between items-center">
          <span className={`text-sm font-medium ${getStatusColor()}`}>
            {charCount}/{maxLength} 자
          </span>
          {charCount > 0 && (
            <span className="text-sm flex items-center gap-1">
              {charCount >= maxLength ? (
                <>
                  <AlertCircle className="h-4 w-4 text-red-500" />
                  <span className="text-red-500">최대 글자 수에 도달했습니다</span>
                </>
              ) : charCount >= 100 ? (
                <>
                  <CheckCircle className="h-4 w-4 text-green-500" />
                  <span className="text-green-500">좋은 길이의 소개글입니다</span>
                </>
              ) : (
                <>
                  <AlertCircle className="h-4 w-4 text-yellow-500" />
                  <span className="text-yellow-500">조금 더 작성해보세요</span>
                </>
              )}
            </span>
          )}
        </div>
        <Progress value={progress} className="h-1" />
      </div>

      {/* 작성 팁 */}
      <div className="bg-purple-50 p-4 rounded-md">
        <h3 className="text-sm font-medium text-purple-800 mb-2">자기소개 작성 팁</h3>
        <ul className="list-disc pl-5 space-y-1">
          {introductionTips.map((tip, index) => (
            <li key={index} className="text-sm text-purple-700">
              {tip}
            </li>
          ))}
        </ul>
      </div>

      {/* 미리보기 */}
      {value.length > 0 && (
        <div className="border rounded-md p-4 bg-white">
          <h3 className="text-sm font-medium text-gray-700 mb-2">미리보기</h3>
          <div className="text-sm text-gray-600 whitespace-pre-wrap">
            {value}
          </div>
        </div>
      )}
    </div>
  )
}