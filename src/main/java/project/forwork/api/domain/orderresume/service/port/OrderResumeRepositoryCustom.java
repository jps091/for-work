package project.forwork.api.domain.orderresume.service.port;

import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;
import project.forwork.api.domain.orderresume.controller.model.OrderTitleResponse;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeMailMessage;
import project.forwork.api.domain.orderresume.model.OrderResume;

import java.util.List;

public interface OrderResumeRepositoryCustom {
    List<OrderResumeMailMessage> findAllPurchaseResume(List<OrderResume> orderResumes);
    List<OrderResumeMailMessage> findPurchaseResume(Order order);
    List<OrderResumeResponse> findByOrderId(Long orderId);
    List<OrderTitleResponse> findOrderTitleByOrderId(Long orderId);
}
