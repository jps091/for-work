package project.forwork.api.domain.order.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.OrderErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.common.service.port.UuidHolder;
import project.forwork.api.domain.order.controller.model.*;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.infrastructure.model.ResumeDto;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.model.Orders;
import project.forwork.api.domain.order.service.port.OrderCommandPort;
import project.forwork.api.domain.order.service.port.OrderQueryPort;
import project.forwork.api.domain.order.service.port.OrderViewPort;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;
import project.forwork.api.domain.orderresume.controller.model.OrderTitleResponse;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.producer.OrderResumeProducer;
import project.forwork.api.domain.resume.service.port.ResumeRepository;

import java.util.List;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ResumeRepository resumeRepository; //TODO 어댑터로 변경
    private final ClockHolder clockHolder;
    private final UuidHolder uuidHolder;

    private final OrderCommandPort orderCommandPort;
    private final OrderQueryPort orderQueryPort;
    private final OrderViewPort orderViewPort;
    private final OrderResumeProducer orderResumeProducer;

    public Order create(CurrentUser currentUser, ConfirmPaymentRequest body){
        Order order = Order.create(currentUser.getId(), body.getRequestId(), body.getAmount(), clockHolder);
        List<ResumeDto> resumeDtos = resumeRepository.findByIds(body.getResumeIds()).stream()
                .map(re -> new ResumeDto(re.getId(), re.getPrice()))
                .toList();
        return orderCommandPort.save(order, resumeDtos);
    }

    public void orderConfirmNow(CurrentUser currentUser, Long orderId, ConfirmOrderRequest body){
        Order order = orderQueryPort.getByIdWithThrow(orderId);
        order.validBuyer(currentUser);
        order = order.confirmOrderResumes(body.getOrderResumeIds());
        Order confirmedOrder = orderCommandPort.update(order);
        orderResumeProducer.setupConfirmedResumesAndSendEmail(confirmedOrder.getOrderResumes());
    }

    public void cancelOrder(CurrentUser currentUser, Order order){
        order.validBuyer(currentUser);
        Order canceledOrder = order.cancelOrderWithThrow(clockHolder);
        orderCommandPort.update(canceledOrder);
    }

    public void cancelPartialOrder(CurrentUser currentUser, Order order, List<OrderResume> orderResumes){
        order.validBuyer(currentUser);
        Order canceledPartialOrder = order.cancelPartialOrder(currentUser.getId(), orderResumes, clockHolder);
        orderCommandPort.update(canceledPartialOrder);
    }

    public void updatedOrderStatus(OrderStatus oldStatus, OrderStatus updatedStatus) {
        int limit = 10;
        while (true) {
            Orders orders = orderQueryPort.findByStatus(oldStatus, limit);

            // 더 이상 처리할 주문이 없으면 반복 종료
            if (orders.isEmpty()) {
                break;
            }

            Orders updatedOrders = orders.updateOrdersStatus(updatedStatus);
            sendMailByOrderConfirm(updatedOrders, updatedStatus);
            orderCommandPort.updateAll(updatedOrders);
        }
    }

    // requestId = 현재 시간 (millis) / 5000 + "_" + userId + "-" + uuid 5자리
    // 동일 유저가 5초 이내에 재 요청을 할 경우 예외 발생
    @Transactional(readOnly = true)
    public void validRequestId(String requestId) {
        orderQueryPort.findByRequestId(requestId).ifPresent(order -> {
            if (isRequestIdEqual(order.getRequestId(), requestId)) {
                throw new ApiException(OrderErrorCode.ORDER_ALREADY_REQUEST);
            }
        });
    }

    @Transactional(readOnly = true)
    public String getRequestIdByOrderId(Long orderId){
        Order order = orderQueryPort.getByIdWithThrow(orderId);
        return order.getRequestId();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(CurrentUser currentUser){

        Orders orders = orderQueryPort.findByUserId(currentUser.getId());
        orders.checkIsEmptyWithThrow();

        return orders.getOrderResponses().stream()
                .map(orderResponse -> {
                    List<OrderTitleResponse> orderTitles = orderViewPort.findOrderTitleByOrderId(orderResponse.getOrderId());
                    String orderResumeTitle = orderTitles.get(0).getTitle();
                    String orderTitle = createOrderTitle(orderTitles, orderResumeTitle);
                    return OrderResponse.from(orderResponse, orderTitle);
                }).toList();
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(CurrentUser currentUser, Long orderId){
        Order order = orderQueryPort.getByIdWithThrow(orderId);
        List<OrderResumeResponse> orderResumes = orderViewPort.findByOrderId(order.getId());
        return OrderDetailResponse.from(order, orderResumes, currentUser.getEmail());
    }

    private boolean isRequestIdEqual(String source, String target){
        try {
            // "_" 밀리초 값, 사용자 ID 추출
            String[] sourceParts = source.split("_");
            String[] targetParts = target.split("_");

            // 밀리초 값 추출
            long sourceMillis = Long.parseLong(sourceParts[0]);
            long targetMillis = Long.parseLong(targetParts[0]);

            // 사용자 ID 추출
            String sourceUserId = sourceParts[1].split("-")[0];
            String targetUserId = targetParts[1].split("-")[0];

            // 사용자 ID와 "Millis / 5000" 값 비교
            return sourceUserId.equals(targetUserId) && (sourceMillis / 5000 == targetMillis / 5000);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private static String createOrderTitle(List<OrderTitleResponse> orderTitles, String orderResumeTitle) {
        int rest = orderTitles.size() - 1;
        return orderTitles.size() == 1 ? orderResumeTitle : orderResumeTitle + " 외 " + rest + "건";
    }

    private void sendMailByOrderConfirm(Orders orders, OrderStatus updatedStatus) {
        if (OrderStatus.PARTIAL_CONFIRM.equals(updatedStatus) || OrderStatus.CONFIRM.equals(updatedStatus)) {
            orders.getOrders().stream()
                    .map(Order::confirmPaidResumes)
                    .forEach(orderResumeProducer::setupConfirmedResumesAndSendEmail);
        }
    }
}