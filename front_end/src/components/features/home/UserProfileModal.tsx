import { useEffect, useRef } from 'react'
import { createPortal } from 'react-dom'
<<<<<<< HEAD
import type { UserSearchResponse } from '@/types/user'

=======
>>>>>>> 17624ac520ee6095d1a53ac9d2979b51dc366ac0
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

<<<<<<< HEAD
=======
// SimpleUserModal과 동일한 사용자 타입 정의
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

>>>>>>> 17624ac520ee6095d1a53ac9d2979b51dc366ac0
// Props 타입 정의
interface UserProfileModalProps {
  isOpen: boolean
  onClose: () => void
<<<<<<< HEAD
  user: UserSearchResponse | null
  onInvite?: (userId: number) => void
=======
  user: User | null
  onInvite?: (userId: string) => void
>>>>>>> 17624ac520ee6095d1a53ac9d2979b51dc366ac0
}

const UserProfileModal = ({ 
  isOpen, 
  onClose, 
  user,
  onInvite
}: UserProfileModalProps) => {
  // ESC로 닫기
  useEffect(() => {
    if (!isOpen) return
    const onKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape') {
        e.stopPropagation()
        onClose()
      }
    }
    document.addEventListener('keydown', onKeyDown, true)
    return () => document.removeEventListener('keydown', onKeyDown, true)
  }, [isOpen, onClose])

  // 비활성화(inert/aria-hidden) 해제
  const rootRef = useRef<HTMLDivElement>(null)
  useEffect(() => {
    if (!isOpen) return
    const el = rootRef.current
    if (el) {
      el.removeAttribute('inert')
      el.removeAttribute('aria-hidden')
      try { (el as any).inert = false } catch {}
    }
    const candidates: (HTMLElement | null | undefined)[] = [
      document.documentElement,
      document.body,
      document.getElementById('root'),
      document.getElementById('__next'),
    ]
    candidates.forEach((node) => {
      if (!node) return
      node.removeAttribute('inert')
      node.removeAttribute('aria-hidden')
      try { (node as any).inert = false } catch {}
    })
  }, [isOpen])

  if (!isOpen || !user) return null

<<<<<<< HEAD
  // 주 포지션과 부 포지션 분리 (첫 번째를 주 포지션으로)
  const mainPosition = user.wantedPosition?.[0] || '미정'
  const subPosition = user.wantedPosition?.[1] || ''

