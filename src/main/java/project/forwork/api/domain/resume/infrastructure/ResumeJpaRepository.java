package project.forwork.api.domain.resume.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeJpaRepository extends JpaRepository<ResumeEntity, Long> {

}
