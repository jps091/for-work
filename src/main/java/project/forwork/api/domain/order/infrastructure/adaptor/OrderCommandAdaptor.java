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
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.model.OrderResumes;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OrderCommandAdaptor implements OrderCommandPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderResumeJpaRepository orderResumeJpaRepository;

    @Override
    public Order save(Order order, List<ResumeDto> resumeDtos) {
        Order savedOrder = order.addOrderResumes(resumeDtos);
        orderJpaRepository.save(OrderEntity.from(savedOrder)).toModel();

        OrderResumes orderResumes = savedOrder.getOrderResumes();
        List<OrderResumeEntity> orderResumeEntityList = orderResumes.convertToEntityList(savedOrder);

        orderResumeJpaRepository.saveAll(orderResumeEntityList);
        return savedOrder;
    }

    @Override
    public Order update(Order order) {
        Order updatedOrder = orderJpaRepository.save(OrderEntity.from(order)).toModel();

        OrderResumes orderResumes = updatedOrder.getOrderResumes();
        List<OrderResumeEntity> orderResumeEntityList = orderResumes.convertToEntityList(updatedOrder);
        orderResumeJpaRepository.saveAll(orderResumeEntityList);
        return updatedOrder;
    }

    @Override
    public Orders updateAll(Orders orders) {
        List<OrderEntity> orderEntities = orders.convertToEntity();
        List<Order> savedOrders = orderJpaRepository.saveAll(orderEntities).stream()
                .map(OrderEntity::toModel)
                .toList();

        List<OrderResumeEntity> orderResumeEntityList = savedOrders.stream()
                .flatMap(order -> order.getOrderResumes()
                        .convertToEntityList(order) // ✅ 도메인 메서드 활용
                        .stream())
                .toList();

        orderResumeJpaRepository.saveAll(orderResumeEntityList);
        return Orders.of(savedOrders);
    }
}
