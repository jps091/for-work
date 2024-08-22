package project.forwork.domain.requestresult.infrastructure;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.common.infrastructure.BaseTimeEntity;
import project.forwork.domain.resume.infrastructure.ResumeEntity;
import project.forwork.domain.user.infrastructure.UserEntity;

@Entity
@Table(name = "request_results")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestResultEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private UserEntity adminEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private ResumeEntity resumeEntity;

    @Column(nullable = false)
    private String title;
}
