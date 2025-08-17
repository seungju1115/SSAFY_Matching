import { useState, useEffect } from 'react'
import { useAI } from '@/hooks/useAI'
import type { CandidateDtoDisplay } from '@/types/ai'

// 스크롤바 숨김 스타일
const scrollbarHideStyle = `
  .scrollbar-hide {
    -ms-overflow-style: none;
    scrollbar-width: none;
  }
  .scrollbar-hide::-webkit-scrollbar {
    display: none;
  }
`

// 사용자 타입 정의 (기존 interface 유지하되 AI 데이터와 매핑)
interface User {
  id: string
  name: string
  mainPosition: string
  subPosition: string
  domain: string
  techStack: string[]
  projectPreferences: string[]
  personalPreferences: string[]
  introduction: string
}

// Props 타입 정의
interface SimpleUserModalProps {
  isOpen: boolean
  onClose: () => void
  onSelectUser: (users: User[]) => void
  teamId?: number // AI 추천을 위한 팀 ID
}

// 모의 데이터
const mockRecommendedUsers: User[] = [
  {
    id: '1',
    name: '김개발',
    mainPosition: '프론트엔드',
    subPosition: 'UI/UX',
    domain: '웹 개발',
    techStack: ['React', 'TypeScript', 'Tailwind CSS', 'Next.js'],
    projectPreferences: ['혁신적인', '사용자 중심', '빠른 개발'],
    personalPreferences: ['소통 활발', '책임감 강함', '학습 열정'],
    introduction: '사용자 경험을 최우선으로 생각하는 프론트엔드 개발자입니다. 새로운 기술 학습을 즐기며, 팀원들과의 활발한 소통을 통해 더 나은 결과를 만들어내고 싶습니다.'
  },
  {
    id: '2',
    name: '이백엔드',
    mainPosition: '백엔드',
    subPosition: 'DevOps',
    domain: '서버 개발',
    techStack: ['Node.js', 'Python', 'Docker', 'AWS', 'MongoDB'],
    projectPreferences: ['확장 가능한', '안정적인', '성능 최적화'],
    personalPreferences: ['체계적 사고', '문제 해결', '꼼꼼함'],
    introduction: '안정적이고 확장 가능한 서버 아키텍처 구축에 전문성을 가지고 있습니다. 복잡한 문제를 체계적으로 분석하고 해결하는 것을 좋아합니다.'
  },
  {
    id: '3',
    name: '박풀스택',
    mainPosition: '풀스택',
    subPosition: '기획',
    domain: '웹/앱 개발',
    techStack: ['Vue.js', 'Spring Boot', 'MySQL', 'Flutter'],
    projectPreferences: ['창의적인', '실용적인', '완성도 높은'],
    personalPreferences: ['균형감각', '적응력', '리더십'],
    introduction: '프론트엔드부터 백엔드까지 전체적인 개발 프로세스를 이해하고 있으며, 기획 단계부터 참여하여 사용자 관점에서 생각하는 개발자입니다.'
  }
]

