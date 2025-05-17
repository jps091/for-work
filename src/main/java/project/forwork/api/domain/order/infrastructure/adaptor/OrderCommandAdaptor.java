package project.forwork.api.domain.order.infrastructure.adaptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.OrderErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.order.infrastructure.OrderEntity;
import project.forwork.api.domain.order.infrastructure.OrderJpaRepository;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.model.Orders;
import project.forwork.api.domain.order.service.port.OrderCommandPort;
import project.forwork.api.domain.order.service.port.OrderRepository;
import project.forwork.api.domain.orderresume.infrastructure.OrderResumeEntity;
import project.forwork.api.domain.orderresume.infrastructure.OrderResumeJpaRepository;
import project.forwork.api.domain.orderresume.model.OrderResume;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OrderCommandAdaptor implements OrderCommandPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderResumeJpaRepository orderResumeJpaRepository;

    @Override
    public Order save(Order order, List<OrderResume> orderResumes) {
        Order savedOrder = orderJpaRepository.save(OrderEntity.from(order)).toModel();
        List<OrderResumeEntity> orderEntities = orderResumes.stream().map(it -> OrderResumeEntity.from2(savedOrder, it)).toList();
        orderResumeJpaRepository.saveAll(orderEntities);
        return savedOrder;
    }

    @Override
    public Orders update(Orders orders) {
        List<OrderEntity> orderEntities = orders.convertToEntity();
        List<Order> savedOrders = orderJpaRepository.saveAll(orderEntities).stream()
                .map(OrderEntity::toModel)
                .toList();
        return Orders.of(savedOrders);
    }

    @Override
    public Order update(Order order) {
        return orderJpaRepository.save(OrderEntity.from(order)).toModel();
    }
}
