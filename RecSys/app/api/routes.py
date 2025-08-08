from fastapi import APIRouter, HTTPException
from app.schemas.models import CandidateRecommendRequest, TeamRecommendRequest
from app.services.recommender import recommend_candidates, recommend_teams
import logging

logger = logging.getLogger(__name__)

router = APIRouter(prefix="/recommend")  # ✅ 바로 이 router 객체가 main.py에서 import됨

@router.post("/candidates")
async def recommend_candidates_api(req: CandidateRecommendRequest):
    """후보자 추천 API"""
    try:
        logger.info(f"Received candidate recommendation request for team: {req.team_info.recruit_positions}")
        
        results = recommend_candidates(
            team_info=req.team_info.model_dump(),
            member_infos=[m.model_dump() for m in req.member_infos],
            candidate_pool=[c.model_dump() for c in req.candidate_pool],
            alpha=req.alpha,
            top_k=req.top_k
        )
        
        logger.info(f"Successfully recommended {len(results)} candidates")
        return {
            "status": "success",
            "message": f"Recommended {len(results)} candidates",
            "results": results
        }
        
    except Exception as e:
        logger.error(f"Error in candidate recommendation: {str(e)}")
        raise HTTPException(status_code=500, detail=f"Recommendation failed: {str(e)}")

@router.post("/teams")
async def recommend_teams_api(req: TeamRecommendRequest):
    """팀 추천 API"""
    try:
        logger.info(f"Received team recommendation request for person: {req.person.main_pos}")
        
        results = recommend_teams(
            person_info=req.person.model_dump(),
            team_pool=[t.model_dump() for t in req.team_pool],
            team_members_map=[[m.model_dump() for m in group] for group in req.team_members_map],
            alpha=req.alpha,
            top_k=req.top_k
        )
        
        logger.info(f"Successfully recommended {len(results)} teams")
        return {
            "status": "success",
            "message": f"Recommended {len(results)} teams",
            "results": results
        }
        
    except Exception as e:
        logger.error(f"Error in team recommendation: {str(e)}")
        raise HTTPException(status_code=500, detail=f"Recommendation failed: {str(e)}")

@router.get("/health")
async def health_check():
    """헬스체크 엔드포인트"""
    return {"status": "healthy", "service": "recommender"}