const SimpleUserModal = ({
                           isOpen,
                           onClose,
                           onSelectUser,
                           teamId
                         }: SimpleUserModalProps) => {
  const [selectedUserIds, setSelectedUserIds] = useState<string[]>([])
  const {
    candidatesDisplay,
    isLoadingCandidates,
    candidatesError,
    getBasicRecommendations
  } = useAI()

  // CandidateDtoDisplay를 User 타입으로 변환
  const convertCandidateToUser = (candidate: CandidateDtoDisplay): User => ({
    id: candidate.userId.toString(),
    name: candidate.userName,
    mainPosition: candidate.mainPos,
    subPosition: candidate.subPos,
    domain: '', // 빈 문자열로 설정
    techStack: candidate.techs,
    projectPreferences: candidate.goals,
    personalPreferences: candidate.vives,
    introduction: candidate.userProfile || '자기소개 정보가 없습니다.'
  })

  // AI 추천 데이터가 있으면 사용, 없으면 mock 데이터 사용
  const recommendedUsers: User[] = candidatesDisplay.length > 0
      ? candidatesDisplay.map(convertCandidateToUser)
      : mockRecommendedUsers

  // 팀 ID가 있을 때 AI 추천 요청
  useEffect(() => {
    if (isOpen && teamId) {
      getBasicRecommendations(teamId)
    }
  }, [isOpen, teamId, getBasicRecommendations])

  // 선택 초기화 (모달이 열릴 때마다)
  useEffect(() => {
    if (isOpen) {
      setSelectedUserIds([])
    }
  }, [isOpen])

  // 사용자 선택 처리 함수
  const handleUserSelect = (userId: string) => {
    setSelectedUserIds(prev => {
      // 이미 선택된 경우 제거
      if (prev.includes(userId)) {
        return prev.filter(id => id !== userId)
      }
      // 3명 이상 선택 불가
      if (prev.length >= 3) {
        return prev
      }
      // 새로 선택 추가
      return [...prev, userId]
    })
  }

  if (!isOpen) return null

  return (
      <div className="fixed inset-0 z-50 flex flex-col bg-black/30 backdrop-blur-sm text-white">
        <style dangerouslySetInnerHTML={{ __html: scrollbarHideStyle }} />
        {/* 헤더 */}
        <div className="flex items-center justify-between p-4 md:p-6 border-b border-white/20">
          <div className="flex-1">
            <h2 className="text-xl md:text-2xl font-semibold text-white mb-1">
              팀원 추천 {teamId ? '(AI 추천)' : ''}
            </h2>
            <p className="text-sm text-white/70">
              {isLoadingCandidates ? '추천 후보자를 불러오는 중...' : '함께할 팀원을 선택하세요'}
            </p>
          </div>
          <button
              onClick={onClose}
              className="w-10 h-10 rounded-full bg-white/10 hover:bg-white/20 text-white/70 hover:text-white transition-all duration-200 flex items-center justify-center"
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        {/* 카드 스크롤 영역 */}
        <div className="flex-1 overflow-hidden">
          <div className="h-full flex items-center">
            {isLoadingCandidates ? (
                <div className="w-full flex items-center justify-center">
                  <div className="text-white/70 text-center">
                    <div className="w-8 h-8 border-2 border-white/30 border-t-white rounded-full animate-spin mx-auto mb-2"></div>
                    <p>AI가 최적의 팀원을 찾고 있습니다...</p>
                  </div>
                </div>
            ) : candidatesError ? (
                <div className="w-full flex items-center justify-center">
                  <div className="text-red-300 text-center">
                    <p>추천 데이터를 불러올 수 없습니다.</p>
                    <p className="text-sm text-white/50 mt-1">{candidatesError}</p>
                  </div>
                </div>
            ) : (
                <div className="w-full overflow-x-auto scrollbar-hide">
                  <div className="flex gap-4 md:gap-6 p-4 md:p-6 min-w-max md:justify-center">
                    {recommendedUsers.map((user) => (
                        <div
                            key={user.id}
                            onClick={() => handleUserSelect(user.id)}
                            className={`
                      relative bg-white rounded-2xl shadow-xl cursor-pointer transition-all duration-300 
                      w-72 md:w-80 flex-shrink-0 overflow-hidden hover:shadow-2xl hover:-translate-y-1
                      ${selectedUserIds.includes(user.id) ? 'ring-2 ring-blue-500 shadow-blue-500/25' : 'hover:shadow-slate-900/20'}
                    `}
                        >
                          {/* 선택 표시 */}
                          {selectedUserIds.includes(user.id) && (
                              <div className="absolute top-4 right-4 w-6 h-6 bg-blue-500 rounded-full flex items-center justify-center">
                                <div className="text-xs font-bold text-white">
                                  {selectedUserIds.indexOf(user.id) + 1}
                                </div>
                              </div>
                          )}

                          <div className="p-6">
                            {/* 사용자 기본 정보 */}
                            <div className="flex items-start gap-4 mb-6">
                              <div className="w-14 h-14 bg-gradient-to-br from-blue-500 to-purple-600 rounded-xl flex items-center justify-center shadow-lg">
                                <span className="text-white font-bold text-lg">{user.name[0]}</span>
                              </div>
                              <div className="flex-1">
                                <h3 className="font-bold text-xl text-slate-900 mb-1">{user.name}</h3>
                                <div className="text-sm text-slate-600">
                                  <span className="font-medium text-blue-600">{user.mainPosition}</span>
                                  <span className="text-slate-400 mx-1">•</span>
                                  <span>{user.subPosition}</span>
                                </div>
                                {user.domain && (
                                    <div className="text-sm text-slate-500 mt-1">{user.domain}</div>
                                )}
                              </div>
                            </div>

                            {/* 기술 스택 */}
                            <div className="mb-4">
                              <h4 className="text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                                <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
                                기술 스택
                              </h4>
                              <div className="flex flex-wrap gap-2">
                                {user.techStack.slice(0, 4).map((tech, index) => (
                                    <span
                                        key={index}
                                        className="px-3 py-1 bg-blue-50 text-blue-700 text-xs rounded-full font-medium border border-blue-100"
                                    >
                              {tech}
                            </span>
                                ))}
                              </div>
                            </div>

                            {/* 프로젝트 선호도 */}
                            <div className="mb-4">
                              <h4 className="text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                                <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                                프로젝트 선호도
                              </h4>
                              <div className="flex flex-wrap gap-2">
                                {user.projectPreferences.slice(0, 3).map((pref, index) => (
                                    <span
                                        key={index}
                                        className="px-3 py-1 bg-green-50 text-green-700 text-xs rounded-full font-medium border border-green-100"
                                    >
                              {pref}
                            </span>
                                ))}
                              </div>
                            </div>

                            {/* 개인 성향 */}
                            <div className="mb-4">
                              <h4 className="text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                                <div className="w-2 h-2 bg-purple-500 rounded-full"></div>
                                개인 성향
                              </h4>
                              <div className="flex flex-wrap gap-2">
                                {user.personalPreferences.slice(0, 3).map((pref, index) => (
                                    <span
                                        key={index}
                                        className="px-3 py-1 bg-purple-50 text-purple-700 text-xs rounded-full font-medium border border-purple-100"
                                    >
                              {pref}
                            </span>
                                ))}
                              </div>
                            </div>

                            {/* 자기소개 */}
                            <div>
                              <h4 className="text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                                <div className="w-2 h-2 bg-slate-500 rounded-full"></div>
                                자기소개
                              </h4>
                              <p className="text-sm text-slate-600 leading-relaxed line-clamp-3">
                                {user.introduction}
                              </p>
                            </div>
                          </div>
                        </div>
                    ))}
                  </div>
                </div>
            )}
          </div>
        </div>

        {/* 하단 버튼들 */}
        <div className="p-4 md:p-6 border-t border-white/20 bg-black/20">
          <div className="flex gap-3 max-w-md mx-auto">
            <button
                onClick={onClose}
                className="flex-1 px-6 py-3 bg-white/20 hover:bg-white/30 text-white rounded-xl transition-all duration-200 font-medium"
            >
              취소
            </button>
            <button
                onClick={() => {
                  if (selectedUserIds.length > 0) {
                    const selectedUsers = recommendedUsers.filter(u => selectedUserIds.includes(u.id))
                    if (selectedUsers.length > 0) {
                      onSelectUser(selectedUsers)
                      onClose()
                    }
                  }
                }}
                disabled={selectedUserIds.length === 0}
                className={`flex-1 px-6 py-3 rounded-xl transition-all duration-200 font-medium ${selectedUserIds.length > 0 ? 'bg-blue-600/90 hover:bg-blue-700/90 text-white shadow-lg hover:shadow-xl' : 'bg-white/10 text-white/50 cursor-not-allowed'}`}
            >
              팀원 초대하기 ({selectedUserIds.length}/3)
            </button>
          </div>
        </div>
      </div>
  )
}

export default SimpleUserModal
