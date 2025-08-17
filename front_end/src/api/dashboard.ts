import type { DashboardResponse } from '@/types/dashboard'
import apiClient from './axios'

export const dashboardApi = {
    // 대시보드 그래프 데이터 조회
    getDashboardData: async (): Promise<DashboardResponse> => {
        const response = await apiClient.get<DashboardResponse>('/dashboard/graph')
        return response.data
    }
}
