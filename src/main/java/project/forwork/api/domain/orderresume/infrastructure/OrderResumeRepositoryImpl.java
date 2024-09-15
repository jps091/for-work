package project.forwork.api.domain.orderresume.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.OrderErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.order.infrastructure.OrderEntity;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.model.PurchaseInfo;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderResumeRepositoryImpl implements OrderResumeRepository {

    private final OrderResumeJpaRepository orderResumeJpaRepository;
    private final OrderResumeQueryDslRepository orderResumeQueryDslRepository;
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
    public OrderResume getByIdWithThrow(long id) {
        return findById(id).orElseThrow(() -> new ApiException(OrderErrorCode.ORDER_NOT_FOUND, id));
    }

    @Override
    public Optional<OrderResume> findById(long id) {
        return orderResumeJpaRepository.findById(id).map(OrderResumeEntity::toModel);
    }

    @Override
    public List<OrderResume> findByIds(List<Long> ids) {
        return orderResumeJpaRepository.findAllById(ids).stream()
                .map(OrderResumeEntity::toModel)
                .toList();
    }

    @Override
    public List<OrderResume> findByOrder(Order order) {
        return orderResumeJpaRepository.findByOrderEntity(OrderEntity.from(order)).stream()
                .map(OrderResumeEntity::toModel)
                .toList();
    }
    @Override
    public List<OrderResume> findByOrders(List<Order> orders) {
        List<OrderEntity> orderEntities = orders.stream().map(OrderEntity::from).toList();
        return orderResumeJpaRepository.findByOrderEntity(orderEntities).stream()
                .map(OrderResumeEntity::toModel)
                .toList();
    }

    @Override
    public Page<PurchaseInfo> getPurchaseResume() {
        return orderResumeQueryDslRepository.getPurchaseInfo();
    }
}
