package project.forwork.api.domain.resumedecision.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;

import java.util.Optional;

public interface ResumeDecisionJpaRepository extends JpaRepository<ResumeDecisionEntity, Long> {

    @Query("select rd from ResumeDecisionEntity rd" +
            " join fetch rd.resumeEntity r" +
            " where r.resumeStatus = :status" +
            " order by r.registeredAt desc")
    Page<ResumeDecisionEntity> findAllByResumeStatus(PageRequest pageRequest, @Param("status") ResumeStatus resumeStatus);

    Optional<ResumeDecisionEntity> findByResumeEntity(ResumeEntity resumeEntity);
}
