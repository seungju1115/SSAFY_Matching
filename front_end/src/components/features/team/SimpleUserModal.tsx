import { useState, useEffect } from 'react'

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

// 사용자 타입 정의
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
}

const SimpleUserModal = ({ 
  isOpen, 
  onClose, 
  onSelectUser 
}: SimpleUserModalProps) => {
  const [selectedUserIds, setSelectedUserIds] = useState<string[]>([])
  

  // 임시 추천 사용자 데이터 (실제로는 AI 추천 API 사용)
  const mockRecommendedUsers: User[] = [
    {
      id: '1',
      name: '김개발',
      mainPosition: '백엔드',
      subPosition: '프론트엔드',
      domain: '웹 서비스',
      techStack: ['Java', 'Spring', 'React', 'MySQL'],
      projectPreferences: ['취업우선', '수상목표'],
      personalPreferences: ['꾸준함', '소통', '자율'],
      introduction: '안정적인 백엔드 시스템 구축에 관심이 많고, 팀워크를 중시합니다.'
    },
    {
      id: '2',
      name: '이프론트',
      mainPosition: '프론트엔드',
      subPosition: 'AI',
      domain: 'AI 서비스',
      techStack: ['React', 'TypeScript', 'Python', 'TensorFlow'],
      projectPreferences: ['학습중심', '포트폴리오중심'],
      personalPreferences: ['도전', '연구', '집중'],
      introduction: '사용자 경험을 중시하는 프론트엔드 개발자입니다. AI에도 관심이 많습니다.'
    },
    {
      id: '3',
      name: '박디자인',
      mainPosition: '디자인',
      subPosition: 'PM',
      domain: '모바일',
      techStack: ['Figma', 'Photoshop', 'Sketch', 'Notion'],
      projectPreferences: ['완성도추구', '빠른개발'],
      personalPreferences: ['협업', '속도', '유연'],
      introduction: '사용자 중심의 디자인을 추구하며, 프로젝트 관리 경험도 있습니다.'
    }
  ]

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
            <h2 className="text-xl md:text-2xl font-semibold text-white mb-1">팀원 추천</h2>
            <p className="text-sm text-white/70">함께할 팀원을 선택하세요</p>
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
            <div className="w-full overflow-x-auto scrollbar-hide">
              <div className="flex gap-4 md:gap-6 p-4 md:p-6 min-w-max md:justify-center">
                {mockRecommendedUsers.map((user: User) => (
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
                          <div className="text-sm text-slate-500 mt-1">{user.domain}</div>
                        </div>
                      </div>

                      {/* 기술 스택 */}
                      <div className="mb-4">
                        <h4 className="text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                          <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
                          기술 스택
                        </h4>
                        <div className="flex flex-wrap gap-2">
                          {user.techStack.slice(0, 4).map((tech: string, index: number) => (
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
                          {user.projectPreferences.slice(0, 3).map((pref: string, index: number) => (
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
                          {user.personalPreferences.slice(0, 3).map((pref: string, index: number) => (
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
                  const selectedUsers = mockRecommendedUsers.filter((u: User) => selectedUserIds.includes(u.id))
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
