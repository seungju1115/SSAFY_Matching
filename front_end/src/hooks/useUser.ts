// User 관련 커스텀 훅
import { useState, useCallback } from 'react'
import { userAPI, userHelpers } from '@/api/user'
import type {
  UserProfileResponse,
  UserProfileUpdateRequest,
  UserSearchRequest,
  UserSearchResponse, UserStatus
} from '@/types/user'
import { useToast } from '@/hooks/use-toast'
import { useEnumMapper } from '@/hooks/useEnumMapper'

export const useUser = () => {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const { toast } = useToast()
  const { mapProjectGoalArray, mapProjectVibeArray, mapTechStackArray, mapPositionArray } = useEnumMapper()

  const clearError = () => setError(null)

  // UserSearchResponse에서 Enum을 문자열로 변환하는 함수
  const convertUserSearchEnums = useCallback((user: UserSearchResponse): UserSearchResponse => {
    return {
      ...user,
      projectGoal: mapProjectGoalArray(user.projectGoal as any), // 이미 변환된 상태일 수도 있음
      projectVive: mapProjectVibeArray(user.projectVive as any),
      techStack: mapTechStackArray(user.techStack as any),
      wantedPosition: mapPositionArray(user.wantedPosition as any)
    }
  }, [mapProjectGoalArray, mapProjectVibeArray, mapTechStackArray, mapPositionArray])

  // 내 프로필 조회
  const getMyProfile = async (): Promise<UserProfileResponse | null> => {
    try {
      setLoading(true)
      clearError()
      
      const response = await userAPI.getMyProfile()
      return response.data.data
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '프로필을 불러오는데 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
      return null
    } finally {
      setLoading(false)
    }
  }

  // 특정 사용자 프로필 조회
  const getUserProfile = async (userId: number): Promise<UserProfileResponse | null> => {
    try {
      setLoading(true)
      clearError()
      
      const response = await userAPI.getUserProfile(userId)
      return response.data.data
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '사용자 프로필을 불러오는데 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
      return null
    } finally {
      setLoading(false)
    }
  }

  // 사용자 프로필 수정
  const updateUserProfile = async (userId: number, updateData: UserProfileUpdateRequest): Promise<UserProfileResponse | null> => {
    try {
      setLoading(true)
      clearError()
      
      const response = await userAPI.updateUserProfile(userId, updateData)
      
      toast({
        title: "성공",
        description: "프로필이 성공적으로 수정되었습니다"
      })
      
      return response.data.data
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '프로필 수정에 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
      return null
    } finally {
      setLoading(false)
    }
  }

  // 사용자 프로필 삭제
  const deleteUserProfile = async (userId: number): Promise<boolean> => {
    try {
      setLoading(true)
      clearError()
      
      await userAPI.deleteUserProfile(userId)
      
      toast({
        title: "성공",
        description: "프로필이 삭제되었습니다"
      })
      
      return true
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '프로필 삭제에 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
      return false
    } finally {
      setLoading(false)
    }
  }

  // 팀원 검색 (팀이 없는 사용자)
  const searchUsersWithoutTeamWithCondition = async (searchCriteria: UserSearchRequest): Promise<UserSearchResponse[]> => {
    try {
      setLoading(true)
      clearError()
      
      const response = await userAPI.searchUsersWithoutTeam(searchCriteria)
      return response.data.data
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '사용자 검색에 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
      return []
    } finally {
      setLoading(false)
    }
  }

  // 대기중인 사용자 조회
  const getWaitingUsers = async (): Promise<UserSearchResponse[]> => {
    try {
      setLoading(true)
      clearError()
      
      const response = await userAPI.getWaitingUsers()
      
      // Enum들을 문자열로 변환
      const convertedUsers = response.data.data.map(convertUserSearchEnums)
      
      return convertedUsers
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '대기중인 사용자를 불러오는데 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
      return []
    } finally {
      setLoading(false)
    }
  }

  // 프로필 설정 완료 (편의 함수)
  const completeProfileSetup = async (userId: number, profileData: {
    positions: string[]
    skills: string[]
    introduction: string
    projectPreferences: string[]
    personalPreferences: string[]
    certifications: { name: string }[]
    userStatus: UserStatus
  }): Promise<UserProfileResponse | null> => {
    try {
      setLoading(true)
      clearError()
      
      const response = await userHelpers.completeProfileSetup(userId, profileData)
      
      toast({
        title: "성공",
        description: "프로필 설정이 완료되었습니다"
      })
      
      return response.data.data
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '프로필 설정에 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
      return null
    } finally {
      setLoading(false)
    }
  }

  // 포지션별 사용자 검색 (편의 함수)
  const searchUsersByPosition = async (position: string): Promise<UserSearchResponse[]> => {
    try {
      setLoading(true)
      clearError()
      
      const response = await userHelpers.searchUsersByPosition(position)
      return response.data.data
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || '포지션별 검색에 실패했습니다'
      setError(errorMessage)
      toast({
        title: "오류",
        description: errorMessage,
        variant: "destructive"
      })
      return []
    } finally {
      setLoading(false)
    }
  }

  return {
    // 상태
    loading,
    error,
    
    // 프로필 관련
    getMyProfile,
    getUserProfile,
    updateUserProfile,
    deleteUserProfile,
    
    // 검색 관련
    searchUsersWithoutTeam: searchUsersWithoutTeamWithCondition,
    getWaitingUsers,
    searchUsersByPosition,
    
    // 편의 함수
    completeProfileSetup,
    
    // 유틸리티
    clearError,
    mapEnumToDisplayValue: userHelpers.mapEnumToDisplayValue,
    convertUserSearchEnums
  }
}

export default useUser