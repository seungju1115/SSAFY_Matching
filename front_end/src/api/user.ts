// 사용자 프로필 관련 API
import apiClient from './axios'
import type {
  UserSearchRequest,
  UserSearchResponse,
  UserProfileResponse,
  UserProfileUpdateRequest, UserStatus
} from "@/types/user.ts";

// API 응답을 위한 래퍼 타입
export interface ApiResponse<T> {
  data: {
    status: number
    message: string
    data: T
  }
}



export const userAPI = {
  /**
   * 내 프로필 조회
   * GET /users/profile
   */
  getMyProfile: (): Promise<ApiResponse<UserProfileResponse>> =>
    apiClient.get('/users/profile'),

  /**
   * 특정 사용자 프로필 조회
   * GET /users/profile/{id}
   */
  getUserProfile: (userId: number): Promise<ApiResponse<UserProfileResponse>> =>
    apiClient.get(`/users/profile/${userId}`),

  /**
   * 사용자 프로필 수정 (부분 수정)
   * PATCH /users/profile/{id}
   */
  updateUserProfile: (userId: number, updateData: UserProfileUpdateRequest): Promise<ApiResponse<UserProfileResponse>> =>
    apiClient.patch(`/users/profile/${userId}`, updateData),

  /**
   * 사용자 프로필 삭제
   * DELETE /users/profile/{id}
   */
  deleteUserProfile: (userId: number): Promise<ApiResponse<void>> =>
    apiClient.delete(`/users/profile/${userId}`),

  /**
   * 팀원 검색 (팀이 없는 사용자)
   * POST /users/profile/search
   */
  searchUsersWithoutTeam: (searchCriteria: UserSearchRequest): Promise<ApiResponse<UserSearchResponse[]>> =>
    apiClient.post('/users/profile/search', searchCriteria),

  /**
   * 대기중인 사용자 조회 (UserStatus가 WAITING인 사용자들)
   * GET /users/profile/waiting
   */
  getWaitingUsers: (): Promise<ApiResponse<UserSearchResponse[]>> =>
    apiClient.get('/users/profile/waiting')
}

