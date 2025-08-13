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
    leader: {
      name: "김개발",
      avatar: "",
      role: "Frontend Developer"
    }
  },
  {
    id: 2,
    name: "AI 스타트업 프로젝트",
    description: "머신러닝을 활용한 혁신적인 서비스 개발",
    tech: ["Python", "TensorFlow", "FastAPI"],
    members: 2,
    maxMembers: 4,
    deadline: "2024-03-01",
    leader: {
      name: "박AI",
      avatar: "",
      role: "ML Engineer"
    }
  },
  {
    id: 3,
    name: "모바일 앱 개발팀",
    description: "크로스 플랫폼 모바일 앱 제작",
    tech: ["Flutter", "Firebase", "Dart"],
    members: 4,
    maxMembers: 6,
    deadline: "2024-02-28",
    leader: {
      name: "이모바일",
      avatar: "",
      role: "Mobile Developer"
    }
  }
]

// 개발자 더미 데이터
export const mockDevelopers: Developer[] = [
  {
    id: 1,
    name: "최프론트",
    role: "Frontend Developer",
    experience: "3년",
    skills: ["React", "Vue.js", "TypeScript", "CSS"],
    location: "서울",
    rating: 4.8,
    projects: 12,
    avatar: "",
    bio: "사용자 경험을 중시하는 프론트엔드 개발자입니다."
  },
  {
    id: 2,
    name: "정백엔드",
    role: "Backend Developer", 
    experience: "5년",
    skills: ["Node.js", "Python", "PostgreSQL", "AWS"],
    location: "부산",
    rating: 4.9,
    projects: 20,
    avatar: "",
    bio: "확장 가능한 서버 아키텍처 설계 전문가"
  },
  {
    id: 3,
    name: "한풀스택",
    role: "Full Stack Developer",
    experience: "4년", 
    skills: ["React", "Node.js", "MongoDB", "Docker"],
    location: "대구",
    rating: 4.7,
    projects: 15,
    avatar: "",
    bio: "프론트엔드부터 백엔드까지 전체 개발 가능"
  },
  {
    id: 4,
    name: "김디자인",
    role: "UI/UX Designer",
    experience: "2년",
    skills: ["Figma", "Adobe XD", "Sketch", "Prototyping"],
    location: "서울",
    rating: 4.6,
    projects: 8,
    avatar: "",
    bio: "사용자 중심의 디자인을 추구합니다"
  },
  {
    id: 5,
    name: "박데이터",
    role: "Data Scientist",
    experience: "3년",
    skills: ["Python", "R", "TensorFlow", "SQL"],
    location: "서울",
    rating: 4.8,
    projects: 10,
    avatar: "",
    bio: "데이터로 인사이트를 찾는 것을 좋아합니다"
  },
  {
    id: 6,
    name: "이DevOps",
    role: "DevOps Engineer",
    experience: "6년",
    skills: ["AWS", "Docker", "Kubernetes", "Jenkins"],
    location: "인천",
    rating: 4.9,
    projects: 25,
    avatar: "",
    bio: "안정적인 인프라 구축 및 자동화 전문"
  }
]