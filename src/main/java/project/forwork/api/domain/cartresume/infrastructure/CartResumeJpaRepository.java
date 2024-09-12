package project.forwork.api.domain.cartresume.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.forwork.api.domain.cart.infrastructure.CartEntity;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;

import java.util.List;

public interface CartResumeJpaRepository extends JpaRepository<CartResumeEntity, Long> {

    boolean findByCartEntityAndResumeEntity(CartEntity cartEntity, ResumeEntity resumeEntity);

    @Query("select cr from CartResumeEntity  cr" +
            " join fetch cr.cartEntity c" +
            " join fetch c.userEntity u" +
            " where u.id = :userId")
    List<CartResumeEntity> findAllInCart(@Param("userId") Long userId);

    @Query("select cr from CartResumeEntity cr" +
            " join fetch cr.resumeEntity" +
            " where cr.id IN :cartResumeIds")
    List<CartResumeEntity> findBySelected(@Param("cartResumeIds") List<Long> cartResumeIds);


    @Query("select cr from CartResumeEntity cr" +
            " join fetch cr.cartEntity c" +
            " join fetch c.userEntity u" +
            " where u.id = :userId" +
            " and cr.id IN :cartResumeIds")
    List<CartResumeEntity> findByUserAndSelected(@Param("userId") Long userId, @Param("cartResumeIds") List<Long> cartResumeIds);
 }
