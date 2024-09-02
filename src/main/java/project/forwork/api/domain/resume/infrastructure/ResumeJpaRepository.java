package project.forwork.api.domain.resume.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.user.infrastructure.UserEntity;

import java.util.List;

public interface ResumeJpaRepository extends JpaRepository<ResumeEntity, Long> {
    List<ResumeEntity> findByResumeStatus(ResumeStatus status);
    List<ResumeEntity> findAllBySellerEntity(UserEntity seller);
}
