import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Badge } from "@/components/ui/badge"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Code2, User, Sparkles } from "lucide-react"
import type { Developer } from "./DeveloperSection"

interface DeveloperCardProps {
  developer: Developer
  onClick?: (developerId: number) => void
  className?: string
}

// ProfileSetup에서 가져온 기술스택 데이터
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

// 기술스택 색상 매핑 (카테고리별)
const getTechStackColor = (tech: string): string => {
  if (SUGGESTED_SKILLS.frontend.includes(tech)) return 'bg-blue-100 text-blue-700'
  if (SUGGESTED_SKILLS.backend.includes(tech)) return 'bg-green-100 text-green-700'
  if (SUGGESTED_SKILLS.database.includes(tech)) return 'bg-purple-100 text-purple-700'
  if (SUGGESTED_SKILLS.devops.includes(tech)) return 'bg-orange-100 text-orange-700'
  if (SUGGESTED_SKILLS.mobile.includes(tech)) return 'bg-pink-100 text-pink-700'
  if (SUGGESTED_SKILLS.ai.includes(tech)) return 'bg-indigo-100 text-indigo-700'
  return 'bg-gray-100 text-gray-700' // 기본 색상
}

export default function DeveloperCard({ 
  developer, 
  onClick,
  className = ""
}: DeveloperCardProps) {
  // 기술스택을 문자열 배열로 변환 (숙련도  제거)
  const techStackNames = developer.techStack?.map(tech => 
    typeof tech === 'string' ? tech : tech.name
  ) || []

  return (
    <Card className={`hover:shadow-lg transition-all duration-300 bg-gradient-to-br from-white to-slate-50/50 border-l-4 h-full flex flex-col ${
      developer.isMajor ? 'border-l-slate-400' : 'border-l-emerald-400'
    } ${className}`}>
      <CardHeader className="pb-3 relative">
        <div className="absolute top-3 right-3">
          <div className={`p-1.5 rounded-full ${
            developer.isMajor ? 'bg-slate-100' : 'bg-emerald-50'
          }`}>
            <Code2 className={`h-3 w-3 ${
              developer.isMajor ? 'text-slate-600' : 'text-emerald-600'
            }`} />
          </div>
        </div>
        <div className="flex items-start gap-3 pr-10">
          <div className="relative">
            <Avatar className="h-10 w-10 ring-2 ring-white shadow-sm">
              <AvatarImage src={developer.avatar} />
              <AvatarFallback className={`text-sm font-semibold ${
                developer.isMajor ? 'bg-slate-500 text-white' : 'bg-emerald-500 text-white'
              }`}>
                {developer.name[0]}
              </AvatarFallback>
            </Avatar>
            <div className={`absolute -bottom-1 -right-1 w-4 h-4 rounded-full border-2 border-white ${
              developer.isMajor ? 'bg-slate-500' : 'bg-emerald-500'
            } flex items-center justify-center`}>
              <Sparkles className="h-2 w-2 text-white" />
            </div>
          </div>
          <div className="flex-1">
            <CardTitle className="text-lg leading-tight font-bold">{developer.name}</CardTitle>
            <div className="flex items-center gap-1 mt-1">
              <User className="h-3 w-3 text-gray-400" />
              <p className="text-xs text-gray-500">
                {developer.positions?.[0] || developer.role || '프론트엔드'}
              </p>
            </div>
            <Badge variant="secondary" className={`text-xs mt-2 ${
              developer.isMajor ? 'bg-slate-100 text-slate-700' : 'bg-emerald-50 text-emerald-700'
            }`}>
              {developer.isMajor ? '💻 전공자' : '🚀 비전공자'}
            </Badge>
          </div>
        </div>
      </CardHeader>
      <CardContent className="pt-0 flex-1 flex flex-col">
        <div className="flex flex-col gap-4 flex-1">
          {/* 기술스택 섹션 - 더 시각적으로 */}
          <div className="bg-gradient-to-r from-gray-50 to-gray-100/50 rounded-lg p-3">
            <div className="flex items-center gap-2 mb-2">
              <Code2 className="h-4 w-4 text-gray-600" />
              <span className="text-xs font-semibold text-gray-700">기술 스택</span>
            </div>
            <div className="flex flex-wrap gap-1.5">
              {techStackNames.length > 0 ? (
                techStackNames.slice(0, 5).map((tech, index) => (
                  <Badge key={index} variant="outline" className={`text-xs border-0 shadow-sm hover:shadow-md transition-shadow ${getTechStackColor(tech)}`}>
                    {tech}
                  </Badge>
                ))
              ) : (
                <span className="text-xs text-gray-400 italic">기술스택 정보를 기다리고 있어요 ✨</span>
              )}
              {techStackNames.length > 5 && (
                <Badge variant="outline" className="text-xs bg-gray-100 text-gray-600 border-gray-300">
                  +{techStackNames.length - 5}
                </Badge>
              )}
            </div>
          </div>

          {/* 정보 그리드 - 더 구조적으로 */}
          <div className="grid grid-cols-2 gap-3 text-xs">
            <div className="bg-white rounded-lg p-2 border border-gray-100">
              <div className="text-gray-500 mb-1">주 포지션</div>
              <div className="font-medium text-gray-800">
                {developer.positions?.[0] || developer.role || '프론트엔드'}
              </div>
            </div>
            <div className="bg-white rounded-lg p-2 border border-gray-100">
              <div className="text-gray-500 mb-1">부포지션</div>
              <div className="font-medium text-gray-800">
                {developer.positions?.[1] || '-'}
              </div>
            </div>
          </div>
        </div>
        {/* 하단 - 성향과 버튼 (고정) */}
        <div className="mt-auto pt-3 border-t border-gray-100 flex items-center justify-between">
          <div className="flex flex-wrap gap-1.5">
            <span className="text-xs text-gray-500 mr-1">💡</span>
            {developer.projectPreferences && developer.projectPreferences.length > 0 ? (
              developer.projectPreferences.slice(0, 2).map((pref) => (
                <Badge
                  key={pref}
                  variant="secondary"
                  className={`text-xs font-medium ${
                    developer.isMajor ? 'bg-slate-50 text-slate-700' : 'bg-emerald-50 text-emerald-700'
                  }`}
                >
                  {pref}
                </Badge>
              ))
            ) : (
              <>
                <Badge variant="secondary" className={`text-xs font-medium ${
                  developer.isMajor ? 'bg-slate-50 text-slate-700' : 'bg-emerald-50 text-emerald-700'
                }`}>
                  취업중심
                </Badge>
                <Badge variant="secondary" className={`text-xs font-medium ${
                  developer.isMajor ? 'bg-slate-50 text-slate-700' : 'bg-emerald-50 text-emerald-700'
                }`}>
                  학습열정
                </Badge>
              </>
            )}
          </div>
          <Button 
            size="sm" 
            className={`text-xs px-4 h-8 ${
              developer.isMajor 
                ? 'bg-slate-600 hover:bg-slate-700' 
                : 'bg-emerald-600 hover:bg-emerald-700'
            }`}
            onClick={(e) => {
              e.stopPropagation()
              onClick?.(developer.id)
            }}
          >
            프로필 보기
          </Button>
        </div>
      </CardContent>
    </Card>
  )
}
