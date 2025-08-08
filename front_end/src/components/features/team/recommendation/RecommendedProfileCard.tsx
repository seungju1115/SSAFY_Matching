import React from 'react';
import { motion } from 'framer-motion';
import { Badge } from '@/components/ui/badge';
import { cn } from '@/lib/utils';

// 역할별 색상 매핑
const roleColors = {
  backend: 'bg-slate-100 text-slate-700 border-slate-200',
  frontend: 'bg-emerald-100 text-emerald-700 border-emerald-200',
  ai: 'bg-violet-100 text-violet-700 border-violet-200',
  pm: 'bg-amber-100 text-amber-700 border-amber-200',
  design: 'bg-rose-100 text-rose-700 border-rose-200'
};

export interface ProfileData {
  id: number;
  name: string;
  position: string;
  role: string;
  domain?: string;
  skills: string[];
  projectPreferences?: string[];
  personalPreferences?: string[];
  description: string;
  avatar: string;
  matching: number;
}

interface RecommendedProfileCardProps {
  profile: ProfileData;
  index: number;
  isSelected: boolean;
  isSpinning: boolean;
  onClick: () => void;
}

const RecommendedProfileCard: React.FC<RecommendedProfileCardProps> = ({
  profile,
  index,
  isSelected,
  isSpinning,
  onClick
}) => {
  return (
    <motion.div
      className={`relative w-[250px] h-[350px] ${isSelected ? 'ring-4 ring-blue-500' : ''}`}
      initial={{ opacity: 0, y: 20 }}
      animate={{
        opacity: 1,
        y: 0,
        rotate: isSpinning ? [(index % 2 === 0 ? 5 : -5), 0, (index % 2 === 0 ? -5 : 5)] : 0,
        scale: isSelected ? 1.05 : 1
      }}
      transition={{
        rotate: { repeat: isSpinning ? Infinity : 0, duration: 1.5 },
        scale: { duration: 0.3 }
      }}
      onClick={onClick}
    >
      {/* 카드 내용 */}
      <div className="w-full h-full bg-gradient-to-b from-gray-700 to-gray-900 rounded-lg overflow-hidden flex flex-col shadow-lg cursor-pointer hover:shadow-xl transition-shadow">
        {/* 프로필 헤더 */}
        <div className="p-4 bg-gradient-to-r from-blue-600 to-purple-600 flex flex-col items-center">
          <div className="w-20 h-20 bg-gray-200 rounded-full mb-3 overflow-hidden border-4 border-white">
            {/* 프로필 이미지 */}
            <div className="w-full h-full flex items-center justify-center text-2xl font-bold bg-gradient-to-br from-blue-400 to-purple-500 text-white">
              {profile.name.charAt(0)}
            </div>
          </div>
          <h3 className="text-xl font-bold text-white mb-1">{profile.name}</h3>
          <span className="text-blue-200 text-sm">{profile.position}</span>
          
          {/* 매칭률 표시 */}
          <div className="absolute top-4 right-4 bg-blue-500 text-white text-sm font-bold px-2 py-1 rounded-full">
            {profile.matching}% 매칭
          </div>
        </div>
        
        {/* 프로필 내용 */}
        <div className="p-4 flex-1 flex flex-col justify-between">
          {/* 도메인 정보 */}
          {profile.domain && (
            <div className="mb-2">
              <p className="text-xs text-gray-400 mb-1">도메인</p>
              <p className="text-sm text-gray-300">{profile.domain}</p>
            </div>
          )}
          
          {/* 스킬 태그 */}
          <div className="mb-3">
            <p className="text-xs text-gray-400 mb-1">주요 스킬</p>
            <div className="flex flex-wrap gap-1">
              {profile.skills.map((skill) => (
                <span 
                  key={skill} 
                  className={`px-2 py-1 rounded-full text-xs font-medium ${roleColors[profile.role as keyof typeof roleColors] || 'bg-gray-600 text-white'}`}
                >
                  {skill}
                </span>
              ))}
            </div>
          </div>
          
          {/* 선호 프로젝트 성향 & 개인 성향 */}
          {(profile.projectPreferences || profile.personalPreferences) && (
            <div className="mb-3">
              {profile.projectPreferences && (
                <>
                  <p className="text-xs text-gray-400 mb-1">선호 프로젝트 성향</p>
                  <p className="text-sm text-gray-300 mb-2">{profile.projectPreferences.join(', ')}</p>
                </>
              )}
              {profile.personalPreferences && (
                <>
                  <p className="text-xs text-gray-400 mb-1">선호 개인 성향</p>
                  <p className="text-sm text-gray-300">{profile.personalPreferences.join(', ')}</p>
                </>
              )}
            </div>
          )}
          
          {/* 자기소개 */}
          <div>
            <p className="text-xs text-gray-400 mb-1">자기소개</p>
            <p className="text-sm text-gray-300">{profile.description}</p>
          </div>
        </div>
        
        {/* 카드 하단 */}
        <div className="px-4 py-3 border-t border-gray-700 bg-gray-800">
          <Badge 
            variant="outline"
            className={cn("w-full flex justify-center", roleColors[profile.role as keyof typeof roleColors])}
          >
            {profile.role.toUpperCase()}
          </Badge>
        </div>
      </div>
      
      {/* 스피닝 효과를 위한 하이라이트 */}
      {isSpinning && (
        <motion.div
          className="absolute inset-0 rounded-lg border-2 border-transparent"
          animate={{
            boxShadow: ['0 0 0px rgba(59, 130, 246, 0)', '0 0 20px rgba(59, 130, 246, 0.7)', '0 0 0px rgba(59, 130, 246, 0)'],
            borderColor: ['rgba(59, 130, 246, 0)', 'rgba(59, 130, 246, 0.7)', 'rgba(59, 130, 246, 0)']
          }}
          transition={{ repeat: Infinity, duration: 2 }}
        />
      )}
    </motion.div>
  );
};

export default RecommendedProfileCard;
