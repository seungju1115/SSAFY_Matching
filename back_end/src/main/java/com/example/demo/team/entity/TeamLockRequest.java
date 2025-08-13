package com.example.demo.team.entity;

import com.example.demo.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "team_lock_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamLockRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 대상 팀
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    // 요청 상태 (예: PENDING, APPROVED, REJECTED 등) - 필요하다면 enum 따로 만드세요
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    @Builder.Default
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

    public void setTeam(Team team) {
        if (this.team != null) {
            this.team.getLockRequests().remove(this);
        }
        this.team = team;
        if (team != null && !team.getLockRequests().contains(this)) {
            team.getLockRequests().add(this);
        }
    }
}