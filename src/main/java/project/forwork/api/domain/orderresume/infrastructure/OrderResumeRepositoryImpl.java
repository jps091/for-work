package project.forwork.api.domain.orderresume.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.OrderResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.order.infrastructure.OrderEntity;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderResumeRepositoryImpl implements OrderResumeRepository {

    private final OrderResumeJpaRepository orderResumeJpaRepository;

    @Override
    public OrderResume save(OrderResume orderResume) {
        return orderResumeJpaRepository.save(OrderResumeEntity.from(orderResume)).toModel();
    }

    @Override
    public List<OrderResume> saveAll(List<OrderResume> orderResumes) {
        List<OrderResumeEntity> orderEntities = orderResumes.stream().map(OrderResumeEntity::from).toList();
        return orderResumeJpaRepository.saveAll(orderEntities).stream().map(OrderResumeEntity::toModel).toList();
    }

    @Override
    public OrderResume getByIdWithThrow(long orderResumeId) {
        return findById(orderResumeId).orElseThrow(() -> new ApiException(OrderResumeErrorCode.ORDER_NOT_FOUND, orderResumeId));
    }

    @Override
    public Optional<OrderResume> findById(long orderResumeId) {
        return orderResumeJpaRepository.findById(orderResumeId).map(OrderResumeEntity::toModel);
    }

    @Override
    public List<OrderResume> findByIds(List<Long> orderResumeIds) {
        return orderResumeJpaRepository.findAllById(orderResumeIds).stream()
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
    public List<OrderResume> findByStatusAndOrders(OrderResumeStatus status, List<Order> orders) {
        List<OrderEntity> orderEntities = orders.stream().map(OrderEntity::from).toList();
        return orderResumeJpaRepository.findByStatusAndOrder(status, orderEntities).stream()
                .map(OrderResumeEntity::toModel)
                .toList();
    }
}
