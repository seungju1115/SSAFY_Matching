// 편집기(호스트)에서 Docker 컨테이너 안의 node_modules 타입을 찾지 못해
// 발생하는 "Cannot find module 'framer-motion'" 에러를 임시로 무력화하는 선언입니다.
// 런타임은 컨테이너 내 실제 framer-motion 패키지를 사용합니다.
declare module 'framer-motion' {
  // 주요 named export 들을 any 로 선언 (필요 시 추가 가능)
  export const motion: any;
  export const AnimatePresence: any;
  export const useMotionValue: any;
  export const useTransform: any;
  export const useSpring: any;
  export const m: any;
  const _default: any;
  export default _default;
}
