import React from 'react';
import { motion } from 'framer-motion';

interface SpinningEffectProps {
  isActive: boolean;
  children?: React.ReactNode;
}

/**
 * 뱀파이어 서바이벌 스타일의 회전/깜빡임 애니메이션 효과 컴포넌트
 * isActive가 true일 때 자식 컴포넌트에 애니메이션 효과를 적용
 */
const SpinningEffect: React.FC<SpinningEffectProps> = ({ isActive, children }) => {
  if (!isActive) return <>{children}</>;
  
  return (
    <motion.div
      animate={{ 
        rotate: [-2, 0, 2, 0], 
      }}
      transition={{ 
        repeat: Infinity, 
        duration: 2,
        ease: "easeInOut" 
      }}
      className="relative"
    >
      {children}
      <motion.div
        className="absolute inset-0"
        animate={{
          boxShadow: [
            '0 0 0px rgba(59, 130, 246, 0)',
            '0 0 15px rgba(59, 130, 246, 0.6)',
            '0 0 0px rgba(59, 130, 246, 0)'
          ]
        }}
        transition={{ repeat: Infinity, duration: 1.5 }}
      />
    </motion.div>
  );
};

export default SpinningEffect;