// 편의를 위한 추가 함수들
export const userHelpers = {
  /**
   * 프로필 설정 완료 (ProfileSetup에서 사용)
   * 프론트엔드 ProfileSetupData를 백엔드 UserProfileUpdateRequest로 변환
   */
  completeProfileSetup: (userId: number, profileData: {
    positions: string[]
    skills: string[]
    introduction: string
    projectPreferences: string[]
    personalPreferences: string[]
    certifications: { name: string }[]
    userStatus: UserStatus
  }) => {
    // 포지션 문자열을 백엔드 Enum으로 매핑
    const positionMapping: Record<string, string> = {
      '백엔드': 'BACKEND',
      '프론트엔드': 'FRONTEND', 
      'AI': 'AI',
      '디자인': 'DESIGN',
      'PM': 'PM'
    }

    // 프로젝트 선호도 한글을 백엔드 Enum으로 매핑
    const projectGoalMapping: Record<string, string> = {
      '취업우선': 'JOB',
      '수상목표': 'AWARD', 
      '포트폴리오중심': 'PORTFOLIO',
      '학습중심': 'STUDY',
      '아이디어실현': 'IDEA',
      '실무경험': 'PROFESSIONAL',
      '빠른개발': 'QUICK',
      '완성도추구': 'QUALITY'
    }

    // 개인 성향 한글을 백엔드 Enum으로 매핑
    const projectViveMapping: Record<string, string> = {
      '반말 지향': 'CASUAL',
      '존대 지향': 'FORMAL',
      '편한 분위기': 'COMFY', 
      '규칙적인 분위기': 'RULE',
      '리더 중심': 'LEADER',
      '합의 중심': 'DEMOCRACY',
      '새로운 주제': 'BRANDNEW',
      '안정적인 주제': 'STABLE',
      '애자일 방식': 'AGILE',
      '워터폴 방식': 'WATERFALL'
    }

    // 기술스택 displayName을 백엔드 Enum으로 매핑 (일부만 표시, 실제로는 더 많음)
    const techStackMapping: Record<string, string> = {
      'React': 'REACT',
      'Vue.js': 'VUE_JS',
      'Angular': 'ANGULAR', 
      'Next.js': 'NEXT_JS',
      'TypeScript': 'TYPESCRIPT',
      'JavaScript': 'JAVASCRIPT',
      'HTML': 'HTML',
      'CSS': 'CSS',
      'SCSS': 'SCSS',
      'Tailwind CSS': 'TAILWIND_CSS',
      'Redux': 'REDUX',
      'Zustand': 'ZUSTAND',
      'Node.js': 'NODE_JS',
      'Express': 'EXPRESS',
      'Spring': 'SPRING',
      'Django': 'DJANGO',
      'Flask': 'FLASK',
      'NestJS': 'NESTJS',
      'Java': 'JAVA',
      'Python': 'PYTHON',
      'C#': 'CSHARP',
      'Go': 'GO',
      'Ruby on Rails': 'RUBY_ON_RAILS',
      'PHP': 'PHP',
      'JPA': 'JPA',
      'MySQL': 'MYSQL',
      'PostgreSQL': 'POSTGRESQL',
      'MongoDB': 'MONGODB',
      'Redis': 'REDIS',
      'Firebase': 'FIREBASE',
      'Oracle': 'ORACLE',
      'SQL Server': 'SQL_SERVER',
      'DynamoDB': 'DYNAMODB',
      'Elasticsearch': 'ELASTICSEARCH',
      'Docker': 'DOCKER',
      'Kubernetes': 'KUBERNETES',
      'AWS': 'AWS',
      'Azure': 'AZURE',
      'GCP': 'GCP',
      'Jenkins': 'JENKINS',
      'GitHub Actions': 'GITHUB_ACTIONS',
      'Terraform': 'TERRAFORM',
      'Ansible': 'ANSIBLE',
      'Prometheus': 'PROMETHEUS',
      'React Native': 'REACT_NATIVE',
      'Flutter': 'FLUTTER',
      'Swift': 'SWIFT',
      'Kotlin': 'KOTLIN',
      'Android': 'ANDROID',
      'iOS': 'IOS',
      'Xamarin': 'XAMARIN',
      'Ionic': 'IONIC',
      'TensorFlow': 'TENSORFLOW',
      'PyTorch': 'PYTORCH',
      'Scikit-learn': 'SCIKIT_LEARN',
      'OpenCV': 'OPENCV',
      'NLP': 'NLP',
      'Computer Vision': 'COMPUTER_VISION',
      'Machine Learning': 'MACHINE_LEARNING',
      'Deep Learning': 'DEEP_LEARNING'
    }

    const updateRequest: UserProfileUpdateRequest = {
      userProfile: profileData.introduction,
      wantedPosition: profileData.positions.map(pos => positionMapping[pos] || pos),
      techStack: profileData.skills.map(skill => techStackMapping[skill] || skill),
      projectGoal: profileData.projectPreferences.map(pref => projectGoalMapping[pref] || pref),
      projectVive: profileData.personalPreferences.map(pref => projectViveMapping[pref] || pref),
      qualification: profileData.certifications.map(cert => cert.name).join(', '),
      userStatus: profileData.userStatus
    }
    
    return userAPI.updateUserProfile(userId, updateRequest)
  },

  /**
   * 포지션별 사용자 검색
   */
  searchUsersByPosition: (position: string) =>
    userAPI.searchUsersWithoutTeam({ wantedPosition: [position] }),

  /**
   * 기술스택별 사용자 검색
   */
  searchUsersByTechStack: (techStack: string[]) =>
    userAPI.searchUsersWithoutTeam({ techStack }),

  /**
   * 프로젝트 선호도별 사용자 검색
   */
  searchUsersByProjectGoal: (projectGoal: string[]) =>
    userAPI.searchUsersWithoutTeam({ projectGoal }),

  /**
   * 백엔드 Enum을 프론트엔드 문자열로 역매핑
   */
  mapEnumToDisplayValue: (enumValue: string, type: 'position' | 'projectGoal' | 'projectVive' | 'techStack'): string => {
    const reverseMappings = {
      position: {
        'BACKEND': '백엔드',
        'FRONTEND': '프론트엔드',
        'AI': 'AI',
        'DESIGN': '디자인',
        'PM': 'PM'
      },
      projectGoal: {
        'JOB': '취업우선',
        'AWARD': '수상목표',
        'PORTFOLIO': '포트폴리오중심',
        'STUDY': '학습중심',
        'IDEA': '아이디어실현',
        'PROFESSIONAL': '실무경험',
        'QUICK': '빠른개발',
        'QUALITY': '완성도추구'
      },
      projectVive: {
        'CASUAL': '반말 지향',
        'FORMAL': '존대 지향',
        'COMFY': '편한 분위기',
        'RULE': '규칙적인 분위기',
        'LEADER': '리더 중심',
        'DEMOCRACY': '합의 중심',
        'BRANDNEW': '새로운 주제',
        'STABLE': '안정적인 주제',
        'AGILE': '애자일 방식',
        'WATERFALL': '워터폴 방식'
      },
      techStack: {
        'REACT': 'React',
        'VUE_JS': 'Vue.js',
        'ANGULAR': 'Angular',
        'NEXT_JS': 'Next.js',
        'TYPESCRIPT': 'TypeScript',
        'JAVASCRIPT': 'JavaScript',
        'HTML': 'HTML',
        'CSS': 'CSS',
        'SCSS': 'SCSS',
        'TAILWIND_CSS': 'Tailwind CSS',
        'REDUX': 'Redux',
        'ZUSTAND': 'Zustand',
        'NODE_JS': 'Node.js',
        'EXPRESS': 'Express',
        'SPRING': 'Spring',
        'DJANGO': 'Django',
        'FLASK': 'Flask',
        'NESTJS': 'NestJS',
        'JAVA': 'Java',
        'PYTHON': 'Python',
        'CSHARP': 'C#',
        'GO': 'Go',
        'RUBY_ON_RAILS': 'Ruby on Rails',
        'PHP': 'PHP',
        'JPA': 'JPA',
        'MYSQL': 'MySQL',
        'POSTGRESQL': 'PostgreSQL',
        'MONGODB': 'MongoDB',
        'REDIS': 'Redis',
        'FIREBASE': 'Firebase',
        'ORACLE': 'Oracle',
        'SQL_SERVER': 'SQL Server',
        'DYNAMODB': 'DynamoDB',
        'ELASTICSEARCH': 'Elasticsearch',
        'DOCKER': 'Docker',
        'KUBERNETES': 'Kubernetes',
        'AWS': 'AWS',
        'AZURE': 'Azure',
        'GCP': 'GCP',
        'JENKINS': 'Jenkins',
        'GITHUB_ACTIONS': 'GitHub Actions',
        'TERRAFORM': 'Terraform',
        'ANSIBLE': 'Ansible',
        'PROMETHEUS': 'Prometheus',
        'REACT_NATIVE': 'React Native',
        'FLUTTER': 'Flutter',
        'SWIFT': 'Swift',
        'KOTLIN': 'Kotlin',
        'ANDROID': 'Android',
        'IOS': 'iOS',
        'XAMARIN': 'Xamarin',
        'IONIC': 'Ionic',
        'TENSORFLOW': 'TensorFlow',
        'PYTORCH': 'PyTorch',
        'SCIKIT_LEARN': 'Scikit-learn',
        'OPENCV': 'OpenCV',
        'NLP': 'NLP',
        'COMPUTER_VISION': 'Computer Vision',
        'MACHINE_LEARNING': 'Machine Learning',
        'DEEP_LEARNING': 'Deep Learning'
      }
    }

    return reverseMappings[type][enumValue as keyof typeof reverseMappings[typeof type]] || enumValue
  }
}