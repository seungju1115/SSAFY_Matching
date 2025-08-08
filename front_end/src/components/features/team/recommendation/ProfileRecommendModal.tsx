import React from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Separator } from '@/components/ui/separator';
import { ScrollArea } from '@/components/ui/scroll-area';
import { X, UserPlus, Star, MapPin } from 'lucide-react';
import { cn } from '@/lib/utils';
import type { ProfileData } from './RecommendedProfileCard';

interface ProfileRecommendModalProps {
  isOpen: boolean;
  onClose: () => void;
  onInvite?: (profile: ProfileData) => void;
  profiles: ProfileData[];
}

// 역할별 색상 매핑
const roleColors = {
  backend: 'bg-slate-100 text-slate-700 border-slate-200',
  frontend: 'bg-emerald-100 text-emerald-700 border-emerald-200',
  ai: 'bg-violet-100 text-violet-700 border-violet-200',
  pm: 'bg-amber-100 text-amber-700 border-amber-200',
  design: 'bg-rose-100 text-rose-700 border-rose-200'
};

/**
 * 깔끔한 팀원 추천 카드 팝업 컴포넌트
 * 상자에서 팝 하고 나오는 듯한 애니메이션
 */
const ProfileRecommendModal: React.FC<ProfileRecommendModalProps> = ({
  isOpen,
  onClose,
  onInvite,
  profiles
}) => {
  const handleInvite = (profile: ProfileData) => {
    if (onInvite) {
      onInvite(profile);
    }
    onClose();
  };

  return (
    <AnimatePresence>
      {isOpen && (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-2 sm:p-4"
          onClick={onClose}
        >
          <motion.div
            initial={{ scale: 0.9, opacity: 0, y: 20 }}
            animate={{ scale: 1, opacity: 1, y: 0 }}
            exit={{ scale: 0.9, opacity: 0, y: 20 }}
            transition={{ type: "spring", damping: 25, stiffness: 300 }}
            className="relative w-full max-w-5xl h-full sm:h-auto sm:max-h-[90vh] flex flex-col"
            onClick={(e: React.MouseEvent) => e.stopPropagation()}
          >
            {/* 닫기 버튼 */}
            <Button
              variant="ghost"
              size="icon"
              className="absolute -top-12 right-0 text-white hover:bg-white/20 z-10"
              onClick={onClose}
            >
              <X className="h-5 w-5" />
            </Button>

            {/* 카드 컨테이너 */}
            <ScrollArea className="flex-grow rounded-lg overflow-hidden">
              <div className="p-4 sm:p-6">
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 sm:gap-6">
                {profiles.map((profile, index) => (
                  <motion.div
                    key={profile.id}
                    initial={{ scale: 0.9, opacity: 0, y: 10 }}
                    animate={{ scale: 1, opacity: 1, y: 0 }}
                    transition={{ 
                      delay: index * 0.08,
                      type: "spring",
                      damping: 25,
                      stiffness: 400
                    }}
                    whileHover={{ scale: 1.02, y: -3, transition: { duration: 0.2 } }}
                    className="w-full"
                  >
                    <Card className="bg-white hover:shadow-xl transition-all duration-200 overflow-hidden h-full border border-gray-100 hover:border-gray-200">
                      <CardHeader className="pb-3">
                        <div className="flex items-start justify-between gap-3">
                          <div className="flex items-center space-x-3">
                            <Avatar className="h-14 w-14 sm:h-16 sm:w-16 ring-2 ring-offset-2 ring-gray-100">
                              <AvatarImage src={profile.avatar} />
                              <AvatarFallback className="bg-gradient-to-br from-blue-500 to-purple-600 text-white font-bold text-lg">
                                {profile.name.charAt(0)}
                              </AvatarFallback>
                            </Avatar>
                            <div className="flex-1 min-w-0">
                              <CardTitle className="text-lg sm:text-xl leading-tight truncate">
                                {profile.name}
                              </CardTitle>
                              <CardDescription className="text-sm sm:text-base mt-1">
                                {profile.position}
                              </CardDescription>
                            </div>
                          </div>
                          <div className="flex items-center space-x-1 bg-green-50 px-2 py-1 rounded-full">
                            <Star className="h-3 w-3 text-green-600 fill-current" />
                            <span className="text-xs font-bold text-green-700">
                              {profile.matching}%
                            </span>
                          </div>
                        </div>
                      </CardHeader>
                      
                      <CardContent className="pt-0 space-y-4">
                        {/* 도메인 */}
                        {profile.domain && (
                          <div className="flex items-center space-x-2 text-sm text-gray-600">
                            <MapPin className="h-4 w-4 text-blue-500" />
                            <span>{profile.domain}</span>
                          </div>
                        )}
                        
                        {/* 기술스택 */}
                        <div>
                          <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-2">기술스택</p>
                          <div className="flex flex-wrap gap-1.5">
                            {profile.skills.map((skill) => (
                              <Badge
                                key={skill}
                                variant="outline"
                                className="text-xs font-medium bg-gray-50 hover:bg-gray-100"
                              >
                                {skill}
                              </Badge>
                            ))}
                          </div>
                        </div>

                        {/* 선호 성향 */}
                        {(profile.projectPreferences || profile.personalPreferences) && (
                          <div className="space-y-2">
                            {profile.projectPreferences && (
                              <div>
                                <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1">프로젝트 성향</p>
                                <p className="text-sm text-gray-700 leading-relaxed">
                                  {profile.projectPreferences.join(' • ')}
                                </p>
                              </div>
                            )}
                            {profile.personalPreferences && (
                              <div>
                                <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1">개인 성향</p>
                                <p className="text-sm text-gray-700 leading-relaxed">
                                  {profile.personalPreferences.join(' • ')}
                                </p>
                              </div>
                            )}
                          </div>
                        )}

                        {/* 자기소개 */}
                        <div>
                          <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-2">자기소개</p>
                          <p className="text-sm text-gray-700 leading-relaxed line-clamp-3 bg-gray-50 p-3 rounded-md">
                            {profile.description}
                          </p>
                        </div>

                        <Separator />

                        {/* 하단 액션 */}
                        <div className="flex items-center justify-between pt-2">
                          <Badge 
                            className={cn(
                              "font-medium text-xs px-2 py-1",
                              roleColors[profile.role as keyof typeof roleColors]
                            )}
                          >
                            {profile.role.toUpperCase()}
                          </Badge>
                          
                          <Button
                            onClick={() => handleInvite(profile)}
                            size="sm"
                            className="text-xs px-4 bg-blue-600 hover:bg-blue-700 text-white"
                          >
                            <UserPlus className="w-3 h-3 mr-1" />
                            초대하기
                          </Button>
                        </div>
                      </CardContent>
                    </Card>
                  </motion.div>
                ))}
              </div>
            </div>
            </ScrollArea>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
};

export default ProfileRecommendModal;
