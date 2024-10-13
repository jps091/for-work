package project.forwork.api.domain.orderresume.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.OrderResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom;

import java.util.List;

@Service
@Builder
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderResumeService {

    private final OrderResumeRepository orderResumeRepository;
    private final SendPurchaseResumeService sendPurchaseResumeService;
    private final OrderResumeRepositoryCustom orderResumeRepositoryCustom;
    private final ClockHolder clockHolder;

    public void registerByCartResume(Order order, List<CartResume> cartResumes){
        List<OrderResume> orderResumes = cartResumes.stream()
                .map(cartResume -> OrderResume.create(order, cartResume.getResume()))
                .toList();
        orderResumes.forEach(orderResumeRepository::save);
    }

    // 즉시 구매 확정
    public Order sendMailForNowConfirmedOrder(Long userId, Order order, List<Long> orderResumeIds){
        List<OrderResume> orderResumes = orderResumeRepository.findByStatusAndOrder(OrderResumeStatus.ORDERED, order);
        List<OrderResume> confirmedOrderResumes = updateSelectedOrderResumes(orderResumes, orderResumeIds);
        confirmNowSendMail(order, confirmedOrderResumes);
        OrderStatus updateOrderStatus = getOrderStatusCheckConfirm(orderResumes, confirmedOrderResumes);
        return order.confirmOrderNow(userId, updateOrderStatus);
    }

    public List<OrderResume> updateSelectedOrderResumes(List<OrderResume> orderResumes, List<Long> orderResumeIds){
        List<OrderResume> filteredOrderResumes = orderResumes.stream()
                .filter(orderResume -> orderResumeIds.contains(orderResume.getId()))
                .map(OrderResume::updateStatusConfirm)
                .toList();
        return orderResumeRepository.saveAll(filteredOrderResumes);
    }

    public void confirmNowSendMail(Order order, List<OrderResume> orderResumes){ //
        sendPurchaseResumeService.sendNowPurchaseResume(order);

        orderResumes = orderResumes.stream()
                .map(orderResume -> orderResume.updateStatusSend((clockHolder))).toList();
        orderResumeRepository.saveAll(orderResumes);
    }

    // 자동 주문 확정
    public void sendMailForAutoConfirmedOrder(List<Order> orders){
        List<OrderResume> orderResumes = orderResumeRepository.findByStatusAndOrders(OrderResumeStatus.ORDERED, orders);
        List<OrderResume> filteredOrderResumes = orderResumes.stream()
                .map(OrderResume::updateStatusConfirm)
                .toList();
        List<OrderResume> confirmedOrderResumes = orderResumeRepository.saveAll(filteredOrderResumes); // TODO 검증필요
        confirmAutoSendMail(confirmedOrderResumes);
        log.info("sendMailForConfirmedOrders");
    }

    public void confirmAutoSendMail(List<OrderResume> orderResumes){ //
        sendPurchaseResumeService.sendAllPurchaseResume();

        orderResumes = orderResumes.stream()
                .map(orderResume -> orderResume.updateStatusSend((clockHolder))).toList();
        log.info("confirmAndSendMails");
        orderResumeRepository.saveAll(orderResumes);
    }

    public void cancel(Order order){
        List<OrderResume> orderResumes = orderResumeRepository.findByStatusAndOrder(OrderResumeStatus.ORDERED, order).stream()
                .map(orderResume -> orderResume.updateStatusCancel(clockHolder))
                .toList();
        orderResumeRepository.saveAll(orderResumes);
    }

    public void updateCanceledOrderResumes(List<OrderResume> orderResumes){

        List<OrderResume> updateOrderResumes = orderResumes.stream()
                .map(orderResume -> orderResume.updateStatusCancel(clockHolder))
                .toList();

        orderResumeRepository.saveAll(updateOrderResumes);
    }

    @Transactional(readOnly = true)
    public List<OrderResume> getCancelRequestOrderResumes(List<Long> orderResumeIds, Long orderId){
        List<OrderResume> orderResumes = orderResumeRepository.findByOrderIdAndStatus(orderResumeIds, orderId, OrderResumeStatus.ORDERED);
        validSelected(orderResumeIds, orderResumes);

        return orderResumes;
    }

    private void validSelected(List<Long> orderResumeIds, List<OrderResume> orderResumes) {
        if(orderResumes.size() != orderResumeIds.size()){
            throw new ApiException(OrderResumeErrorCode.NOT_SELECTED);
        }
    }

    private OrderStatus getOrderStatusCheckConfirm(List<OrderResume> orderResumes, List<OrderResume> confirmedOrderResumes) {
        if(orderResumes.size() != confirmedOrderResumes.size()){ // 같지 않으면 주문내 개별로 주문확정
            return OrderStatus.PARTIAL_CONFIRM;
        }else{
            return OrderStatus.CONFIRM; // 요청 ids 갯수와 쿼리 결과 갯수가 일칠하면 전체 주무확정
        }
    }
}
