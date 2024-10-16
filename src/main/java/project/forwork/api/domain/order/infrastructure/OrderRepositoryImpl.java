package project.forwork.api.domain.order.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.OrderErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.port.OrderRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(OrderEntity.from(order)).toModel();
    }

    @Override
    public List<Order> saveAll(List<Order> orders) {
        List<OrderEntity> orderEntities = orders.stream().map(OrderEntity::from).toList();
        return orderJpaRepository.saveAll(orderEntities).stream()
                .map(OrderEntity::toModel)
                .toList();
    }

    @Override
    public Order getByIdWithThrow(Long orderId) {
        return findById(orderId).orElseThrow(() -> new ApiException(OrderErrorCode.ORDER_NOT_FOUND, orderId));
    }

    @Override
    public boolean existsByRequestId(String requestId) {
        return orderJpaRepository.existsByRequestId(requestId);
    }

    @Override
    public Order getByRequestIdWithThrow(String requestId) {
        return orderJpaRepository.getByRequestId(requestId)
                .orElseThrow(() -> new ApiException(OrderErrorCode.ORDER_NOT_FOUND))
                .toModel();
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

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderJpaRepository.findByUserEntity_Id(userId).stream().map(OrderEntity::toModel).toList();
    }

    @Override
    public List<Order> findByStatus(OrderStatus status, int limit) {
        return orderJpaRepository.findByStatus(status, Limit.of(limit)).stream().map(OrderEntity::toModel).toList();
    }
}
