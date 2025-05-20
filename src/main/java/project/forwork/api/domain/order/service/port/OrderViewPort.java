package project.forwork.api.domain.order.service.port;

import project.forwork.api.domain.orderresume.controller.model.OrderResumePurchaseInfo;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;
import project.forwork.api.domain.orderresume.controller.model.OrderTitleResponse;
import project.forwork.api.domain.orderresume.model.OrderResumes;

import java.util.List;

/***
 * index : 1. status 2. requestId
 */
public interface OrderViewPort {
    List<OrderResumePurchaseInfo> findAllPurchaseResume(OrderResumes orderResumes);
    List<OrderResumeResponse> findByOrderId(Long orderId);
    List<OrderTitleResponse> findOrderTitleByOrderId(Long orderId);
}
