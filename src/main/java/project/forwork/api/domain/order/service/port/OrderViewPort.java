package project.forwork.api.domain.order.service.port;

import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.model.Orders;
import project.forwork.api.domain.orderresume.controller.model.OrderResumePurchaseInfo;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;
import project.forwork.api.domain.orderresume.controller.model.OrderTitleResponse;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;

import java.util.List;
import java.util.Optional;

/***
 * index : 1. status 2. requestId
 */
public interface OrderViewPort {
    List<OrderResumePurchaseInfo> findAllPurchaseResume(List<OrderResume> orderResumes);
    List<OrderResumeResponse> findByOrderId(Long orderId);
    List<OrderTitleResponse> findOrderTitleByOrderId(Long orderId);
}
