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

    @Column(name = "admin_id") @NotNull
    private Long adminId;

    @Column(name = "resume_id") @NotNull
    private Long resumeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status") @NotNull
    private DecisionStatus decisionStatus;

    public static ResumeDecisionEntity from(ResumeDecision resumeDecision){
        ResumeDecisionEntity resumeDecisionEntity = new ResumeDecisionEntity();
        resumeDecisionEntity.id = resumeDecision.getId();
        resumeDecisionEntity.adminId = resumeDecision.getAdminId();
        resumeDecisionEntity.resumeId = resumeDecision.getResumeId();
        resumeDecisionEntity.decisionStatus = resumeDecision.getDecisionStatus();
        return resumeDecisionEntity;
    }

    public ResumeDecision toModel(){
        return ResumeDecision.builder()
                .id(id)
                .adminId(adminId)
                .resumeId(resumeId)
                .decisionStatus(decisionStatus)
                .registeredAt(getRegisteredAt())
                .build();
    }
}
