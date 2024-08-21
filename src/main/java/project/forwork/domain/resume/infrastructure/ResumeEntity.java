package project.forwork.domain.resume.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.common.infrastructure.BaseTimeEntity;

@Entity
@Table(name = "resumes")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "resume_id")
    private Long id;

}
