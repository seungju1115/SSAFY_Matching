package com.example.demo.team.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeamMembershipRequest is a Querydsl query type for TeamMembershipRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamMembershipRequest extends EntityPathBase<TeamMembershipRequest> {

    private static final long serialVersionUID = 774806216L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeamMembershipRequest teamMembershipRequest = new QTeamMembershipRequest("teamMembershipRequest");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath message = createString("message");

    public final EnumPath<RequestType> requestType = createEnum("requestType", RequestType.class);

    public final EnumPath<RequestStatus> status = createEnum("status", RequestStatus.class);

    public final QTeam team;

    public final com.example.demo.user.entity.QUser user;

    public QTeamMembershipRequest(String variable) {
        this(TeamMembershipRequest.class, forVariable(variable), INITS);
    }

    public QTeamMembershipRequest(Path<? extends TeamMembershipRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTeamMembershipRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTeamMembershipRequest(PathMetadata metadata, PathInits inits) {
        this(TeamMembershipRequest.class, metadata, inits);
    }

    public QTeamMembershipRequest(Class<? extends TeamMembershipRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.team = inits.isInitialized("team") ? new QTeam(forProperty("team"), inits.get("team")) : null;
        this.user = inits.isInitialized("user") ? new com.example.demo.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

