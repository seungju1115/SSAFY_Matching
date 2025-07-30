package com.example.demo.team.entity;

import com.example.demo.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "team_membership_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMembershipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 요청 대상 또는 요청자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 대상 팀
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    // 요청 타입: INVITE, JOIN_REQUEST
    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", length = 10, nullable = false)
    private RequestType requestType;

    // 요청 상태: PENDING, ACCEPTED, REJECTED, CANCELED
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    // 요청 메시지 (선택 사항)
    @Lob
    @Column(columnDefinition = "TEXT")
    private String message;

    // 생성일시
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ✅ 양방향 헬퍼 메서드
    public void setUser(User user) {
        if (this.user != null) {
            this.user.getMembershipRequests().remove(this);
        }
        this.user = user;
        if (user != null && !user.getMembershipRequests().contains(this)) {
            user.getMembershipRequests().add(this);
        }
    }

    public void setTeam(Team team) {
        if (this.team != null) {
            this.team.getMembershipRequests().remove(this);
        }
        this.team = team;
        if (team != null && !team.getMembershipRequests().contains(this)) {
            team.getMembershipRequests().add(this);
        }
    }
}

