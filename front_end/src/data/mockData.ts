import type { Team } from "@/components/features/home/TeamSection"
import type { Developer } from "@/components/features/home/DeveloperSection"

// 팀 더미 데이터
export const mockTeams: Team[] = [
  {
    id: 1,
    name: "React 프로젝트 팀",
    description: "현대적인 웹 애플리케이션을 함께 만들어요!",
    tech: ["React", "TypeScript", "Node.js"],
    members: 3,
    maxMembers: 5,
    deadline: "2024-02-15",
    roleDistribution: {
      backend: 2,
      frontend: 2,
      ai: 0,
      design: 1,
      pm: 0
    },
    roleCurrent: {
      backend: 1,
      frontend: 1,
      ai: 0,
      design: 1,
      pm: 0
    },
    leader: {
      name: "김개발",
      avatar: "",
      role: "Frontend Developer"
    },
    domains: ["웹 서비스", "커머스"],
    teamAtmosphere: ["자율", "꾸준함", "소통"],
    introduction: "React/TS 기반으로 안정적인 웹 서비스를 함께 개발할 팀원을 찾습니다."
  },
  {
    id: 2,
    name: "AI 스타트업 프로젝트",
    description: "머신러닝을 활용한 혁신적인 서비스 개발",
    tech: ["Python", "TensorFlow", "FastAPI"],
    members: 2,
    maxMembers: 4,
    deadline: "2024-03-01",
    roleDistribution: {
      backend: 1,
      frontend: 0,
      ai: 2,
      design: 0,
      pm: 1
    },
    roleCurrent: {
      backend: 1,
      frontend: 0,
      ai: 1,
      design: 0,
      pm: 0
    },
    leader: {
      name: "박AI",
      avatar: "",
      role: "ML Engineer"
    },
    domains: ["AI", "데이터 분석"],
    teamAtmosphere: ["도전", "연구", "집중"],
    introduction: "실험과 학습을 중시하는 분위기에서 프로토타입을 빠르게 만들어봅니다."
  },
  {
    id: 3,
    name: "모바일 앱 개발팀",
    description: "크로스 플랫폼 모바일 앱 제작",
    tech: ["Flutter", "Firebase", "Dart"],
    members: 4,
    maxMembers: 6,
    deadline: "2024-02-28",
    roleDistribution: {
      backend: 2,
      frontend: 2,
      ai: 0,
      design: 1,
      pm: 1
    },
    roleCurrent: {
      backend: 1,
      frontend: 2,
      ai: 0,
      design: 1,
      pm: 0
    },
    leader: {
      name: "이모바일",
      avatar: "",
      role: "Mobile Developer"
    },
    domains: ["모바일", "유틸리티"],
    teamAtmosphere: ["협업", "속도", "유연"],
    introduction: "Flutter로 사용자 경험이 뛰어난 모바일 앱을 빠르게 출시합니다."
  }
]

// 개발자 더미 데이터
import type {Developer} from "@/components/features/home/DeveloperSection.tsx";

export const mockDevelopers: Developer[] = [
  {
    id: 1,
    name: "김철수",
    role: "백엔드",
    avatar: "",
    isMajor: true,
    positions: ["백엔드", "디자인"],
    projectPreferences: ["취업우선", "수상목표"],
    techStack: [
      { name: "Java", level: 90 },
      { name: "Spring", level: 85 },
      { name: "MySQL", level: 80 },
      { name: "Docker", level: 70 },
      { name: "AWS", level: 65 }
    ]
  },
  {
    id: 2,
    name: "이영희",
    role: "프론트엔드",
    avatar: "",
    isMajor: false,
    positions: ["프론트엔드", "AI"],
    projectPreferences: ["학습중심", "포트폴리오중심"],
    techStack: [
      { name: "React", level: 95 },
      { name: "TypeScript", level: 88 },
      { name: "Next.js", level: 82 },
      { name: "Tailwind", level: 90 },
      { name: "Python", level: 75 }
    ]
  },
  {
    id: 3,
    name: "박민수",
    role: "AI",
    avatar: "",
    isMajor: true,
    positions: ["AI", "백엔드"],
    projectPreferences: ["아이디어실현", "실무경험"],
    techStack: [
      { name: "Python", level: 95 },
      { name: "TensorFlow", level: 88 },
      { name: "PyTorch", level: 85 },
      { name: "FastAPI", level: 80 },
      { name: "PostgreSQL", level: 75 }
    ]
  },
  {
    id: 4,
    name: "최지은",
    role: "디자인",
    avatar: "",
    isMajor: false,
    positions: ["디자인", "PM"],
    projectPreferences: ["완성도추구", "빠른개발"],
    techStack: [
      { name: "Figma", level: 95 },
      { name: "Photoshop", level: 90 },
      { name: "Illustrator", level: 85 },
      { name: "Sketch", level: 80 },
      { name: "Framer", level: 70 }
    ]
  },
  {
    id: 5,
    name: "정우진",
    role: "PM",
    avatar: "",
    isMajor: true,
    positions: ["PM", "프론트엔드"],
    projectPreferences: ["취업우선", "학습중심"],
    techStack: [
      { name: "Notion", level: 95 },
      { name: "Jira", level: 88 },
      { name: "Slack", level: 90 },
      { name: "React", level: 75 },
      { name: "Git", level: 85 }
    ]
  },
  {
    id: 6,
    name: "한지민",
    role: "백엔드",
    avatar: "",
    isMajor: true,
    positions: ["백엔드"],
    projectPreferences: ["포트폴리오중심", "완성도추구"],
    techStack: [
      { name: "Node.js", level: 92 },
      { name: "Express", level: 88 },
      { name: "MongoDB", level: 85 },
      { name: "Redis", level: 78 },
      { name: "GraphQL", level: 72 }
    ]
  }
]