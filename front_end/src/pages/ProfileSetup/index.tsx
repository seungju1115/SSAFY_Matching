import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Progress } from '@/components/ui/progress'
import { Badge } from '@/components/ui/badge'
import { Separator } from '@/components/ui/separator'
import { ChevronRight, ChevronLeft, User, Sparkles, Target, Heart, Award, Code, Briefcase, Loader2 } from 'lucide-react'
import { useToast } from '@/hooks/use-toast'

// 컴포넌트 import
import PositionSelector from '@/components/features/profile/PositionSelector'
import SkillSelector from '@/components/features/profile/SkillSelector'
import IntroductionEditor from '@/components/features/profile/IntroductionEditor'
import PreferenceSelector from '@/components/features/profile/PreferenceSelector'
import CertificationInput from '@/components/features/profile/CertificationInput'

// API import
import { userHelpers } from '@/api/user'
import useUserStore from '@/stores/userStore'
import type {UserStatus} from "@/types/user.ts";

// 프로필 설정 데이터 타입
interface Certification {
  id: string
  name: string
}

interface ProfileSetupData {
  positions: string[]
  skills: string[]
  introduction: string
  projectPreferences: string[]
  personalPreferences: string[]
  certifications: Certification[]
  userStatus: UserStatus
}

export default function ProfileSetup() {
  const navigate = useNavigate()
  const { toast } = useToast()
  const { user, setUser } = useUserStore()
  const [step, setStep] = useState(1)
  const [isLoading, setIsLoading] = useState(false)
  const [profileData, setProfileData] = useState<ProfileSetupData>({
    positions: [],
    skills: [],
    introduction: '',
    projectPreferences: [],
    personalPreferences: [],
    certifications: [],
    userStatus: "WAITING" // 대기자 등록하면 WAITING 상태로 변하도록
  })

  const projectPreferenceSuggestions = [
    '취업우선', '수상목표', '포트폴리오중심', '학습중심', '아이디어실현', '실무경험', '빠른개발', '완성도추구'
  ]

  const personalPreferenceSuggestions = [
    '반말 지향', '존대 지향', '편한 분위기', '규칙적인 분위기', '리더 중심', '합의 중심', '새로운 주제', '안정적인 주제', '애자일 방식', '워터폴 방식'
  ]

  const handleNext = () => {
    if (step < 2) {
      setStep(step + 1)
    } else {
      handleComplete()
    }
  }

  const handlePrevious = () => {
    if (step > 1) {
      setStep(step - 1)
    }
  }

  const handleComplete = async () => {
    if (!user?.id) {
      toast({
        title: "오류",
        description: "사용자 정보를 찾을 수 없습니다.",
        variant: "destructive"
      })
      return
    }

    setIsLoading(true)
    
    try {
      const response = await userHelpers.completeProfileSetup(user.id, profileData)

      // API 응답에서 실제 데이터 추출
      const userData = response.data.data


      // 사용자 상태 업데이트 - 백엔드 Enum을 프론트엔드 문자열로 역매핑
      const updatedUser = {
        // 기존 필수 정보 보존
        id: user.id,
        userName: user.userName,
        role: user.role,
        email: user.email,
        major: user.major,
        lastClass: user.lastClass,
        // 기존 선택적 정보 보존 (있다면)
        teamId: user.teamId || null,
        teamName: user.teamName || null,
        projectExp: user.projectExp || null,
        isSigned: user.isSigned || false,
        // 새로 업데이트되는 프로필 정보
        userProfile: userData.userProfile || null,
        wantedPosition: userData.wantedPosition?.map((pos: string) => 
          userHelpers.mapEnumToDisplayValue(pos, 'position')
        ) || null,
        techStack: userData.techStack?.map((tech: string) => 
          userHelpers.mapEnumToDisplayValue(tech, 'techStack')
        ) || null,
        projectGoal: userData.projectGoal?.map((goal: string) => 
          userHelpers.mapEnumToDisplayValue(goal, 'projectGoal')
        ) || null,
        projectVive: userData.projectVive?.map((vibe: string) => 
          userHelpers.mapEnumToDisplayValue(vibe, 'projectVive')
        ) || null,
        qualification: userData.qualification || null,
        isProfileComplete: true
      }
      
      setUser(updatedUser)

      toast({
        title: "프로필 설정 완료!",
        description: "프로필이 성공적으로 설정되었습니다.",
      })

      navigate('/')
    } catch (error) {
      console.error('Profile setup failed:', error)
      toast({
        title: "프로필 설정 실패",
        description: "프로필 설정 중 오류가 발생했습니다. 다시 시도해주세요.",
        variant: "destructive"
      })
    } finally {
      setIsLoading(false)
    }
  }

  const progress = (step / 2) * 100

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50 to-indigo-100 relative overflow-hidden">
      {/* Background decorative elements */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute -top-40 -right-40 w-80 h-80 bg-gradient-to-br from-blue-200/30 to-purple-200/30 rounded-full blur-3xl" />
        <div className="absolute -bottom-40 -left-40 w-80 h-80 bg-gradient-to-br from-green-200/30 to-blue-200/30 rounded-full blur-3xl" />
      </div>
      
      <div className="relative z-10 flex flex-col items-center justify-center min-h-screen p-4 sm:p-6">
        <div className="w-full max-w-4xl mx-auto">
          {/* Header with enhanced styling */}
          <div className="text-center mb-12">
            <div className="inline-flex items-center justify-center w-20 h-20 bg-gradient-to-r from-blue-500 to-purple-600 rounded-2xl mb-6 shadow-lg">
              <User className="w-10 h-10 text-white" />
            </div>
            <h1 className="text-5xl font-bold bg-gradient-to-r from-gray-900 via-blue-800 to-purple-800 bg-clip-text text-transparent mb-4">
              프로필 설정
            </h1>
            <p className="text-xl text-gray-600 max-w-md mx-auto leading-relaxed">
              당신만의 특별한 이야기를 들려주세요 ✨
            </p>
          </div>

          {/* Enhanced progress section */}
          <div className="mb-12">
            <div className="relative">
              <Progress value={progress} className="w-full h-3 bg-gray-200/50" />
              <div className="flex justify-between items-center mt-4">
                <div className="flex items-center space-x-2">
                  <div className={`w-8 h-8 rounded-full flex items-center justify-center transition-all duration-300 ${
                    step >= 1 ? 'bg-gradient-to-r from-blue-500 to-purple-600 text-white shadow-lg' : 'bg-gray-200 text-gray-400'
                  }`}>
                    <Briefcase className="w-4 h-4" />
                  </div>
                  <span className={`font-semibold transition-colors duration-300 ${
                    step >= 1 ? 'text-blue-600' : 'text-gray-400'
                  }`}>기본 정보</span>
                </div>
                <div className="flex items-center space-x-2">
                  <div className={`w-8 h-8 rounded-full flex items-center justify-center transition-all duration-300 ${
                    step >= 2 ? 'bg-gradient-to-r from-green-500 to-blue-600 text-white shadow-lg' : 'bg-gray-200 text-gray-400'
                  }`}>
                    <Target className="w-4 h-4" />
                  </div>
                  <span className={`font-semibold transition-colors duration-300 ${
                    step >= 2 ? 'text-green-600' : 'text-gray-400'
                  }`}>선호도 및 자격</span>
                </div>
              </div>
            </div>
          </div>

          <div className="space-y-8">
            {step === 1 && (
              <div className="space-y-8 animate-in slide-in-from-right-5 duration-500">
                <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-blue-50/50 backdrop-blur-sm">
                  <CardHeader className="pb-4">
                    <div className="flex items-center space-x-3">
                      <div className="p-2 bg-gradient-to-r from-blue-500 to-purple-600 rounded-lg">
                        <Briefcase className="w-5 h-5 text-white" />
                      </div>
                      <div>
                        <CardTitle className="text-2xl font-bold bg-gradient-to-r from-gray-800 to-blue-700 bg-clip-text text-transparent">
                          포지션
                        </CardTitle>
                        <p className="text-sm text-gray-500 mt-1">전문 분야를 선택해주세요</p>
                      </div>
                    </div>
                  </CardHeader>
                  <Separator className="mx-6" />
                  <CardContent className="pt-6">
                    <PositionSelector
                      selectedPositions={profileData.positions}
                      onChange={(positions) => setProfileData((prev) => ({ ...prev, positions }))}
                    />
                    {profileData.positions.length > 0 && (
                      <div className="mt-4 flex flex-wrap gap-2">
                        {profileData.positions.map((position) => (
                          <Badge key={position} variant="secondary" className="bg-blue-100 text-blue-800 hover:bg-blue-200">
                            {position}
                          </Badge>
                        ))}
                      </div>
                    )}
                  </CardContent>
                </Card>

                <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-green-50/50 backdrop-blur-sm">
                  <CardHeader className="pb-4">
                    <div className="flex items-center space-x-3">
                      <div className="p-2 bg-gradient-to-r from-green-500 to-blue-600 rounded-lg">
                        <Code className="w-5 h-5 text-white" />
                      </div>
                      <div>
                        <CardTitle className="text-2xl font-bold bg-gradient-to-r from-gray-800 to-green-700 bg-clip-text text-transparent">
                          기술스택
                        </CardTitle>
                        <p className="text-sm text-gray-500 mt-1">보유 기술을 선택해주세요</p>
                      </div>
                    </div>
                  </CardHeader>
                  <Separator className="mx-6" />
                  <CardContent className="pt-6">
                    <SkillSelector
                      selectedSkills={profileData.skills}
                      onChange={(skills) => setProfileData((prev) => ({ ...prev, skills }))}
                    />
                    {profileData.skills.length > 0 && (
                      <div className="mt-4 flex flex-wrap gap-2">
                        {profileData.skills.map((skill) => (
                          <Badge key={skill} variant="secondary" className="bg-green-100 text-green-800 hover:bg-green-200">
                            {skill}
                          </Badge>
                        ))}
                      </div>
                    )}
                  </CardContent>
                </Card>

                <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-purple-50/50 backdrop-blur-sm">
                  <CardHeader className="pb-4">
                    <div className="flex items-center space-x-3">
                      <div className="p-2 bg-gradient-to-r from-purple-500 to-pink-600 rounded-lg">
                        <Sparkles className="w-5 h-5 text-white" />
                      </div>
                      <div>
                        <CardTitle className="text-2xl font-bold bg-gradient-to-r from-gray-800 to-purple-700 bg-clip-text text-transparent">
                          자기소개
                        </CardTitle>
                        <p className="text-sm text-gray-500 mt-1">자신만의 특별한 이야기를 들려주세요 (최대 500자)</p>
                      </div>
                    </div>
                  </CardHeader>
                  <Separator className="mx-6" />
                  <CardContent className="pt-6">
                    <IntroductionEditor
                      value={profileData.introduction}
                      onChange={(introduction) => setProfileData((prev) => ({ ...prev, introduction }))}
                      maxLength={500}
                    />
                  </CardContent>
                </Card>
              </div>
            )}

            {step === 2 && (
              <div className="space-y-8 animate-in slide-in-from-left-5 duration-500">
                <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-purple-50/50 backdrop-blur-sm">
                  <CardHeader className="pb-4">
                    <div className="flex items-center space-x-3">
                      <div className="p-2 bg-gradient-to-r from-purple-500 to-pink-600 rounded-lg">
                        <Target className="w-5 h-5 text-white" />
                      </div>
                      <div>
                        <CardTitle className="text-2xl font-bold bg-gradient-to-r from-gray-800 to-purple-700 bg-clip-text text-transparent">
                          프로젝트 선호 성향
                        </CardTitle>
                        <p className="text-sm text-gray-500 mt-1">선호하는 프로젝트 유형을 선택해주세요</p>
                      </div>
                    </div>
                  </CardHeader>
                  <Separator className="mx-6" />
                  <CardContent className="pt-6">
                    <PreferenceSelector
                      title=""
                      description=""
                      selectedPreferences={profileData.projectPreferences}
                      onChange={(projectPreferences) => setProfileData((prev) => ({ ...prev, projectPreferences }))}
                      suggestions={projectPreferenceSuggestions}
                      color="purple"
                    />
                    {profileData.projectPreferences.length > 0 && (
                      <div className="mt-4 flex flex-wrap gap-2">
                        {profileData.projectPreferences.map((pref) => (
                          <Badge key={pref} variant="secondary" className="bg-purple-100 text-purple-800 hover:bg-purple-200">
                            {pref}
                          </Badge>
                        ))}
                      </div>
                    )}
                  </CardContent>
                </Card>

                <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-orange-50/50 backdrop-blur-sm">
                  <CardHeader className="pb-4">
                    <div className="flex items-center space-x-3">
                      <div className="p-2 bg-gradient-to-r from-orange-500 to-red-600 rounded-lg">
                        <Heart className="w-5 h-5 text-white" />
                      </div>
                      <div>
                        <CardTitle className="text-2xl font-bold bg-gradient-to-r from-gray-800 to-orange-700 bg-clip-text text-transparent">
                          개인 선호 성향
                        </CardTitle>
                        <p className="text-sm text-gray-500 mt-1">개인적인 작업 스타일을 선택해주세요</p>
                      </div>
                    </div>
                  </CardHeader>
                  <Separator className="mx-6" />
                  <CardContent className="pt-6">
                    <PreferenceSelector
                      title=""
                      description=""
                      selectedPreferences={profileData.personalPreferences}
                      onChange={(personalPreferences) => setProfileData((prev) => ({ ...prev, personalPreferences }))}
                      suggestions={personalPreferenceSuggestions}
                      color="orange"
                    />
                    {profileData.personalPreferences.length > 0 && (
                      <div className="mt-4 flex flex-wrap gap-2">
                        {profileData.personalPreferences.map((pref) => (
                          <Badge key={pref} variant="secondary" className="bg-orange-100 text-orange-800 hover:bg-orange-200">
                            {pref}
                          </Badge>
                        ))}
                      </div>
                    )}
                  </CardContent>
                </Card>

                <Card className="group hover:shadow-2xl transition-all duration-300 border-0 bg-gradient-to-br from-white to-emerald-50/50 backdrop-blur-sm">
                  <CardHeader className="pb-4">
                    <div className="flex items-center space-x-3">
                      <div className="p-2 bg-gradient-to-r from-emerald-500 to-teal-600 rounded-lg">
                        <Award className="w-5 h-5 text-white" />
                      </div>
                      <div>
                        <CardTitle className="text-2xl font-bold bg-gradient-to-r from-gray-800 to-emerald-700 bg-clip-text text-transparent">
                          자격증
                        </CardTitle>
                        <p className="text-sm text-gray-500 mt-1">보유한 자격증을 입력해주세요 (최대 3개)</p>
                      </div>
                    </div>
                  </CardHeader>
                  <Separator className="mx-6" />
                  <CardContent className="pt-6">
                    <CertificationInput
                      certifications={profileData.certifications}
                      onChange={(certifications) => setProfileData((prev) => ({ ...prev, certifications }))}
                      maxCertifications={3}
                    />
                  </CardContent>
                </Card>
              </div>
            )}
        </div>

          {/* Enhanced navigation buttons */}
          <div className="flex justify-between items-center mt-16">
            <Button
              variant="outline"
              onClick={handlePrevious}
              disabled={step === 1}
              className="group w-44 h-16 text-lg font-semibold border-2 border-gray-200 hover:border-gray-300 hover:shadow-lg transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <ChevronLeft className="h-6 w-6 mr-2 group-hover:-translate-x-1 transition-transform duration-300" />
              이전
            </Button>
            
            <div className="text-center">
              <p className="text-sm text-gray-500 mb-2">단계 {step} / 2</p>
              <div className="flex space-x-2">
                <div className={`w-3 h-3 rounded-full transition-all duration-300 ${
                  step >= 1 ? 'bg-blue-500' : 'bg-gray-300'
                }`} />
                <div className={`w-3 h-3 rounded-full transition-all duration-300 ${
                  step >= 2 ? 'bg-blue-500' : 'bg-gray-300'
                }`} />
              </div>
            </div>
            
            <Button
              onClick={handleNext}
              disabled={isLoading}
              className="group w-44 h-16 text-lg font-semibold bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white shadow-lg hover:shadow-xl transition-all duration-300 transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none"
            >
              {step === 2 ? (
                <>
                  {isLoading ? (
                    <Loader2 className="h-6 w-6 mr-2 animate-spin" />
                  ) : (
                    <Sparkles className="h-6 w-6 mr-2" />
                  )}
                  {isLoading ? '저장 중...' : '완료'}
                </>
              ) : (
                <>
                  다음
                  <ChevronRight className="h-6 w-6 ml-2 group-hover:translate-x-1 transition-transform duration-300" />
                </>
              )}
            </Button>
          </div>
        </div>
      </div>
    </div>
  )
}