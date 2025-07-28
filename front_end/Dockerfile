# 멀티 스테이지 빌드 - 빌드 스테이지
FROM node:22-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /app

# package.json과 package-lock.json 복사
COPY package*.json ./

# 의존성 설치 (빌드에 필요한 devDependencies 포함)
RUN npm install

# 소스 코드 복사
COPY . .

# 빌드
RUN npm run build

# 프로덕션 스테이지 - Nginx 서버
FROM nginx:alpine

# 빌드된 파일을 nginx 웹 루트로 복사
COPY --from=builder /app/dist /usr/share/nginx/html

# nginx 설정 파일 복사 (선택사항)
# COPY nginx.conf /etc/nginx/nginx.conf

# 포트 노출
EXPOSE 80

# nginx 실행
CMD ["nginx", "-g", "daemon off;"] 