import { useEffect, useCallback } from 'react'
import { useDashboardStore } from '@/stores/dashboardStore.ts'
import { dashboardApi } from '@/api/dashboard'
import { useToast } from '@/hooks/use-toast'

export const useDashboard = () => {
    const { toast } = useToast()
    const {
        data,
        stats,
        isLoading,
        error,
        setData,
        setLoading,
        setError,
        clearData
    } = useDashboardStore()

    const fetchDashboardData = useCallback(async () => {
        try {
            setLoading(true)
            setError(null)

            const response = await dashboardApi.getDashboardData()
            setData(response)

        } catch (err: any) {
            const errorMessage = err?.response?.data?.message || err?.message || '대시보드 데이터를 불러오는데 실패했습니다'
            setError(errorMessage)

            toast({
                title: '오류',
                description: errorMessage,
                variant: 'destructive'
            })
        } finally {
            setLoading(false)
        }
    }, [])

    // 컴포넌트 마운트 시 자동으로 데이터 fetch
    useEffect(() => {
        fetchDashboardData()
    }, [fetchDashboardData])

    const refresh = useCallback(() => {
        fetchDashboardData()
    }, [fetchDashboardData])

    return {
        // 데이터
        data,
        stats,

        // 상태
        isLoading,
        error,

        // 액션
        refresh,
        clearData
    }
}