import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

positions = ["pm", "backend", "frontend", "design", "ai"]
GOALS_LIST = ["취업우선", "수상목표", "포트폴리오중심", "학습중심", "아이디어실현", "실무경험", "빠른개발", "완성도추구"]
VIBES_LIST = ["반말 지향", "존대 지향", "편한 분위기", "규칙적인 분위기", "리더 중심", "합의 중심", "새로운 주제", "안정적인 주제", "애자일 방식", "워터폴 방식"]

def multi_hot(values, category_list):
    vec = np.zeros(len(category_list))
    for v in values:
        if v in category_list:
            vec[category_list.index(v)] = 1
    return vec

def person_vector(user_id, name, main_pos, sub_pos, goals_, vibes_):
    # ID와 name은 무시하고 추천 로직에서는 사용하지 않음
    pos_vec = np.zeros(len(positions))
    if main_pos in positions:
        pos_vec[positions.index(main_pos)] += 0.7
    if sub_pos in positions:
        pos_vec[positions.index(sub_pos)] += 0.3
    goal_vec = multi_hot(goals_, GOALS_LIST)  # ← 전역 상수 사용
    vibe_vec = multi_hot(vibes_, VIBES_LIST)  # ← 전역 상수 사용
    return np.concatenate([pos_vec, goal_vec, vibe_vec])

def team_vector(team_id, team_name, recruit_positions, team_goals, team_vibes):  # ← 파라미터명 변경
    # ID와 name은 무시하고 추천 로직에서는 사용하지 않음
    pos_vec = multi_hot(recruit_positions, positions)
    goal_vec = multi_hot(team_goals, GOALS_LIST)  # ← 올바른 비교
    vibe_vec = multi_hot(team_vibes, VIBES_LIST)  # ← 올바른 비교
    return np.concatenate([pos_vec, goal_vec, vibe_vec])

def combined_team_vector(team_vec, member_vecs, alpha=0.5):
    member_mean = np.mean(member_vecs, axis=0)
    return alpha * team_vec + (1 - alpha) * member_mean

def recommend_candidates(team_info, member_infos, candidate_pool, alpha=0.5, top_k=5):
    team_vec = team_vector(
        team_id=team_info['team_id'],
        team_name=team_info['team_name'],
        recruit_positions=team_info['recruit_positions'],
        team_goals=team_info['goals'],  # ← 파라미터명 맞춤
        team_vibes=team_info['vibes']   # ← 파라미터명 맞춤
    )
    member_vecs = [person_vector(
        user_id=member['user_id'],
        name=member['name'],
        main_pos=member['main_pos'],
        sub_pos=member['sub_pos'],
        goals_=member['goals_'],
        vibes_=member['vibes_']
    ) for member in member_infos]
    combined_vec = combined_team_vector(team_vec, member_vecs, alpha)

    similarities = []
    for idx, candidate in enumerate(candidate_pool):
        cand_vec = person_vector(
            user_id=candidate['user_id'],
            name=candidate['name'],
            main_pos=candidate['main_pos'],
            sub_pos=candidate['sub_pos'],
            goals_=candidate['goals_'],
            vibes_=candidate['vibes_']
        )
        sim = cosine_similarity([combined_vec], [cand_vec])[0][0]
        similarities.append((idx, sim))

    top_k_results = sorted(similarities, key=lambda x: x[1], reverse=True)[:top_k]
    recommended = []
    for i, sim in top_k_results:
        candidate = candidate_pool[i].copy()
        candidate["similarity"] = round(sim, 4)
        recommended.append(candidate)
    
    return recommended

def recommend_teams(person_info, team_pool, team_members_map, alpha=0.5, top_k=5):
    person_vec = person_vector(
        user_id=person_info['user_id'],
        name=person_info['name'],
        main_pos=person_info['main_pos'],
        sub_pos=person_info['sub_pos'],
        goals_=person_info['goals_'],
        vibes_=person_info['vibes_']
    )
    results = []

    for i, (team, members) in enumerate(zip(team_pool, team_members_map)):
        team_vec = team_vector(
            team_id=team['team_id'],
            team_name=team['team_name'],
            recruit_positions=team['recruit_positions'],
            team_goals=team['goals'],    # ← 파라미터명 맞춤
            team_vibes=team['vibes']     # ← 파라미터명 맞춤
        )
        member_vecs = [person_vector(
            user_id=m['user_id'],
            name=m['name'],
            main_pos=m['main_pos'],
            sub_pos=m['sub_pos'],
            goals_=m['goals_'],
            vibes_=m['vibes_']
        ) for m in members]
        combined_vec = combined_team_vector(team_vec, member_vecs, alpha)
        sim = cosine_similarity([person_vec], [combined_vec])[0][0]
        results.append((i, sim))

    top_k_results = sorted(results, key=lambda x: x[1], reverse=True)[:top_k]
    recommended = [team_pool[i] | {"similarity": round(sim, 4)} for i, sim in top_k_results]
    return recommended