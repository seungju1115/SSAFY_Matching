from pydantic import BaseModel
from typing import List, Optional

class Person(BaseModel):
    user_id: int
    name: Optional[str] = None
    main_pos: str
    sub_pos: str
    goals_: List[str]
    vibes_: List[str]

class Team(BaseModel):
    team_id: int
    team_name: Optional[str] = None
    recruit_positions: List[str]
    goals: List[str]
    vibes: List[str]

class CandidateRecommendRequest(BaseModel):
    team_info: Team
    member_infos: List[Person]
    candidate_pool: List[Person]
    alpha: Optional[float] = 0.5
    top_k: Optional[int] = 5

class TeamRecommendRequest(BaseModel):
    person: Person
    team_pool: List[Team]
    team_members_map: List[List[Person]]
    alpha: Optional[float] = 0.5
    top_k: Optional[int] = 5
