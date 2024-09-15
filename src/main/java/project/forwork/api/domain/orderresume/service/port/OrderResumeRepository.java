package project.forwork.api.domain.orderresume.service.port;

import org.springframework.data.domain.Page;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.model.PurchaseInfo;

import java.util.List;
import java.util.Optional;

public interface OrderResumeRepository {
    OrderResume save(OrderResume orderResume);
    List<OrderResume> saveAll(List<OrderResume> orderResumes);
    OrderResume getByIdWithThrow(long id);
    Optional<OrderResume> findById(long id);
    List<OrderResume> findByIds(List<Long> ids);
    List<OrderResume> findByOrder(Order order);
    List<OrderResume> findByOrders(List<Order> orders);
    Page<PurchaseInfo> getPurchaseResume();
}
