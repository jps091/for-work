package project.forwork.api.domain.order.infrastructure;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByRequestId(String requestId);
    Optional<OrderEntity> findByUserEntity_IdAndId(Long userId, Long orderId);
    List<OrderEntity> findByUserEntity_IdOrderByIdDesc(Long userId);
    List<OrderEntity> findByStatus(OrderStatus status, Limit limit);
}
