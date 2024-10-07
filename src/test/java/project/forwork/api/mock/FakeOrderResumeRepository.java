package project.forwork.api.mock;

import project.forwork.api.common.error.OrderResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeOrderResumeRepository implements OrderResumeRepository {

    private final AtomicLong id = new AtomicLong(0);
    private final List<OrderResume> data = new ArrayList<>();

    @Override
    public OrderResume save(OrderResume orderResume) {
        if(orderResume.getId() == null || orderResume.getId() == 0){
            OrderResume newOrderResume = OrderResume.builder()
                    .id(id.incrementAndGet())
                    .order(orderResume.getOrder())
                    .resume(orderResume.getResume())
                    .build();
            data.add(newOrderResume);
            return newOrderResume;
        }else{
            data.removeIf(o -> Objects.equals(o.getId(), orderResume.getId()));
            data.add(orderResume);
            return orderResume;
        }
    }

    @Override
    public List<OrderResume> saveAll(List<OrderResume> orderResumes) {
        return orderResumes.stream().map(this::save).toList();
    }

    @Override
    public OrderResume getByIdWithThrow(long orderResumeId) {
        return findById(orderResumeId).orElseThrow(() -> new ApiException(OrderResumeErrorCode.NOT_FOUND, orderResumeId));
    }

    @Override
    public Optional<OrderResume> findById(long orderResumeId) {
        return data.stream().filter(o -> Objects.equals(o.getId(), orderResumeId)).findAny();
    }

    @Override
    public List<OrderResume> findByIds(List<Long> orderResumeIds) {
        return data.stream()
                .filter(orderResume -> orderResumeIds.contains(orderResume.getId()))
                .toList();
    }

    @Override
    public List<OrderResume> findByStatusAndOrder(OrderResumeStatus status, Order order) {
        return data.stream()
                .filter(orderResume -> Objects.equals(orderResume.getOrder().getId(), order.getId()) &&
                        Objects.equals(orderResume.getStatus(), OrderResumeStatus.ORDERED))
                .toList();
    }

    @Override
    public List<OrderResume> findByStatusAndOrders(OrderResumeStatus status, List<Order> orders) {
        List<Long> orderIds = orders.stream()
                .map(Order::getId)  // Order 객체에서 ID 추출
                .toList();

        return data.stream()
                .filter(orderResume -> orderIds.contains(orderResume.getOrder().getId()) &&
                        Objects.equals(orderResume.getStatus(), OrderResumeStatus.ORDERED))
                .toList();
    }
}
