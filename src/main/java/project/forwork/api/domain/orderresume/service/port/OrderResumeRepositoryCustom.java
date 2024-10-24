package project.forwork.api.domain.orderresume.service.port;

import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;
import project.forwork.api.domain.orderresume.controller.model.OrderTitleResponse;
import project.forwork.api.domain.orderresume.controller.model.PurchaseResponse;
import project.forwork.api.domain.orderresume.model.OrderResume;

import java.util.List;

public interface OrderResumeRepositoryCustom {
    List<PurchaseResponse> findAllPurchaseResume(List<OrderResume> orderResumes);
    List<PurchaseResponse> findPurchaseResume(Order order);
    List<OrderResumeResponse> findByOrderId(Long orderId);
    List<OrderTitleResponse> findOrderTitleByOrderId(Long orderId);
}
