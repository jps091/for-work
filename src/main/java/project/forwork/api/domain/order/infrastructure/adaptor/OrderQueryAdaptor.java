package project.forwork.api.domain.order.infrastructure.adaptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.OrderErrorCode;
import project.forwork.api.common.error.OrderResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.order.infrastructure.OrderEntity;
import project.forwork.api.domain.order.infrastructure.OrderJpaRepository;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.model.Orders;
import project.forwork.api.domain.order.service.port.OrderQueryPort;
import project.forwork.api.domain.order.service.port.OrderRepository;
import project.forwork.api.domain.orderresume.infrastructure.OrderResumeEntity;
import project.forwork.api.domain.orderresume.infrastructure.OrderResumeJpaRepository;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OrderQueryAdaptor implements OrderQueryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderResumeJpaRepository orderResumeJpaRepository;

    @Override
    public Order getByIdWithThrow(Long orderId) {
        return orderJpaRepository.findById(orderId)
                .map(OrderEntity::toModel)
                .orElseThrow(() -> new ApiException(OrderErrorCode.ORDER_NOT_FOUND, orderId));
    }

    @Override
    public Optional<Order> findByRequestId(String requestId) {
        return orderJpaRepository.findByRequestId(requestId).map(OrderEntity::toModel);

    }

    @Override
    public Orders findByUserId(Long userId) {
        List<Order> orders = orderJpaRepository.findByUserEntity_IdOrderByIdDesc(userId).stream().map(OrderEntity::toModel).toList();
        return Orders.of(orders);
    }

    @Override
    public Orders findByStatus(OrderStatus status, int limit) {
        List<Order> orders = orderJpaRepository.findByStatus(status, Limit.of(limit))
                .stream().map(OrderEntity::toModel).toList();
        return Orders.of(orders);
    }

    @Override
    public List<OrderResume> findByOrderId(Long orderId) {
        return orderResumeJpaRepository.findByOrderEntity_Id(orderId).stream()
                .map(OrderResumeEntity::toModel)
                .toList();
    }

    @Override
    public OrderResume getByIdWithThrow(long orderResumeId) {
        return orderResumeJpaRepository
                .findById(orderResumeId)
                .map(OrderResumeEntity::toModel)
                .orElseThrow(() -> new ApiException(OrderResumeErrorCode.NOT_FOUND, orderResumeId));
    }

    @Override
    public List<OrderResume> findByOrderIdAndStatus(List<Long> orderResumeIds, Long orderId, OrderResumeStatus status) {
        return orderResumeJpaRepository.findByOrderIdAndStatus(orderResumeIds, orderId, status).stream()
                .map(OrderResumeEntity::toModel)
                .toList();
    }

    @Override
    public List<OrderResume> findByStatusAndOrder(OrderResumeStatus status, Order order) {
        return orderResumeJpaRepository.findByStatusAndOrder(status, OrderEntity.from(order)).stream()
                .map(OrderResumeEntity::toModel)
                .toList();
    }
    @Override
    public List<OrderResume> findByStatusAndOrders(OrderResumeStatus status, Orders orders) {
        List<OrderEntity> orderEntities = orders.convertToEntity();
        return orderResumeJpaRepository.findByStatusAndOrder(status, orderEntities).stream()
                .map(OrderResumeEntity::toModel)
                .toList();
    }
}
