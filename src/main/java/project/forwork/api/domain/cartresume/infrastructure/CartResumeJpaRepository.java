package project.forwork.api.domain.cartresume.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.forwork.api.domain.cart.infrastructure.CartEntity;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;

import java.util.List;

public interface CartResumeJpaRepository extends JpaRepository<CartResumeEntity, Long> {
    boolean existsByCartEntityAndResumeEntity(CartEntity cartEntity, ResumeEntity resumeEntity);

    @Query("select cr from CartResumeEntity  cr" +
            " join fetch cr.cartEntity c" +
            " join fetch c.userEntity u" +
            " where u.id = :userId")
    List<CartResumeEntity> findAllInCart(@Param("userId") Long userId);

    @Modifying
    @Query("delete from CartResumeEntity cr" +
            " where cr.cartEntity.userEntity.id = :userId")
    void deleteAllInCart(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM CartResumeEntity c WHERE c.cartEntity.id = :cartId and c.id IN :ids")
    void deleteByIds(@Param("cartId") Long cartId, @Param("ids") List<Long> ids);

    @Query("select cr from CartResumeEntity cr" +
            " join fetch cr.resumeEntity" +
            " where cr.id IN (:cartResumeIds)")
    List<CartResumeEntity> findBySelected(@Param("cartResumeIds") List<Long> cartResumeIds);

    @Query("select cr from CartResumeEntity cr" +
            " where cr.id IN :cartResumeIds")
    List<CartResumeEntity> findByConfirmedResumes(@Param("cartResumeIds") List<Long> cartResumeIds);

    @Query("select cr from CartResumeEntity cr" +
            " join fetch cr.cartEntity c" +
            " join fetch c.userEntity u" +
            " where u.id = :userId" +
            " and cr.id IN (:cartResumeIds)")
    List<CartResumeEntity> findByUserAndSelected(@Param("userId") Long userId, @Param("cartResumeIds") List<Long> cartResumeIds);

    void deleteAllByResumeEntity_Id(Long resumeId);
 }
