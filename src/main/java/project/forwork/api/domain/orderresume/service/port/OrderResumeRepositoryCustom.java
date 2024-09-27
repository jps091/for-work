package project.forwork.api.domain.orderresume.service.port;

import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;
import project.forwork.api.domain.orderresume.controller.model.PurchaseResponse;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;

import java.util.List;

public interface OrderResumeRepositoryCustom {
    List<PurchaseResponse> findPurchaseResume() ;
    List<OrderResumeResponse> findByUserIdAndStatus(Long userId, List<OrderResumeStatus> statuses);
    List<OrderResumeResponse> findByOrderId(Long orderId);
}
