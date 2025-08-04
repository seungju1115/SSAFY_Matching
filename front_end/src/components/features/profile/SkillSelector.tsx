import { useState } from 'react'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { X, Plus } from 'lucide-react'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'

interface SkillSelectorProps {
  selectedSkills: string[]
  onChange: (skills: string[]) => void
}

// 카테고리별 추천 기술스택
const SUGGESTED_SKILLS = {
  frontend: [
    'React', 'Vue.js', 'Angular', 'Next.js', 'TypeScript', 'JavaScript',
    'HTML', 'CSS', 'SCSS', 'Tailwind CSS', 'Redux', 'Zustand'
  ],
  backend: [
    'Node.js', 'Express', 'Spring', 'Django', 'Flask', 'NestJS',
    'Java', 'Python', 'C#', 'Go', 'Ruby on Rails', 'PHP'
  ],
  database: [
    'MySQL', 'PostgreSQL', 'MongoDB', 'Redis', 'Firebase',
    'Oracle', 'SQL Server', 'DynamoDB', 'Elasticsearch'
  ],
  devops: [
    'Docker', 'Kubernetes', 'AWS', 'Azure', 'GCP', 'Jenkins',
    'GitHub Actions', 'Terraform', 'Ansible', 'Prometheus'
  ],
  mobile: [
    'React Native', 'Flutter', 'Swift', 'Kotlin', 'Android',
    'iOS', 'Xamarin', 'Ionic'
  ],
  ai: [
    'TensorFlow', 'PyTorch', 'Scikit-learn', 'OpenCV', 'NLP',
    'Computer Vision', 'Machine Learning', 'Deep Learning'
  ]
}

// 카테고리 정의
const CATEGORIES = [
  { id: 'frontend', name: '프론트엔드' },
  { id: 'backend', name: '백엔드' },
  { id: 'database', name: '데이터베이스' },
  { id: 'devops', name: 'DevOps' },
  { id: 'mobile', name: '모바일' },
  { id: 'ai', name: 'AI/ML' }
]

type CategoryId = keyof typeof SUGGESTED_SKILLS

export default function SkillSelector({ selectedSkills, onChange }: SkillSelectorProps) {
  const [inputValue, setInputValue] = useState('')
  const [activeCategory, setActiveCategory] = useState<CategoryId>('frontend')

  // 스킬 추가
  const addSkill = (skill: string) => {
    if (!skill.trim()) return
    if (!selectedSkills.includes(skill)) {
      onChange([...selectedSkills, skill])
    }
    setInputValue('')
  }

  // 스킬 제거
  const removeSkill = (skill: string) => {
    onChange(selectedSkills.filter(s => s !== skill))
  }

  return (
    <div className="space-y-6">
      {/* 선택된 스킬 표시 */}
      <div className="flex flex-wrap gap-2 min-h-12">
        {selectedSkills.map(skill => (
          <Badge 
            key={skill}
            className="px-3 py-1.5 text-sm bg-blue-100 text-blue-800 hover:bg-blue-200"
          >
            {skill}
            <button 
              className="ml-2 text-blue-700 hover:text-blue-900"
              onClick={() => removeSkill(skill)}
            >
              <X className="h-3 w-3" />
            </button>
          </Badge>
        ))}
        {selectedSkills.length === 0 && (
          <p className="text-sm text-gray-500">선택된 기술스택이 없습니다. 아래에서 선택하거나 직접 입력해주세요.</p>
        )}
      </div>

      {/* 스킬 입력 */}
      <div className="flex gap-2">
        <Input
          placeholder="기술스택 입력 (예: React)"
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          className="flex-1"
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              e.preventDefault()
              addSkill(inputValue)
            }
          }}
        />
        <Button 
          onClick={() => addSkill(inputValue)}
          disabled={!inputValue.trim()}
          className="bg-blue-500 hover:bg-blue-600"
        >
          <Plus className="h-4 w-4" />
          추가
        </Button>
      </div>

      {/* 카테고리별 추천 스킬 */}
      <Tabs defaultValue="frontend" value={activeCategory} onValueChange={(value) => setActiveCategory(value as CategoryId)}>
        <TabsList className="grid grid-cols-3 md:grid-cols-6 w-full">
          {CATEGORIES.map(category => (
            <TabsTrigger key={category.id} value={category.id}>
              {category.name}
            </TabsTrigger>
          ))}
        </TabsList>
        
        {Object.keys(SUGGESTED_SKILLS).map((categoryId) => (
          <TabsContent key={categoryId} value={categoryId} className="pt-4">
            <div className="flex flex-wrap gap-2">
              {SUGGESTED_SKILLS[categoryId as CategoryId]
                .filter(skill => !selectedSkills.includes(skill))
                .map(skill => (
                  <Badge 
                    key={skill}
                    variant="outline" 
                    className="px-3 py-1.5 text-sm cursor-pointer hover:bg-blue-50"
                    onClick={() => addSkill(skill)}
                  >
                    {skill}
                  </Badge>
                ))
              }
            </div>
          </TabsContent>
        ))}
      </Tabs>

      {/* 입력된 스킬 설명 */}
      <div className="bg-blue-50 p-4 rounded-md">
        <h3 className="text-sm font-medium text-blue-800 mb-2">기술스택 선택 팁</h3>
        <p className="text-sm text-blue-700">
          여러 기술을 선택할 수 있으며, 숙련도가 높은 기술을 먼저 입력하는 것이 좋습니다.
          본인이 사용해본 기술이나 관심 있는 기술을 자유롭게 추가해보세요.
        </p>
      </div>
    </div>
  )
}