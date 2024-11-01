package project.forwork.api.domain.cart.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findByUserEntity_Id(Long userId);
    void deleteByUserEntity_Id(Long userId);
}
