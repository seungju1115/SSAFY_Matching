import { create } from 'zustand'
import type { DashboardResponse, DashboardStats } from '@/types/dashboard.ts'

interface DashboardStore {
    data: DashboardResponse | null
    stats: DashboardStats | null
    isLoading: boolean
    error: string | null

    // Actions
    setData: (data: DashboardResponse) => void
    setLoading: (loading: boolean) => void
    setError: (error: string | null) => void
    clearData: () => void
}

export const useDashboardStore = create<DashboardStore>((set) => ({
    data: null,
    stats: null,
    isLoading: false,
    error: null,

    setData: (data: DashboardResponse) => {
        // 받은 데이터로부터 통계 계산
        const stats: DashboardStats = {
            totalUsers: data.whole,
            matchedUsers: data.matchedMajor + data.mathcedUnmajor,
            unmatchedUsers: data.unmatchedMajor + data.unmatchedUnmajor,
            matchingRate: data.whole > 0 ? ((data.matchedMajor + data.mathcedUnmajor) / data.whole) * 100 : 0,

            majorStats: {
                total: data.matchedMajor + data.unmatchedMajor,
                matched: data.matchedMajor,
                unmatched: data.unmatchedMajor,
                matchingRate: (data.matchedMajor + data.unmatchedMajor) > 0 ?
                    (data.matchedMajor / (data.matchedMajor + data.unmatchedMajor)) * 100 : 0
            },

            nonMajorStats: {
                total: data.mathcedUnmajor + data.unmatchedUnmajor,
                matched: data.mathcedUnmajor,
                unmatched: data.unmatchedUnmajor,
                matchingRate: (data.mathcedUnmajor + data.unmatchedUnmajor) > 0 ?
                    (data.mathcedUnmajor / (data.mathcedUnmajor + data.unmatchedUnmajor)) * 100 : 0
            },

            positionStats: {
                main: {
                    ai: data.ai_main,
                    backend: data.back_main,
                    frontend: data.front_main,
                    design: data.design_main,
                    pm: data.pm_main
                },
                sub: {
                    ai: data.ai_sub,
                    backend: data.back_sub,
                    frontend: data.front_sub,
                    design: data.design_sub,
                    pm: data.pm_sub
                }
            }
        }

        set({ data, stats, error: null })
    },

    setLoading: (isLoading: boolean) => set({ isLoading }),

    setError: (error: string | null) => set({ error }),

    clearData: () => set({
        data: null,
        stats: null,
        error: null,
        isLoading: false
    })
}))
