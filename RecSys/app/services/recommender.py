import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

positions = ["pm", "backend", "frontend", "design", "ai"]
GOALS_LIST = ["취업우선", "수상목표", "포트폴리오중심", "학습중심", "아이디어실현", "실무경험", "빠른개발", "완성도추구"]
VIBES_LIST = ["반말 지향", "존대 지향", "편한 분위기", "규칙적인 분위기", "리더 중심", "합의 중심", "새로운 주제", "안정적인 주제", "애자일 방식", "워터폴 방식"]

POS_DIM, GOAL_DIM, VIBE_DIM = len(positions), len(GOALS_LIST), len(VIBES_LIST)

def multi_hot(values, category_list):
    vec = np.zeros(len(category_list))
    for v in values:
        if v in category_list:
            vec[category_list.index(v)] = 1
    return vec

def person_vector(user_id, name, main_pos, sub_pos, goals_, vibes_, main_w=0.7, sub_w=0.3):
    pos_vec = np.zeros(POS_DIM)
    if main_pos in positions:
        pos_vec[positions.index(main_pos)] += main_w
    if sub_pos in positions:
        pos_vec[positions.index(sub_pos)] += sub_w
    goal_vec = multi_hot(goals_, GOALS_LIST)
    vibe_vec = multi_hot(vibes_, VIBES_LIST)
    return np.concatenate([pos_vec, goal_vec, vibe_vec])

def team_vector(team_id, team_name, recruit_positions, team_goals, team_vibes):
    pos_vec = multi_hot(recruit_positions, positions)
    goal_vec = multi_hot(team_goals, GOALS_LIST)
    vibe_vec = multi_hot(team_vibes, VIBES_LIST)
    return np.concatenate([pos_vec, goal_vec, vibe_vec])

def combined_team_vector(team_vec, member_vecs, alpha=0.5):
    member_mean = np.mean(member_vecs, axis=0) if len(member_vecs) > 0 else np.zeros_like(team_vec)
    return alpha * team_vec + (1 - alpha) * member_mean

def _idf_from_docs(goal_docs, vibe_docs):
    # docs: 리스트의 리스트 (문서마다 goals_/vibes_ 리스트)
    N = max(1, len(goal_docs))
    goal_df = np.zeros(GOAL_DIM)
    vibe_df = np.zeros(VIBE_DIM)
    for g in goal_docs:
        seen = set(g)
        for v in seen:
            if v in GOALS_LIST:
                goal_df[GOALS_LIST.index(v)] += 1
    for vlist in vibe_docs:
        seen = set(vlist)
        for v in seen:
            if v in VIBES_LIST:
                vibe_df[VIBES_LIST.index(v)] += 1
    goal_idf = np.log((N + 1) / (goal_df + 1)) + 1
    vibe_idf = np.log((N + 1) / (vibe_df + 1)) + 1
    return goal_idf, vibe_idf

def _build_weight_vector(w_pos=0.5, w_goal=0.3, w_vibe=0.2, goal_idf=None, vibe_idf=None):
    goal_w = (goal_idf if goal_idf is not None else np.ones(GOAL_DIM)) * w_goal
    vibe_w = (vibe_idf if vibe_idf is not None else np.ones(VIBE_DIM)) * w_vibe
    return np.concatenate([np.full(POS_DIM, w_pos), goal_w, vibe_w])

def _mmr(sim_scores, cand_mat, k, lambda_=0.7):
    # sim_scores: (n,) 1차 스코어, cand_mat: (n, d) 후보 벡터(가중치 반영된)
    n = len(sim_scores)
    if k >= n:
        return list(range(n))
    selected = []
    remaining = set(range(n))
    # 미리 후보들 간 유사도 계산
    pair_sim = cosine_similarity(cand_mat)  # (n,n)
    for _ in range(k):
        best_i, best_score = None, -1e9
        for i in remaining:
            diversity = 0 if not selected else np.max(pair_sim[i, selected])
            score = lambda_ * sim_scores[i] - (1 - lambda_) * diversity
            if score > best_score:
                best_i, best_score = i, score
        selected.append(best_i)
        remaining.remove(best_i)
    return selected

def _explain_overlap(team_info, candidate):
    reasons = []
    # 포지션
    if candidate['main_pos'] in team_info['recruit_positions']:
        reasons.append(f"포지션 일치(main:{candidate['main_pos']})")
    elif candidate['sub_pos'] in team_info['recruit_positions']:
        reasons.append(f"포지션 부분일치(sub:{candidate['sub_pos']})")
    # 태그
    g_overlap = sorted(set(candidate['goals_']).intersection(set(team_info['goals'])))
    v_overlap = sorted(set(candidate['vibes_']).intersection(set(team_info['vibes'])))
    if g_overlap: reasons.append(f"목표 겹침: {', '.join(g_overlap)}")
    if v_overlap: reasons.append(f"분위기 겹침: {', '.join(v_overlap)}")
    return reasons[:3]

