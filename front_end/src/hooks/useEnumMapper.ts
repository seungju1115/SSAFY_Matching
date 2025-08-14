// Enum → 한글 매핑 커스텀 훅
import type { ProjectGoalEnum, ProjectViveEnum } from '@/types/team'
import type { TechEnum, PositionEnum } from '@/types/user'

// 매핑 테이블들
const projectGoalLabels: Record<ProjectGoalEnum, string> = {
  JOB: '취업우선',
  AWARD: '수상목표', 
  PORTFOLIO: '포트폴리오중심',
  STUDY: '학습중심',
  IDEA: '아이디어실현',
  PROFESSIONAL: '실무경험',
  QUICK: '빠른개발',
  QUALITY: '완성도추구'
}

const projectVibeLabels: Record<ProjectViveEnum, string> = {
  CASUAL: '반말 지향',
  FORMAL: '존대 지향',
  COMFY: '편한 분위기',
  RULE: '규칙적인 분위기',
  LEADER: '리더 중심',
  DEMOCRACY: '합의 중심',
  BRANDNEW: '새로운 주제',
  STABLE: '안정적인 주제',
  AGILE: '애자일 방식',
  WATERFALL: '워터폴 방식'
}

const techStackLabels: Record<TechEnum, string> = {
  // Frontend
  REACT: 'React', VUE_JS: 'Vue.js', ANGULAR: 'Angular', NEXT_JS: 'Next.js',
  TYPESCRIPT: 'TypeScript', JAVASCRIPT: 'JavaScript', HTML: 'HTML', CSS: 'CSS',
  SCSS: 'SCSS', TAILWIND_CSS: 'Tailwind CSS', REDUX: 'Redux', ZUSTAND: 'Zustand',
  
  // Backend
  NODE_JS: 'Node.js', EXPRESS: 'Express', SPRING: 'Spring', DJANGO: 'Django',
  FLASK: 'Flask', NESTJS: 'NestJS', JAVA: 'Java', PYTHON: 'Python',
  CSHARP: 'C#', GO: 'Go', RUBY_ON_RAILS: 'Ruby on Rails', PHP: 'PHP', JPA: 'JPA',
  
  // Database
  MYSQL: 'MySQL', POSTGRESQL: 'PostgreSQL', MONGODB: 'MongoDB', REDIS: 'Redis',
  FIREBASE: 'Firebase', ORACLE: 'Oracle', SQL_SERVER: 'SQL Server',
  DYNAMODB: 'DynamoDB', ELASTICSEARCH: 'Elasticsearch',
  
  // DevOps
  DOCKER: 'Docker', KUBERNETES: 'Kubernetes', AWS: 'AWS', AZURE: 'Azure',
  GCP: 'GCP', JENKINS: 'Jenkins', GITHUB_ACTIONS: 'GitHub Actions',
  TERRAFORM: 'Terraform', ANSIBLE: 'Ansible', PROMETHEUS: 'Prometheus',
  
  // Mobile
  REACT_NATIVE: 'React Native', FLUTTER: 'Flutter', SWIFT: 'Swift',
  KOTLIN: 'Kotlin', ANDROID: 'Android', IOS: 'iOS', XAMARIN: 'Xamarin', IONIC: 'Ionic',
  
  // AI
  TENSORFLOW: 'TensorFlow', PYTORCH: 'PyTorch', SCIKIT_LEARN: 'scikit-learn',
  OPENCV: 'OpenCV', NLP: 'NLP', COMPUTER_VISION: 'Computer Vision',
  MACHINE_LEARNING: 'Machine Learning', DEEP_LEARNING: 'Deep Learning'
}

const positionLabels: Record<PositionEnum, string> = {
  BACKEND: '백엔드',
  FRONTEND: '프론트엔드',
  AI: 'AI',
  PM: 'PM',
  DESIGN: '디자인'
}

export const useEnumMapper = () => {
  // 개별 변환 함수들
  const mapProjectGoal = (enumValue: ProjectGoalEnum): string => {
    return projectGoalLabels[enumValue] || enumValue
  }

  const mapProjectVibe = (enumValue: ProjectViveEnum): string => {
    return projectVibeLabels[enumValue] || enumValue
  }

  const mapTechStack = (enumValue: TechEnum): string => {
    return techStackLabels[enumValue] || enumValue
  }

  const mapPosition = (enumValue: PositionEnum): string => {
    return positionLabels[enumValue] || enumValue
  }

  // 배열 변환 함수들
  const mapProjectGoalArray = (enumArray?: ProjectGoalEnum[]): string[] => {
    return enumArray?.map(mapProjectGoal) || []
  }

  const mapProjectVibeArray = (enumArray?: ProjectViveEnum[]): string[] => {
    return enumArray?.map(mapProjectVibe) || []
  }

  const mapTechStackArray = (enumArray?: TechEnum[]): string[] => {
    return enumArray?.map(mapTechStack) || []
  }

  const mapPositionArray = (enumArray?: PositionEnum[]): string[] => {
    return enumArray?.map(mapPosition) || []
  }

  // 사용자 데이터 전체 변환 함수
  const convertUserData = (userData: any) => {
    return {
      ...userData,
      projectGoal: mapProjectGoalArray(userData.projectGoal),
      projectVive: mapProjectVibeArray(userData.projectVive),
      techStack: mapTechStackArray(userData.techStack),
      wantedPosition: mapPositionArray(userData.wantedPosition)
    }
  }

  // 매핑 테이블들도 export (다른 곳에서 직접 사용하고 싶을 때)
  const mappingTables = {
    projectGoalLabels,
    projectVibeLabels,
    techStackLabels,
    positionLabels
  }

  return {
    // 개별 변환 함수들
    mapProjectGoal,
    mapProjectVibe,
    mapTechStack,
    mapPosition,
    
    // 배열 변환 함수들
    mapProjectGoalArray,
    mapProjectVibeArray,
    mapTechStackArray,
    mapPositionArray,
    
    // 전체 변환 함수
    convertUserData,
    
    // 매핑 테이블들
    mappingTables
  }
}