=======
>>>>>>> 17624ac520ee6095d1a53ac9d2979b51dc366ac0
  return createPortal(
    <div ref={rootRef} className="fixed inset-0 z-[100] flex flex-col bg-black/30 backdrop-blur-sm text-white">
      <style dangerouslySetInnerHTML={{ __html: scrollbarHideStyle }} />
        {/* 헤더 */}
        <div className="flex items-center justify-between p-4 md:p-6 border-b border-white/20">
          <div className="flex-1">
            <h2 className="text-xl md:text-2xl font-semibold text-white mb-1">개발자 프로필</h2>
            <p className="text-sm text-white/70">개발자 정보를 확인하세요</p>
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
<<<<<<< HEAD
        <div className="flex-1 overflow-y-auto">
          <div className="h-full flex items-center">
            <div className="w-full overflow-x-auto overflow-y-auto scrollbar-hide">
=======
        <div className="flex-1 overflow-hidden">
          <div className="h-full flex items-center">
            <div className="w-full overflow-x-auto scrollbar-hide">
>>>>>>> 17624ac520ee6095d1a53ac9d2979b51dc366ac0
              <div className="flex gap-4 md:gap-6 p-4 md:p-6 min-w-max md:justify-center">
                <div className="relative bg-white rounded-2xl shadow-xl w-72 md:w-80 flex-shrink-0 overflow-hidden">
                  <div className="p-6">
                    {/* 사용자 기본 정보 */}
                    <div className="flex items-start gap-4 mb-6">
                      <div className="w-14 h-14 bg-gradient-to-br from-blue-500 to-purple-600 rounded-xl flex items-center justify-center shadow-lg">
<<<<<<< HEAD
                        <span className="text-white font-bold text-lg">{user.userName[0]}</span>
                      </div>
                      <div className="flex-1">
                        <h3 className="font-bold text-xl text-slate-900 mb-1">{user.userName}</h3>
                        <div className="text-sm text-slate-600">
                          <span className="font-medium text-blue-600">{mainPosition}</span>
                          {subPosition && (
                            <>
                              <span className="text-slate-400 mx-1">•</span>
                              <span>{subPosition}</span>
                            </>
                          )}
                        </div>
                        <div className="text-sm text-slate-500 mt-1">
                          <span className={`px-2 py-1 rounded-full text-xs ${user.major ? 'bg-green-100 text-green-700' : 'bg-orange-100 text-orange-700'}`}>
                            {user.major ? '전공자' : '비전공자'}
                          </span>
                          <span className="text-slate-400 mx-1">•</span>
                          <span>{user.lastClass}기</span>
                        </div>
=======
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
>>>>>>> 17624ac520ee6095d1a53ac9d2979b51dc366ac0
                      </div>
                    </div>

                    {/* 기술 스택 */}
                    <div className="mb-4">
                      <h4 className="text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                        <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
                        기술 스택
                      </h4>
                      <div className="flex flex-wrap gap-2">
<<<<<<< HEAD
                        {user.techStack && user.techStack.length > 0 ? (
                          user.techStack.slice(0, 6).map((tech, index) => (
                            <span
                              key={index}
                              className="px-3 py-1 bg-blue-50 text-blue-700 text-xs rounded-full font-medium border border-blue-100"
                            >
                              {tech}
                            </span>
                          ))
                        ) : (
                          <span className="text-xs text-slate-400">기술 스택 정보 없음</span>
                        )}
=======
                        {user.techStack.slice(0, 4).map((tech, index) => (
                          <span
                            key={index}
                            className="px-3 py-1 bg-blue-50 text-blue-700 text-xs rounded-full font-medium border border-blue-100"
                          >
                            {tech}
                          </span>
                        ))}
>>>>>>> 17624ac520ee6095d1a53ac9d2979b51dc366ac0
                      </div>
                    </div>

                    {/* 프로젝트 선호도 */}
                    <div className="mb-4">
                      <h4 className="text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                        <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                        프로젝트 선호도
                      </h4>
                      <div className="flex flex-wrap gap-2">
<<<<<<< HEAD
                        {user.projectGoal && user.projectGoal.length > 0 ? (
                          user.projectGoal.slice(0, 3).map((pref, index) => (
                            <span
                              key={index}
                              className="px-3 py-1 bg-green-50 text-green-700 text-xs rounded-full font-medium border border-green-100"
                            >
                              {pref}
                            </span>
                          ))
                        ) : (
                          <span className="text-xs text-slate-400">프로젝트 선호도 정보 없음</span>
                        )}
=======
                        {user.projectPreferences.slice(0, 3).map((pref, index) => (
                          <span
                            key={index}
                            className="px-3 py-1 bg-green-50 text-green-700 text-xs rounded-full font-medium border border-green-100"
                          >
                            {pref}
                          </span>
                        ))}
>>>>>>> 17624ac520ee6095d1a53ac9d2979b51dc366ac0
                      </div>
                    </div>

                    {/* 개인 성향 */}
                    <div className="mb-4">
                      <h4 className="text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                        <div className="w-2 h-2 bg-purple-500 rounded-full"></div>
                        개인 성향
                      </h4>
                      <div className="flex flex-wrap gap-2">
<<<<<<< HEAD
                        {user.projectVive && user.projectVive.length > 0 ? (
                          user.projectVive.slice(0, 3).map((pref, index) => (
                            <span
                              key={index}
                              className="px-3 py-1 bg-purple-50 text-purple-700 text-xs rounded-full font-medium border border-purple-100"
                            >
                              {pref}
                            </span>
                          ))
                        ) : (
                          <span className="text-xs text-slate-400">개인 성향 정보 없음</span>
                        )}
                      </div>
                    </div>

                    {/* 자격증 */}
                    {user.qualification && (
                      <div className="mb-4">
                        <h4 className="text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                          <div className="w-2 h-2 bg-yellow-500 rounded-full"></div>
                          자격증
                        </h4>
                        <p className="text-sm text-slate-600 leading-relaxed">
                          {user.qualification}
                        </p>
                      </div>
                    )}

=======
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

>>>>>>> 17624ac520ee6095d1a53ac9d2979b51dc366ac0
                    {/* 자기소개 */}
                    <div>
                      <h4 className="text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                        <div className="w-2 h-2 bg-slate-500 rounded-full"></div>
                        자기소개
                      </h4>
                      <p className="text-sm text-slate-600 leading-relaxed line-clamp-3">
<<<<<<< HEAD
                        {user.userProfile || '자기소개가 작성되지 않았습니다.'}
=======
                        {user.introduction}
>>>>>>> 17624ac520ee6095d1a53ac9d2979b51dc366ac0
                      </p>
                    </div>
                  </div>
                </div>
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
              닫기
            </button>
            <button
              onClick={() => {
                onInvite?.(user.id)
                onClose()
              }}
              className="flex-1 px-6 py-3 bg-blue-600/90 hover:bg-blue-700/90 text-white rounded-xl transition-all duration-200 font-medium shadow-lg hover:shadow-xl"
            >
              초대하기
            </button>
          </div>
        </div>
    </div>,
    document.body
  )
}

export default UserProfileModal
