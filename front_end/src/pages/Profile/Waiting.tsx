import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Separator } from '@/components/ui/separator'
import { 
  Users, 
  User,
  Code,
  Briefcase,
  Target,
  Heart,
  Award,
  Edit3
} from 'lucide-react'
import { useNavigate } from 'react-router-dom'

// 대기자 등록 정보 페이지 - ProfileSetup 디자인 적용
export default function ProfileWaiting() {
  const navigate = useNavigate()
  // 임시 대기자 등록 데이터 (실제로는 API에서 가져와야 함)
  const profileData = {
    positions: ['프론트엔드', '백엔드'],
    skills: ['React', 'TypeScript', 'Node.js', 'MySQL'],
    introduction: '안녕하세요! 열정적으로 개발을 배우고 있는 개발자입니다. 팀원들과 함께 성장하며 좋은 프로젝트를 만들고 싶습니다.',
    projectPreferences: ['취업우선', '포트폴리오중심', '완성도추구'],
    personalPreferences: ['편한 분위기', '합의 중심', '새로운 주제'],
    certifications: [
      { id: '1', name: '정보처리기사' },
      { id: '2', name: 'SQLD' }
    ]
  }

  return (
    <div className="space-y-8">
      {/* 헤더 섹션 - ProfileSetup 스타일 */}
      <div className="text-center space-y-4">
        <div className="inline-flex items-center justify-center w-20 h-20 bg-gradient-to-br from-blue-500 to-purple-600 rounded-full shadow-lg">
          <Users className="w-10 h-10 text-white" />
        </div>
        <div>
          <h1 className="text-4xl font-bold bg-gradient-to-r from-gray-800 to-blue-600 bg-clip-text text-transparent mb-2">
            대기자 등록 정보
          </h1>
          <p className="text-lg text-gray-600 max-w-2xl mx-auto">
            현재 등록된 프로필 정보를 확인할 수 있습니다.
          </p>
        </div>
      </div>

      {/* 현재 프로필 정보 섹션 */}
      <div className="space-y-6">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-2xl font-bold text-gray-800">현재 등록된 프로필</h2>
          <Button variant="outline" size="sm" onClick={() => navigate('/profile-setup')}>
            <Edit3 className="h-4 w-4 mr-2" />
            수정
          </Button>
        </div>
        
        <div className="grid gap-6 md:grid-cols-2">
          {/* 포지션 카드 */}
          <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-blue-50/50 backdrop-blur-sm">
            <CardHeader className="pb-4">
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-gradient-to-r from-blue-500 to-indigo-600 rounded-lg">
                  <Briefcase className="w-5 h-5 text-white" />
                </div>
                <div>
                  <CardTitle className="text-xl font-bold bg-gradient-to-r from-gray-800 to-blue-700 bg-clip-text text-transparent">
                    희망 포지션
                  </CardTitle>
                </div>
              </div>
            </CardHeader>
            <Separator className="mx-6" />
            <CardContent className="pt-6">
              <div className="flex flex-wrap gap-2">
                {profileData.positions.map((position) => (
                  <Badge key={position} variant="secondary" className="bg-blue-100 text-blue-800 hover:bg-blue-200">
                    {position}
                  </Badge>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* 기술 스택 카드 */}
          <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-purple-50/50 backdrop-blur-sm">
            <CardHeader className="pb-4">
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-gradient-to-r from-purple-500 to-pink-600 rounded-lg">
                  <Code className="w-5 h-5 text-white" />
                </div>
                <div>
                  <CardTitle className="text-xl font-bold bg-gradient-to-r from-gray-800 to-purple-700 bg-clip-text text-transparent">
                    기술 스택
                  </CardTitle>
                </div>
              </div>
            </CardHeader>
            <Separator className="mx-6" />
            <CardContent className="pt-6">
              <div className="flex flex-wrap gap-2">
                {profileData.skills.map((skill) => (
                  <Badge key={skill} variant="secondary" className="bg-purple-100 text-purple-800 hover:bg-purple-200">
                    {skill}
                  </Badge>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* 자기소개 카드 */}
          <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-green-50/50 backdrop-blur-sm md:col-span-2">
            <CardHeader className="pb-4">
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-gradient-to-r from-green-500 to-emerald-600 rounded-lg">
                  <User className="w-5 h-5 text-white" />
                </div>
                <div>
                  <CardTitle className="text-xl font-bold bg-gradient-to-r from-gray-800 to-green-700 bg-clip-text text-transparent">
                    자기소개
                  </CardTitle>
                </div>
              </div>
            </CardHeader>
            <Separator className="mx-6" />
            <CardContent className="pt-6">
              <p className="text-gray-700 leading-relaxed">{profileData.introduction}</p>
            </CardContent>
          </Card>

          {/* 프로젝트 선호도 카드 */}
          <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-indigo-50/50 backdrop-blur-sm">
            <CardHeader className="pb-4">
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-gradient-to-r from-indigo-500 to-blue-600 rounded-lg">
                  <Target className="w-5 h-5 text-white" />
                </div>
                <div>
                  <CardTitle className="text-xl font-bold bg-gradient-to-r from-gray-800 to-indigo-700 bg-clip-text text-transparent">
                    프로젝트 선호도
                  </CardTitle>
                </div>
              </div>
            </CardHeader>
            <Separator className="mx-6" />
            <CardContent className="pt-6">
              <div className="flex flex-wrap gap-2">
                {profileData.projectPreferences.map((pref) => (
                  <Badge key={pref} variant="secondary" className="bg-indigo-100 text-indigo-800 hover:bg-indigo-200">
                    {pref}
                  </Badge>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* 개인 선호도 카드 */}
          <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-orange-50/50 backdrop-blur-sm">
            <CardHeader className="pb-4">
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-gradient-to-r from-orange-500 to-red-600 rounded-lg">
                  <Heart className="w-5 h-5 text-white" />
                </div>
                <div>
                  <CardTitle className="text-xl font-bold bg-gradient-to-r from-gray-800 to-orange-700 bg-clip-text text-transparent">
                    개인 선호도
                  </CardTitle>
                </div>
              </div>
            </CardHeader>
            <Separator className="mx-6" />
            <CardContent className="pt-6">
              <div className="flex flex-wrap gap-2">
                {profileData.personalPreferences.map((pref) => (
                  <Badge key={pref} variant="secondary" className="bg-orange-100 text-orange-800 hover:bg-orange-200">
                    {pref}
                  </Badge>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* 자격증 카드 */}
          {profileData.certifications.length > 0 && (
            <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-emerald-50/50 backdrop-blur-sm md:col-span-2">
              <CardHeader className="pb-4">
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-gradient-to-r from-emerald-500 to-teal-600 rounded-lg">
                    <Award className="w-5 h-5 text-white" />
                  </div>
                  <div>
                    <CardTitle className="text-xl font-bold bg-gradient-to-r from-gray-800 to-emerald-700 bg-clip-text text-transparent">
                      자격증
                    </CardTitle>
                  </div>
                </div>
              </CardHeader>
              <Separator className="mx-6" />
              <CardContent className="pt-6">
                <div className="flex flex-wrap gap-2">
                  {profileData.certifications.map((cert) => (
                    <Badge key={cert.id} variant="secondary" className="bg-emerald-100 text-emerald-800 hover:bg-emerald-200">
                      {cert.name}
                    </Badge>
                  ))}
                </div>
              </CardContent>
            </Card>
          )}
        </div>
      </div>
    </div>
  )
}
