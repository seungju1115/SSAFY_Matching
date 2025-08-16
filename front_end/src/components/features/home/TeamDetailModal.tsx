import type { Team } from './TeamSection'
import { useEffect, useRef } from 'react'
import { createPortal } from 'react-dom'

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

// Props 타입 정의
interface TeamDetailModalProps {
  isOpen: boolean
  onClose: () => void
  team: Team | null
  onJoinRequest?: (teamId: number) => void
}

const TeamDetailModal = ({ 
  isOpen, 
  onClose, 
  team,
  onJoinRequest
}: TeamDetailModalProps) => {
  // ESC 키로 닫기: 훅은 항상 동일한 순서로 호출되어야 하므로 early return보다 위에 둔다
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

  // Radix Dialog가 외부 콘텐츠에 inert/aria-hidden을 적용하는 경우 대비하여 해제
  const rootRef = useRef<HTMLDivElement>(null)
  useEffect(() => {
    if (!isOpen) return
    const el = rootRef.current
    if (el) {
      el.removeAttribute('inert')
      el.removeAttribute('aria-hidden')
      // 일부 브라우저에서 inert 프로퍼티가 존재할 수 있음
      try { (el as any).inert = false } catch {}
    }
  }, [isOpen])

  // 혹시 모를 전역 inert/aria-hidden 정리 (Radix가 모달 모드에서 외부를 비활성화한 잔여 효과 방지)
  useEffect(() => {
    if (!isOpen) return
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

  if (!isOpen || !team) return null

  // 역할별 정보 매핑 (tmp_dev API 구조에 맞게 수정)
  const roleInfo = [
    { key: 'backend', label: '백엔드', color: 'bg-blue-400', current: team.roleCurrent?.backend || 0, target: team.roleDistribution?.backend || 0 },
    { key: 'frontend', label: '프론트엔드', color: 'bg-green-400', current: team.roleCurrent?.frontend || 0, target: team.roleDistribution?.frontend || 0 },
    { key: 'ai', label: 'AI', color: 'bg-purple-400', current: team.roleCurrent?.ai || 0, target: team.roleDistribution?.ai || 0 },
    { key: 'design', label: '디자인', color: 'bg-pink-400', current: team.roleCurrent?.design || 0, target: team.roleDistribution?.design || 0 },
    { key: 'pm', label: 'PM', color: 'bg-orange-400', current: team.roleCurrent?.pm || 0, target: team.roleDistribution?.pm || 0 }
  ].filter(role => role.target > 0)

  // 도메인 표시 통일: domains[0] 우선 사용, 없으면 domain 사용
  const displayedDomain = (team.domains && team.domains.length > 0) ? team.domains[0] : team.domain

  return createPortal(
    <div
      ref={rootRef}
      className="fixed inset-0 z-[100] flex flex-col bg-black/30 backdrop-blur-sm text-white"
    >
      <style dangerouslySetInnerHTML={{ __html: scrollbarHideStyle }} />
        {/* 헤더 */}
        <div className="flex items-center justify-between p-4 md:p-6 border-b border-white/20">
          <div className="flex-1">
            <h2 className="text-xl md:text-2xl font-semibold text-white mb-1">팀 정보</h2>
            <p className="text-sm text-white/70">팀 상세 정보를 확인하세요</p>
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
        <div className="flex-1 overflow-y-auto">
          <div className="h-full flex items-center">
            <div className="w-full overflow-x-auto overflow-y-auto scrollbar-hide">
              <div className="flex gap-4 md:gap-6 p-4 md:p-6 min-w-max md:justify-center">
                <div className="relative bg-white rounded-2xl shadow-xl w-72 md:w-80 flex-shrink-0 overflow-hidden">
                  <div className="p-6">
                    {/* 팀 기본 정보 */}
                    <div className="flex items-start gap-4 mb-6">
                      <div className="w-14 h-14 bg-gradient-to-br from-blue-500 to-purple-600 rounded-xl flex items-center justify-center shadow-lg">
                        <span className="text-white font-bold text-lg">{team.leader.name[0]}</span>
                      </div>
                      <div className="flex-1">
                        <h3 className="font-bold text-xl text-slate-900 mb-1">{team.leader.name} 팀장</h3>
                        <div className="text-sm text-slate-600">
                          <span className="font-medium text-blue-600">{team.members}/{team.maxMembers}명</span>
                          <span className="text-slate-400 mx-1">•</span>
                          <span>{displayedDomain || '도메인 미정'}</span>
                        </div>
                      </div>
                    </div>

                    {/* 도메인 */}
                    <div className="mb-4">
                      <h4 className="text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                        <div className="w-2 h-2 bg-emerald-500 rounded-full"></div>
                        프로젝트 도메인
                      </h4>
                      <div className="flex flex-wrap gap-2">
                        {displayedDomain ? (
                          <span className="px-3 py-1 bg-emerald-50 text-emerald-700 text-xs rounded-full font-medium border border-emerald-100">
                            {displayedDomain}
                          </span>
                        ) : (
                          <span className="text-xs text-slate-400">미정</span>
                        )}
                      </div>
                    </div>

                    {/* 역할 비율 */}
                    <div className="mb-4">
                      <h4 className="text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                        <div className="w-2 h-2 bg-purple-500 rounded-full"></div>
                        역할 비율 설정
                      </h4>
                      <div className="space-y-2">
                        {roleInfo.map((role) => {
                          const pct = role.target ? Math.round((role.current / role.target) * 100) : 0
                          return (
                            <div key={role.key} className="flex items-center gap-2">
                              <div className="w-12 text-[10px] text-gray-600">{role.label}</div>
                              <div className="flex-1 bg-gray-200 rounded-full h-2 overflow-hidden">
                                <div 
                                  className={`h-2 ${role.color} transition-all duration-300`} 
                                  style={{ width: `${pct}%` }} 
                                />
                              </div>
                              <span className="text-[11px] text-gray-700 font-medium w-14 text-right">
                                {role.current}/{role.target}
                              </span>
                            </div>
                          )
                        })}
                      </div>
                    </div>

                    {/* 프로젝트 성향 */}
                    <div className="mb-4">
                      <h4 className="text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                        <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                        프로젝트 성향
                      </h4>
                      <div className="flex flex-wrap gap-2">
                        {team.projectPreferences && team.projectPreferences.slice(0, 3).map((pref: string, index: number) => (
                          <span
                            key={index}
                            className="px-3 py-1 bg-green-50 text-green-700 text-xs rounded-full font-medium border border-green-100"
                          >
                            {pref}
                          </span>
                        ))}
                      </div>
                    </div>

                    {/* 팀 한줄 소개 */}
                    <div>
                      <h4 className="text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                        <div className="w-2 h-2 bg-slate-500 rounded-full"></div>
                        팀 한줄 소개
                      </h4>
                      <p className="text-sm text-slate-600 leading-relaxed line-clamp-3">
                        {team.description || '함께 성장하며 혁신적인 프로젝트를 만들어갈 팀입니다.'}
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
                onJoinRequest?.(team.id)
                onClose()
              }}
              className="flex-1 px-6 py-3 bg-blue-600/90 hover:bg-blue-700/90 text-white rounded-xl transition-all duration-200 font-medium shadow-lg hover:shadow-xl"
            >
              참여 신청
            </button>
          </div>
        </div>
    </div>,
    document.body
  )
}

export default TeamDetailModal
