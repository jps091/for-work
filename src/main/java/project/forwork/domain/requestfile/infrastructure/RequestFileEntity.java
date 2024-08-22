package project.forwork.domain.requestfile.infrastructure;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.common.infrastructure.BaseTimeEntity;
import project.forwork.domain.resume.infrastructure.ResumeEntity;
import project.forwork.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.domain.thumbnailimage.infrastructure.ThumbnailImageEntity;
import project.forwork.domain.user.infrastructure.UserEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "request_files")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestFileEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_file_id")
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
