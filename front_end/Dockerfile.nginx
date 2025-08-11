# 멀티 스테이지 빌드 - 빌드 스테이지
FROM node:22-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 필수 시스템 패키지 설치 (빌드 최적화)
RUN apk add --no-cache libc6-compat

# package.json과 package-lock.json 복사 (의존성 캐싱 최적화)
COPY package*.json ./

# 의존성 설치 (npm ci 사용으로 더 빠르고 안정적인 설치)
RUN npm ci --only=production --silent && npm cache clean --force

# 개발 의존성 설치 (빌드에 필요)
RUN npm ci --silent

# 소스 코드 복사 (의존성 설치 후에 복사하여 캐시 효율성 향상)
COPY . .

# TypeScript 컴파일 및 Vite 빌드
# shadcn/ui, Tailwind CSS, TypeScript 등 모든 새 라이브러리 포함
RUN npm run build

# 빌드 결과물 확인 (디버깅용)
RUN ls -la /app/dist

# 프로덕션 스테이지 - Nginx 서버
FROM nginx:alpine AS production

# 보안을 위한 nginx 사용자 생성
RUN addgroup -g 1001 -S nodejs && adduser -S nextjs -u 1001

# 빌드된 파일을 nginx 웹 루트로 복사
COPY --from=builder /app/dist /usr/share/nginx/html

# Nginx 설정 파일 복사 (SPA 라우팅 지원)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 정적 파일 권한 설정
RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chmod -R 755 /usr/share/nginx/html

# 헬스체크 추가
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost/ || exit 1

# 포트 노출
EXPOSE 80

# nginx 실행
CMD ["nginx", "-g", "daemon off;"] 