package project.forwork.api.domain.salepost.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.salepost.infrastructure.enums.SalesStatus;

import java.util.Optional;

public interface SalePostJpaRepository extends JpaRepository<SalePostEntity, Long> {

    Optional<SalePostEntity> findByResumeEntity(ResumeEntity resumeEntity);


    @Query("select s from SalePostEntity s" +
            " join s.resumeEntity r" +
            " where r.resumeStatus = :resumeStatus" +
            " and s.id = :salePostId" +
            " and s.salesStatus = :salesStatus")
    Optional<SalePostEntity> findSellingPost(
            @Param("salePostId") Long salePostId,
            @Param("resumeStatus") ResumeStatus resumeStatus,
            @Param("salesStatus") SalesStatus salesStatus);

}
