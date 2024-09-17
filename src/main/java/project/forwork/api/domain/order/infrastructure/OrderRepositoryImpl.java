package project.forwork.api.domain.order.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.OrderErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.port.OrderRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(OrderEntity.from(order)).toModel();
    }

    @Override
    public void saveAll(List<Order> orders) {
        List<OrderEntity> orderEntities = orders.stream().map(OrderEntity::from).toList();
        orderJpaRepository.saveAll(orderEntities);
    }

    @Override
    public Order getByIdWithThrow(Long orderId) {
        return findById(orderId).orElseThrow(() -> new ApiException(OrderErrorCode.ORDER_NOT_FOUND, orderId));
    }

    @Override
    public Order getOrderWithThrow(Long userId, Long orderId) {
        return orderJpaRepository.getByUserEntity_IdAndId(userId, orderId)
                .orElseThrow(() -> new ApiException(OrderErrorCode.ORDER_NOT_FOUND, orderId))
                .toModel();
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderJpaRepository.findById(orderId).map(OrderEntity::toModel);
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return orderJpaRepository.findAllByStatus(status).stream()
                .map(OrderEntity::toModel)
                .toList();
    }
}
