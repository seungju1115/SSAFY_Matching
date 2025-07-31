import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { 
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { 
  UserDetailSettings as UserDetailSettingsType, 
  SEMESTER_OPTIONS, 
  CLASS_OPTIONS, 
  MAJOR_OPTIONS 
} from '@/types/user'
import { authAPI } from '@/api/auth'
import { CheckCircle2, User, GraduationCap, BookOpen, Users } from 'lucide-react'

export default function Setup() {
  const navigate = useNavigate()
  const [settings, setSettings] = useState<UserDetailSettingsType>({
    semester: '',
    classNumber: '',
    major: '',
    isMajor: true
  })
  const [isLoading, setIsLoading] = useState(false)


  const handleSettingChange = (field: keyof UserDetailSettingsType, value: string | boolean) => {
    setSettings(prev => ({
      ...prev,
      [field]: value
    }))
  }

  const handleComplete = async () => {
    if (!settings.semester || !settings.classNumber || !settings.major) {
      return
    }

    setIsLoading(true)
    try {
      await authAPI.updateUserDetails(settings)
      console.log('User detail settings saved successfully:', settings)
      localStorage.setItem('userDetailSettings', JSON.stringify(settings))
      navigate('/')
    } catch (error) {
      console.error('Failed to save settings:', error)
    } finally {
      setIsLoading(false)
    }
  }

  const isFormComplete = settings.semester && settings.classNumber && settings.major

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50 flex items-center justify-center p-4">
      <div className="w-full max-w-2xl">
        {/* 헤더 */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-gradient-to-r from-blue-600 to-purple-600 rounded-full mb-4">
            <User className="w-8 h-8 text-white" />
          </div>
          <h1 className="text-3xl font-bold text-gray-900 mb-2">프로필 설정</h1>
          <p className="text-gray-600">Match SSAFY를 시작하기 위한 마지막 단계입니다</p>
          

        </div>

        <Card className="shadow-xl border-0 bg-white/80 backdrop-blur-sm">
          <CardHeader className="text-center pb-6">
            <CardTitle className="text-xl text-gray-800">기본 정보 입력</CardTitle>
            <CardDescription>
              매칭을 위해 필요한 정보를 입력해주세요
            </CardDescription>
          </CardHeader>
          
          <CardContent className="space-y-8">
            {/* 학기 선택 */}
            <div className="space-y-3">
              <div className="flex items-center space-x-2">
                <GraduationCap className="w-5 h-5 text-blue-600" />
                <label className="text-sm font-semibold text-gray-700">학기</label>
                {settings.semester && <CheckCircle2 className="w-4 h-4 text-green-500" />}
              </div>
              <Select
                value={settings.semester}
                onValueChange={(value) => {
                  handleSettingChange('semester', value)
                  handleSettingChange('classNumber', '') // 학기 변경시 반 초기화
                }}
              >
                <SelectTrigger className="w-full h-12 text-base">
                  <SelectValue placeholder="학기를 선택하세요" />
                </SelectTrigger>
                <SelectContent>
                  {SEMESTER_OPTIONS.map((option) => (
                    <SelectItem key={option.value} value={option.value}>
                      {option.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {/* 반 선택 */}
            {settings.semester && (
              <div className="space-y-3 animate-in slide-in-from-top-2 duration-300">
                <div className="flex items-center space-x-2">
                  <Users className="w-5 h-5 text-green-600" />
                  <label className="text-sm font-semibold text-gray-700">반</label>
                  {settings.classNumber && <CheckCircle2 className="w-4 h-4 text-green-500" />}
                </div>
                <Select
                  value={settings.classNumber}
                  onValueChange={(value) => handleSettingChange('classNumber', value)}
                >
                  <SelectTrigger className="w-full h-12 text-base">
                    <SelectValue placeholder={`${settings.semester}학기 반을 선택하세요`} />
                  </SelectTrigger>
                  <SelectContent>
                    {CLASS_OPTIONS.map((option) => (
                      <SelectItem key={option.value} value={option.value}>
                        {settings.semester}학기 {option.label}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
            )}

            {/* 전공/비전공 선택 */}
            <div className="space-y-3">
              <div className="flex items-center space-x-2">
                <BookOpen className="w-5 h-5 text-purple-600" />
                <label className="text-sm font-semibold text-gray-700">전공 구분</label>
              </div>
              <Select
                value={settings.isMajor.toString()}
                onValueChange={(value) => handleSettingChange('isMajor', value === 'true')}
              >
                <SelectTrigger className="w-full h-12 text-base">
                  <SelectValue placeholder="전공/비전공을 선택하세요" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="true">전공</SelectItem>
                  <SelectItem value="false">비전공</SelectItem>
                </SelectContent>
              </Select>
            </div>

            {/* 전공 트랙 선택 */}
            <div className="space-y-3">
              <div className="flex items-center space-x-2">
                <GraduationCap className="w-5 h-5 text-orange-600" />
                <label className="text-sm font-semibold text-gray-700">전공 트랙</label>
                {settings.major && <CheckCircle2 className="w-4 h-4 text-green-500" />}
              </div>
              <Select
                value={settings.major}
                onValueChange={(value) => handleSettingChange('major', value)}
              >
                <SelectTrigger className="w-full h-12 text-base">
                  <SelectValue placeholder="전공 트랙을 선택하세요" />
                </SelectTrigger>
                <SelectContent>
                  {MAJOR_OPTIONS.map((option) => (
                    <SelectItem key={option.value} value={option.value}>
                      {option.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {/* 완료 버튼 */}
            <div className="pt-6">
              <Button
                onClick={handleComplete}
                disabled={!isFormComplete || isLoading}
                className="w-full h-14 text-lg font-semibold bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200"
              >
                {isLoading ? (
                  <div className="flex items-center space-x-2">
                    <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin" />
                    <span>설정 저장 중...</span>
                  </div>
                ) : (
                  <div className="flex items-center space-x-2">
                    <CheckCircle2 className="w-5 h-5" />
                    <span>설정 완료</span>
                  </div>
                )}
              </Button>
            </div>
          </CardContent>
        </Card>


      </div>
    </div>
  )
} 