package project.forwork.api.domain.order.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> getByUserEntity_IdAndId(Long userId, Long orderId);
    List<OrderEntity> findAllByStatus(OrderStatus status);
    List<OrderEntity> findByUserEntity_IdAndStatusInOrderByIdDesc(Long userId, List<OrderStatus> statuses);
}
