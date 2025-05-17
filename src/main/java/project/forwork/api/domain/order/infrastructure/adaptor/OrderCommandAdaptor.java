package project.forwork.api.domain.order.infrastructure.adaptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.forwork.api.domain.order.infrastructure.OrderEntity;
import project.forwork.api.domain.order.infrastructure.OrderJpaRepository;
import project.forwork.api.domain.order.infrastructure.model.ResumeDto;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.model.Orders;
import project.forwork.api.domain.order.service.port.OrderCommandPort;
import project.forwork.api.domain.orderresume.infrastructure.OrderResumeEntity;
import project.forwork.api.domain.orderresume.infrastructure.OrderResumeJpaRepository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OrderCommandAdaptor implements OrderCommandPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderResumeJpaRepository orderResumeJpaRepository;

    @Override
    public Order save(Order order, List<ResumeDto> resumeDtos) {
        Order savedOrder = orderJpaRepository.save(OrderEntity.from(order)).toModel();
        savedOrder.addOrderResumes(resumeDtos);

        List<OrderResumeEntity> orderEntities = savedOrder.getOrderResumes()
                .stream()
                .map(it -> OrderResumeEntity.from(it, savedOrder))
                .toList();
        orderResumeJpaRepository.saveAll(orderEntities);
        return savedOrder;
    }

    @Override
    public Order update(Order order) {
        Order updatedOrder = orderJpaRepository.save(OrderEntity.from(order)).toModel();

        List<OrderResumeEntity> orderEntities = updatedOrder.getOrderResumes()
                .stream()
                .map(it -> OrderResumeEntity.from(it, updatedOrder))
                .toList();
        orderResumeJpaRepository.saveAll(orderEntities);
        return updatedOrder;
    }

    @Override
    public Orders updateAll(Orders orders) {
        List<OrderEntity> orderEntities = orders.convertToEntity();
        List<Order> savedOrders = orderJpaRepository.saveAll(orderEntities).stream()
                .map(OrderEntity::toModel)
                .toList();
        return Orders.of(savedOrders);
    }
}
