package project.forwork.api.domain.resumedecision.infrastructure;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.resumedecision.infrastructure.enums.DecisionStatus;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.user.infrastructure.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "resume_decisions")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeDecisionEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_decision_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private UserEntity adminEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private ResumeEntity resumeEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DecisionStatus decisionStatus;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;

    public static ResumeDecisionEntity from(ResumeDecision resumeDecision){
        ResumeDecisionEntity resumeDecisionEntity = new ResumeDecisionEntity();
        resumeDecisionEntity.id = resumeDecision.getId();
        resumeDecisionEntity.adminEntity = UserEntity.from(resumeDecision.getAdmin());
        resumeDecisionEntity.resumeEntity = ResumeEntity.from(resumeDecision.getResume());
        resumeDecisionEntity.decisionStatus = resumeDecision.getDecisionStatus();
        resumeDecisionEntity.decidedAt = resumeDecision.getDecidedAt();
        return resumeDecisionEntity;
    }

    public ResumeDecision toModel(){
        return ResumeDecision.builder()
                .id(id)
                .admin(adminEntity.toModel())
                .resume(resumeEntity.toModel())
                .decisionStatus(decisionStatus)
                .decidedAt(decidedAt)
                .build();
    }
}
