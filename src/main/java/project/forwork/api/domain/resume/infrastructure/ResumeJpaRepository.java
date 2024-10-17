package project.forwork.api.domain.resume.infrastructure;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.forwork.api.domain.user.infrastructure.UserEntity;

import java.util.List;
import java.util.Optional;

public interface ResumeJpaRepository extends JpaRepository<ResumeEntity, Long>{
    List<ResumeEntity> findAllBySellerEntity(UserEntity seller);

    @Query("select r from ResumeEntity r where r.id IN (:resumeIds)")
    List<ResumeEntity> findByIds(@Param("resumeIds") List<Long> resumeIds);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from ResumeEntity r where r.id IN (:resumeIds)")
    List<ResumeEntity> findByIdsWithPessimisticLock(@Param("resumeIds") List<Long> resumeIds);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from ResumeEntity r where r.id = :resumeId")
    Optional<ResumeEntity> findByIdWithPessimisticLock(@Param("resumeId") Long resumeId);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select r from ResumeEntity r where r.id = :resumeId")
    Optional<ResumeEntity> findByIdWithOptimisticLock(@Param("resumeId") Long resumeId);

    @Modifying
    @Query("update ResumeEntity r set r.salesQuantity = r.salesQuantity + 1 where r.id = :resumeId")
    void increaseQuantity(@Param("resumeId") Long resumeId);
}
