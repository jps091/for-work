package project.forwork.api.domain.order.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.OrderErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.common.service.port.UuidHolder;
import project.forwork.api.domain.order.controller.model.*;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.port.OrderRepository;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;
import project.forwork.api.domain.orderresume.controller.model.OrderTitleResponse;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.OrderResumeService;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.List;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderResumeRepositoryCustom orderResumeRepositoryCustom;
    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final OrderResumeService orderResumeService;
    private final ClockHolder clockHolder;
    private final UuidHolder uuidHolder;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Order create(CurrentUser currentUser, ConfirmPaymentRequest body){
        User user = userRepository.getByIdWithThrow(currentUser.getId());

        List<Resume> resumes = resumeRepository.findByIds(body.getResumeIds());

        Order order = Order.create(user, body.getRequestId(), body.getAmount());
        order = orderRepository.save(order);

        orderResumeService.createByResumes(order, resumes);
        return order;
    }

    public void orderConfirmNow(CurrentUser currentUser, Long orderId, ConfirmOrderRequest body){
        Order order = orderRepository.getByIdWithThrow(orderId);
        order = orderResumeService.sendMailForNowConfirmedOrder(currentUser.getId(), order, body.getOrderResumeIds());
        orderRepository.save(order);
    }

    public Order cancelOrder(CurrentUser currentUser, Long orderId){
        Order order = orderRepository.getByIdWithThrow(orderId);
        order = order.cancelOrder(currentUser.getId());
        orderResumeService.cancelByOrder(order);
        return orderRepository.save(order);
    }

    public Order cancelPartialOrder(CurrentUser currentUser, Long orderId, List<OrderResume> orderResumes){
        Order order = orderRepository.getByIdWithThrow(orderId);
        order = order.cancelPartialOrder(currentUser.getId(), orderResumes);
        orderResumeService.cancelByOrderResumes(orderResumes);
        return orderRepository.save(order);
    }

    public List<Order> updateOrdersByStatus(List<Order> orders, OrderStatus status) {
        List<Order> updatedOrders = orders.stream()
                .map(order -> order.updateStatus(status))
                .toList();

        return orderRepository.saveAll(updatedOrders);
    }

    public Order updateOrderPaid(Order order) {
        Order paidOrder = order.updatePaid(clockHolder);
        return orderRepository.save(paidOrder);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateOrderConfirmFailure(Order order) {
        Order failedOrder = order.updateStatus(OrderStatus.PAYMENT_FAILED);
        orderRepository.save(failedOrder);
        orderResumeService.updateFailByOrder(failedOrder);
    }


    // requestId = 현재 시간 (millis) / 5000 + "#" + userId + "-" + uuid 5자리
    // 동일 유저가 5초 이내에 재 요청을 할 경우 예외 발생
    @Transactional(readOnly = true)
    public void validRequestId(String requestId) {
        orderRepository.findByRequestId(requestId).ifPresent(order -> {
            if (isRequestIdEqual(order.getRequestId(), requestId)) {
                throw new ApiException(OrderErrorCode.ORDER_ALREADY_REQUEST);
            }
        });
    }

    @Transactional(readOnly = true)
    public String getRequestIdByOrderId(Long orderId){
        Order order = orderRepository.getByIdWithThrow(orderId);
        return order.getRequestId();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(CurrentUser currentUser){

        List<Order> orders = orderRepository.findByUserId(currentUser.getId());
        if(orders.isEmpty()){
            throw new ApiException(OrderErrorCode.ORDER_NO_CONTENT);
        }

        return orders.stream()
                .map(order -> {
                    List<OrderTitleResponse> orderTitles = orderResumeRepositoryCustom.findOrderTitleByOrderId(order.getId());
                    String orderResumeTitle = orderTitles.get(0).getTitle();
                    String orderTitle = createOrderTitle(orderTitles, orderResumeTitle);
                    return OrderResponse.from(order, orderTitle);
                }).toList();
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(CurrentUser currentUser, Long orderId){
        Order order = orderRepository.getOrderWithThrow(currentUser.getId(), orderId);
        List<OrderResumeResponse> orderResumes = orderResumeRepositoryCustom.findByOrderId(order.getId());
        return OrderDetailResponse.from(order, orderResumes);
    }

    @Transactional(readOnly = true)
    public List<Order> findOrdersByStatus(OrderStatus status, int limit){
        return orderRepository.findByStatus(status, limit);
    }

    private boolean isRequestIdEqual(String source, String target){
        return source.split("-")[0].equals(target.split("-")[0]);
    }

    private static String createOrderTitle(List<OrderTitleResponse> orderTitles, String orderResumeTitle) {
        return orderTitles.size() == 1 ? orderResumeTitle : orderResumeTitle + "외 " + orderTitles.size() + "건";
    }
}