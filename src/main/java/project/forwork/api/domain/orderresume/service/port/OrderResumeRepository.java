package project.forwork.api.domain.orderresume.service.port;

import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;

import java.util.List;
import java.util.Optional;

public interface OrderResumeRepository {
    OrderResume save(OrderResume orderResume);
    List<OrderResume> saveAll(List<OrderResume> orderResumes);
    OrderResume getByIdWithThrow(long orderResumeId);
    Optional<OrderResume> findById(long orderResumeId);
    List<OrderResume> findByIds(List<Long> orderResumeIds);
    List<OrderResume> findByStatusAndOrder(OrderResumeStatus status, Order order);
    List<OrderResume> findByStatusAndOrders(OrderResumeStatus status, List<Order> orders);
}
