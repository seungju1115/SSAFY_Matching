package com.example.demo.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 305253613L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final ListPath<com.example.demo.chat.entity.ChatRoomMember, com.example.demo.chat.entity.QChatRoomMember> chatRoomMembers = this.<com.example.demo.chat.entity.ChatRoomMember, com.example.demo.chat.entity.QChatRoomMember>createList("chatRoomMembers", com.example.demo.chat.entity.ChatRoomMember.class, com.example.demo.chat.entity.QChatRoomMember.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> lastClass = createNumber("lastClass", Integer.class);

    public final BooleanPath major = createBoolean("major");

    public final ListPath<com.example.demo.team.entity.TeamMembershipRequest, com.example.demo.team.entity.QTeamMembershipRequest> membershipRequests = this.<com.example.demo.team.entity.TeamMembershipRequest, com.example.demo.team.entity.QTeamMembershipRequest>createList("membershipRequests", com.example.demo.team.entity.TeamMembershipRequest.class, com.example.demo.team.entity.QTeamMembershipRequest.class, PathInits.DIRECT2);

    public final StringPath projectExp = createString("projectExp");

    public final SetPath<com.example.demo.user.Enum.ProjectGoalEnum, EnumPath<com.example.demo.user.Enum.ProjectGoalEnum>> projectGoal = this.<com.example.demo.user.Enum.ProjectGoalEnum, EnumPath<com.example.demo.user.Enum.ProjectGoalEnum>>createSet("projectGoal", com.example.demo.user.Enum.ProjectGoalEnum.class, EnumPath.class, PathInits.DIRECT2);

    public final SetPath<com.example.demo.user.Enum.ProjectViveEnum, EnumPath<com.example.demo.user.Enum.ProjectViveEnum>> projectVive = this.<com.example.demo.user.Enum.ProjectViveEnum, EnumPath<com.example.demo.user.Enum.ProjectViveEnum>>createSet("projectVive", com.example.demo.user.Enum.ProjectViveEnum.class, EnumPath.class, PathInits.DIRECT2);

    public final StringPath qualification = createString("qualification");

    public final StringPath role = createString("role");

    public final com.example.demo.team.entity.QTeam team;

    public final SetPath<com.example.demo.user.Enum.TechEnum, EnumPath<com.example.demo.user.Enum.TechEnum>> techStack = this.<com.example.demo.user.Enum.TechEnum, EnumPath<com.example.demo.user.Enum.TechEnum>>createSet("techStack", com.example.demo.user.Enum.TechEnum.class, EnumPath.class, PathInits.DIRECT2);

    public final StringPath userName = createString("userName");

    public final StringPath userProfile = createString("userProfile");

    public final ListPath<com.example.demo.user.Enum.PositionEnum, EnumPath<com.example.demo.user.Enum.PositionEnum>> wantedPosition = this.<com.example.demo.user.Enum.PositionEnum, EnumPath<com.example.demo.user.Enum.PositionEnum>>createList("wantedPosition", com.example.demo.user.Enum.PositionEnum.class, EnumPath.class, PathInits.DIRECT2);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.team = inits.isInitialized("team") ? new com.example.demo.team.entity.QTeam(forProperty("team"), inits.get("team")) : null;
    }

}

