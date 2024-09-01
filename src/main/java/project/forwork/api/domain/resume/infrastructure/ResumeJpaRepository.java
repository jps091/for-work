package project.forwork.api.domain.resume.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import project.forwork.api.domain.user.infrastructure.UserEntity;

import java.util.List;

public interface ResumeJpaRepository extends JpaRepository<ResumeEntity, Long> {
    List<ResumeEntity> findAllBySellerEntity(UserEntity seller);
}
