package com.example.demo.team.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeam is a Querydsl query type for Team
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeam extends EntityPathBase<Team> {

    private static final long serialVersionUID = 379137809L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeam team = new QTeam("team");

    public final NumberPath<Integer> aiCount = createNumber("aiCount", Integer.class);

    public final NumberPath<Integer> backendCount = createNumber("backendCount", Integer.class);

    public final com.example.demo.chat.entity.QChatRoom chatRoom;

    public final NumberPath<Integer> designCount = createNumber("designCount", Integer.class);

    public final NumberPath<Integer> frontendCount = createNumber("frontendCount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.demo.user.entity.QUser leader;

    public final ListPath<com.example.demo.user.entity.User, com.example.demo.user.entity.QUser> members = this.<com.example.demo.user.entity.User, com.example.demo.user.entity.QUser>createList("members", com.example.demo.user.entity.User.class, com.example.demo.user.entity.QUser.class, PathInits.DIRECT2);

    public final ListPath<TeamMembershipRequest, QTeamMembershipRequest> membershipRequests = this.<TeamMembershipRequest, QTeamMembershipRequest>createList("membershipRequests", TeamMembershipRequest.class, QTeamMembershipRequest.class, PathInits.DIRECT2);

    public final StringPath memberWanted = createString("memberWanted");

    public final NumberPath<Integer> pmCount = createNumber("pmCount", Integer.class);

    public final EnumPath<TeamStatus> status = createEnum("status", TeamStatus.class);

    public final StringPath teamDescription = createString("teamDescription");

    public final StringPath teamDomain = createString("teamDomain");

    public final StringPath teamName = createString("teamName");

    public final SetPath<com.example.demo.user.Enum.ProjectGoalEnum, EnumPath<com.example.demo.user.Enum.ProjectGoalEnum>> teamPreference = this.<com.example.demo.user.Enum.ProjectGoalEnum, EnumPath<com.example.demo.user.Enum.ProjectGoalEnum>>createSet("teamPreference", com.example.demo.user.Enum.ProjectGoalEnum.class, EnumPath.class, PathInits.DIRECT2);

    public final SetPath<com.example.demo.user.Enum.ProjectViveEnum, EnumPath<com.example.demo.user.Enum.ProjectViveEnum>> teamVive = this.<com.example.demo.user.Enum.ProjectViveEnum, EnumPath<com.example.demo.user.Enum.ProjectViveEnum>>createSet("teamVive", com.example.demo.user.Enum.ProjectViveEnum.class, EnumPath.class, PathInits.DIRECT2);

    public QTeam(String variable) {
        this(Team.class, forVariable(variable), INITS);
    }

    public QTeam(Path<? extends Team> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTeam(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTeam(PathMetadata metadata, PathInits inits) {
        this(Team.class, metadata, inits);
    }

    public QTeam(Class<? extends Team> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new com.example.demo.chat.entity.QChatRoom(forProperty("chatRoom"), inits.get("chatRoom")) : null;
        this.leader = inits.isInitialized("leader") ? new com.example.demo.user.entity.QUser(forProperty("leader"), inits.get("leader")) : null;
    }

}

