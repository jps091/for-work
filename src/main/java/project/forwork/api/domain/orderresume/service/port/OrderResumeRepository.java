package project.forwork.api.domain.orderresume.service.port;

import project.forwork.api.domain.orderresume.model.OrderResume;

import java.util.List;
import java.util.Optional;

public interface OrderResumeRepository {
    OrderResume save(OrderResume orderResume);
    void saveAll(List<OrderResume> orderResumes);

    OrderResume getByIdWithThrow(long id);
    Optional<OrderResume> findById(long id);
    List<OrderResume> findByOrderId(long orderId);
}
