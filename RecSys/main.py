from fastapi import FastAPI
from app.api.routes import router
from app.core.config import add_cors, setup_logging


# 로깅 설정
setup_logging()

app = FastAPI(
    title="Team-Person Recommender API",
    description="AI 기반 팀-개인 추천 시스템",
    version="1.0.0"
)

# CORS 설정
add_cors(app)

# 라우터 연결
app.include_router(router)
