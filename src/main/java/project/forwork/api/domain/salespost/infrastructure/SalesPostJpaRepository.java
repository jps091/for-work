package project.forwork.api.domain.salespost.infrastructure;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;

import java.util.Optional;

public interface SalesPostJpaRepository extends JpaRepository<SalesPostEntity, Long> {

    Optional<SalesPostEntity> findByResumeEntity_Id(Long resumeId);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SalesPostEntity s where s.id = :id")
    Optional<SalesPostEntity> findByIdWithPessimisticLock(@Param("id") Long id);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select s from SalesPostEntity s where s.id = :id")
    Optional<SalesPostEntity> findByIdWithOptimisticLock(@Param("id") Long id);

    void deleteByResumeEntity_Id(Long resumeId);
}
