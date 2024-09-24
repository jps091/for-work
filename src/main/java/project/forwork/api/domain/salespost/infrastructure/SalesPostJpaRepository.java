package project.forwork.api.domain.salespost.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;

import java.util.Optional;

public interface SalesPostJpaRepository extends JpaRepository<SalesPostEntity, Long> {

    Optional<SalesPostEntity> findByResumeEntity(ResumeEntity resumeEntity);


    @Query("select s from SalesPostEntity s" +
            " join s.resumeEntity r" +
            " where r.resumeStatus = :resumeStatus" +
            " and s.id = :salesPostId" +
            " and s.salesStatus = :salesStatus")
    Optional<SalesPostEntity> findSellingPost(
            @Param("salesPostId") Long salesPostId,
            @Param("resumeStatus") ResumeStatus resumeStatus,
            @Param("salesStatus") SalesStatus salesStatus);

}
