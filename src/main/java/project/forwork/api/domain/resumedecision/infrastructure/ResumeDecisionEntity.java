package project.forwork.api.domain.resumedecision.infrastructure;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.resumedecision.infrastructure.enums.DecisionStatus;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.resumedecision.model.ResumeDecision;
import project.forwork.api.domain.user.infrastructure.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "resume_decisions")
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeDecisionEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_decision_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id") @NotNull
    private UserEntity adminEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id") @NotNull
    private ResumeEntity resumeEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status") @NotNull
    private DecisionStatus decisionStatus;

    @NotNull
    @Column(name = "decided_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime registeredAt;

    public static ResumeDecisionEntity from(ResumeDecision resumeDecision){
        ResumeDecisionEntity resumeDecisionEntity = new ResumeDecisionEntity();
        resumeDecisionEntity.id = resumeDecision.getId();
        resumeDecisionEntity.adminEntity = UserEntity.from(resumeDecision.getAdmin());
        resumeDecisionEntity.resumeEntity = ResumeEntity.from(resumeDecision.getResume());
        resumeDecisionEntity.decisionStatus = resumeDecision.getDecisionStatus();
        resumeDecisionEntity.registeredAt = resumeDecision.getRegisteredAt();
        return resumeDecisionEntity;
    }

    public ResumeDecision toModel(){
        return ResumeDecision.builder()
                .id(id)
                .admin(adminEntity.toModel())
                .resume(resumeEntity.toModel())
                .decisionStatus(decisionStatus)
                .registeredAt(registeredAt)
                .build();
    }
}
