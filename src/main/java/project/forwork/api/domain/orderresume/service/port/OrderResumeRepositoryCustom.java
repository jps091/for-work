package project.forwork.api.domain.orderresume.service.port;

import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;
import project.forwork.api.domain.orderresume.controller.model.OrderTitleResponse;
import project.forwork.api.domain.orderresume.controller.model.OrderResumePurchaseInfo;
import project.forwork.api.domain.orderresume.model.OrderResume;

import java.util.List;

public interface OrderResumeRepositoryCustom {
    List<OrderResumePurchaseInfo> findAllPurchaseResume(List<OrderResume> orderResumes);
    List<OrderResumeResponse> findByOrderId(Long orderId);
    List<OrderTitleResponse> findOrderTitleByOrderId(Long orderId);
}
