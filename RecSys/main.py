from fastapi import FastAPI
from app.api.routes import router
from fastapi.middleware.cors import CORSMiddleware
from app.core.config import setup_logging


# 로깅 설정
setup_logging()

app = FastAPI(
    title="Team-Person Recommender API",
    description="AI 기반 팀-개인 추천 시스템",
    version="1.0.0"
)

# CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=[
            "http://localhost:8081",  # Spring Boot 포트
            "http://127.0.0.1:8081",
            "http://localhost:8080",  # nginx 포트도 허용
            "*"  # 개발 중이라면 모든 origin 허용
    ],
    allow_credentials=True,
    allow_methods=["*"],  # 모든 HTTP 메서드 허용
    allow_headers=["*"],  # 모든 헤더 허용
)

# 라우터 연결
app.include_router(router)