def recommend_candidates(team_info, member_infos, candidate_pool,
                         alpha=0.5, top_k=5,
                         group_weights=(0.5, 0.3, 0.2),  # (pos, goal, vibe)
                         main_sub=(0.75, 0.25),
                         use_mmr=True, mmr_lambda=0.7,
                         pos_need_boost=(0.06, 0.03)):  # (main, sub)
    # 벡터 구성
    team_vec = team_vector(team_info['team_id'], team_info['team_name'],
                           team_info['recruit_positions'], team_info['goals'], team_info['vibes'])
    member_vecs = [person_vector(m['user_id'], m['name'], m['main_pos'], m['sub_pos'], m['goals_'], m['vibes_'],
                                 main_w=main_sub[0], sub_w=main_sub[1]) for m in member_infos]
    combined_vec = combined_team_vector(team_vec, member_vecs, alpha)

    # TF-IDF 가중치(희소성 보정)
    goal_docs = [c['goals_'] for c in candidate_pool] + [m['goals_'] for m in member_infos] + [team_info['goals']]
    vibe_docs = [c['vibes_'] for c in candidate_pool] + [m['vibes_'] for m in member_infos] + [team_info['vibes']]
    goal_idf, vibe_idf = _idf_from_docs(goal_docs, vibe_docs)
    w_vec = _build_weight_vector(*group_weights, goal_idf=goal_idf, vibe_idf=vibe_idf)

    # 유사도 계산 + 수요 부스트
    sims, cand_mats = [], []
    for cand in candidate_pool:
        cand_vec = person_vector(cand['user_id'], cand['name'], cand['main_pos'], cand['sub_pos'],
                                 cand['goals_'], cand['vibes_'],
                                 main_w=main_sub[0], sub_w=main_sub[1])
        w_combined = combined_vec * w_vec
        w_cand = cand_vec * w_vec
        base_sim = cosine_similarity([w_combined], [w_cand])[0][0]
        # 포지션 수요 부스트
        boost = 0.0
        if cand['main_pos'] in team_info['recruit_positions']:
            boost += pos_need_boost[0]
        elif cand['sub_pos'] in team_info['recruit_positions']:
            boost += pos_need_boost[1]
        sims.append(base_sim + boost)
        cand_mats.append(w_cand)

    sims = np.array(sims)
    cand_mats = np.vstack(cand_mats)

    # 1차 정렬
    order = np.argsort(-sims)
    # 다양성 재정렬(MMR)
    if use_mmr and len(order) > 1 and top_k > 1:
        order = order[_mmr(sims[order], cand_mats[order], k=min(top_k, len(order)), lambda_=mmr_lambda)]
    else:
        order = order[:top_k]

    recommended = []
    for i in order[:top_k]:
        cand = candidate_pool[i].copy()
        cand["similarity"] = round(float(sims[i]), 4)
        cand["reasons"] = _explain_overlap(team_info, cand)
        recommended.append(cand)
    return recommended

def recommend_teams(person_info, team_pool, team_members_map,
                    alpha=0.5, top_k=5,
                    group_weights=(0.5, 0.3, 0.2),
                    main_sub=(0.75, 0.25),
                    use_mmr=True, mmr_lambda=0.7):
    person_vec_ = person_vector(person_info['user_id'], person_info['name'],
                                person_info['main_pos'], person_info['sub_pos'],
                                person_info['goals_'], person_info['vibes_'],
                                main_w=main_sub[0], sub_w=main_sub[1])

    # TF-IDF 가중치: 팀 목표/분위기 분포 기준
    goal_docs = [t['goals'] for t in team_pool]
    vibe_docs = [t['vibes'] for t in team_pool]
    goal_idf, vibe_idf = _idf_from_docs(goal_docs, vibe_docs)
    w_vec = _build_weight_vector(*group_weights, goal_idf=goal_idf, vibe_idf=vibe_idf)

    sims, team_mats = [], []
    for team, members in zip(team_pool, team_members_map):
        t_vec = team_vector(team['team_id'], team['team_name'], team['recruit_positions'],
                            team['goals'], team['vibes'])
        m_vecs = [person_vector(m['user_id'], m['name'], m['main_pos'], m['sub_pos'], m['goals_'], m['vibes_'],
                                main_w=main_sub[0], sub_w=main_sub[1]) for m in members]
        combined_vec = combined_team_vector(t_vec, m_vecs, alpha)
        w_person = person_vec_ * w_vec
        w_team = combined_vec * w_vec
        sims.append(cosine_similarity([w_person], [w_team])[0][0])
        team_mats.append(w_team)

    sims = np.array(sims)
    team_mats = np.vstack(team_mats)
    order = np.argsort(-sims)
    if use_mmr and len(order) > 1 and top_k > 1:
        order = order[_mmr(sims[order], team_mats[order], k=min(top_k, len(order)), lambda_=mmr_lambda)]
    else:
        order = order[:top_k]

    recommended = []
    for i in order[:top_k]:
        t = team_pool[i].copy()
        t["similarity"] = round(float(sims[i]), 4)
        recommended.append(t)
    return recommended