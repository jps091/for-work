package project.forwork.api.domain.orderresume.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.error.OrderResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.cartresume.service.port.CartResumeRepository;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.model.Orders;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;
import project.forwork.api.domain.resume.model.Resume;

import java.util.List;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
public class OrderResumeService {

    private final OrderResumeRepository orderResumeRepository;
    private final CartResumeRepository cartResumeRepository;
    private final ClockHolder clockHolder;
    private final OrderResumeProducer orderResumeProducer;

    public void createByResumes(Order order, List<Resume> resumes){
        List<OrderResume> orderResumes = resumes.stream()
                .map(resume -> OrderResume.create(order, resume))
                .toList();
        orderResumeRepository.saveAll(orderResumes);
    }

    // 즉시 구매 확정
    public Order sendMailForNowConfirmedOrder(Long userId, Order order, List<Long> orderResumeIds){
        List<OrderResume> orderedResumes = orderResumeRepository.findByStatusAndOrder(OrderResumeStatus.PAID, order)
                .stream()
                .filter(orderResume -> orderResumeIds.contains(orderResume.getId()))
                .map(OrderResume::updateStatusConfirm)
                .toList();

        OrderStatus updateOrderStatus = checkOrderConfirmation(order, orderedResumes);
        List<OrderResume> confirmedResumes = orderResumeRepository.saveAll(orderedResumes);
        orderResumeProducer.setupConfirmedResumesAndSendEmail(confirmedResumes);

        return order.confirmOrderNow(userId, updateOrderStatus);
    }

    // 자동 주문 확정
    public void sendMailForAutoConfirmedOrder(Orders orders){
        List<OrderResume> orderedResumes = orderResumeRepository.findByStatusAndOrders(OrderResumeStatus.PAID, orders);
        if(orderedResumes.isEmpty()){
            return;
        }
        List<OrderResume> updatedOrderedResumes = orderedResumes.stream()
                .map(OrderResume::updateStatusConfirm)
                .toList();
        List<OrderResume> confirmedResumes = orderResumeRepository.saveAll(updatedOrderedResumes);
        orderResumeProducer.setupConfirmedResumesAndSendEmail(confirmedResumes);
    }

    public void cancelByOrder(Order order){
        List<OrderResume> canceledResumes = orderResumeRepository.findByStatusAndOrder(OrderResumeStatus.PAID, order).stream()
                .map(orderResume -> orderResume.updateStatusCancel(clockHolder))
                .toList();
        orderResumeRepository.saveAll(canceledResumes);
    }

    public void cancelByOrderResumes(List<OrderResume> orderResumes){
        List<OrderResume> updateOrderResumes = orderResumes.stream()
                .map(orderResume -> orderResume.updateStatusCancel(clockHolder))
                .toList();
        orderResumeRepository.saveAll(updateOrderResumes);
    }

    @Transactional(readOnly = true)
    public List<OrderResume> getCancelRequestOrderResumes(List<Long> orderResumeIds, Long orderId){
        List<OrderResume> orderResumes = orderResumeRepository.findByOrderIdAndStatus(orderResumeIds, orderId, OrderResumeStatus.PAID);
        validSelected(orderResumeIds, orderResumes);

        return orderResumes;
    }

    @Transactional(readOnly = true)
    public OrderStatus checkOrderConfirmation(Order order, List<OrderResume> selectOrder) {
        List<OrderResume> orderResumes = orderResumeRepository.findByOrderId(order.getId());

        if (isPartialConfirmStatus(orderResumes, selectOrder)) {
            return OrderStatus.PARTIAL_CONFIRM;
        }
        if (isFullConfirmStatus(orderResumes, selectOrder)) {
            return OrderStatus.CONFIRM;
        }

        return order.getStatus();
    }

    private boolean isPartialConfirmStatus(List<OrderResume> orderResumes, List<OrderResume> selectOrder){
        int totalSize = orderResumes.size();
        long confirmedSize = getListSizeByOrderResumeStatus(orderResumes, OrderResumeStatus.CONFIRM);
        long canceledSize = getListSizeByOrderResumeStatus(orderResumes, OrderResumeStatus.CANCEL);
        int confirmNowSize = selectOrder.size();

        return canceledSize > 0 && (canceledSize + confirmNowSize + confirmedSize == totalSize);
    }

    private boolean isFullConfirmStatus(List<OrderResume> orderResumes, List<OrderResume> selectOrder){
        int totalSize = orderResumes.size();
        long confirmedSize = getListSizeByOrderResumeStatus(orderResumes, OrderResumeStatus.CONFIRM);
        int confirmNowSize = selectOrder.size();

        return confirmedSize + confirmNowSize == totalSize;
    }
    private static long getListSizeByOrderResumeStatus(List<OrderResume> orderResumes, OrderResumeStatus status) {
        return orderResumes.stream()
                .filter(resume -> resume.getStatus() == status)
                .count();
    }

    private void validSelected(List<Long> orderResumeIds, List<OrderResume> orderResumes) {
        if(orderResumes.size() != orderResumeIds.size()){
            throw new ApiException(OrderResumeErrorCode.CANCEL_FAIL);
        }
    }

    @Transactional(readOnly = true)
    public OrderStatus checkOrderConfirmation1(Order order, List<OrderResume> selectOrder) {
        int totalOrderSize = orderResumeRepository.findByStatusAndOrder(OrderResumeStatus.PAID, order).size();
        int confirmOrderSize = selectOrder.size();
        if(totalOrderSize != confirmOrderSize){
            return order.getStatus();
        }

        return OrderStatus.CONFIRM;
    }

    @Transactional(readOnly = true)
    public OrderStatus checkOrderConfirmation2(Order order, List<OrderResume> selectOrder) {
        int totalSize = orderResumeRepository.findByOrderId(order.getId()).size();
        int confirmSize = orderResumeRepository.findByStatusAndOrder(OrderResumeStatus.CONFIRM, order).size();
        int cancelSize = orderResumeRepository.findByStatusAndOrder(OrderResumeStatus.CANCEL, order).size();
        int confirmOrderSize = selectOrder.size();

        if(cancelSize != 0){
            if(cancelSize + confirmOrderSize + confirmSize == totalSize){
                return OrderStatus.PARTIAL_CONFIRM;
            }
        }

        if(confirmSize + confirmOrderSize == totalSize){
            return OrderStatus.CONFIRM;
        }
        return order.getStatus();
        // 요청 ids 갯수와 쿼리 결과 갯수가 일치 하면 전체 주무확정
    }

    @Transactional(readOnly = true)
    public OrderStatus checkOrderConfirmation3(Order order, List<OrderResume> selectOrder) {
        List<OrderResume> orderResumes = orderResumeRepository.findByOrderId(order.getId());

        // Calculate counts
        int totalSize = orderResumes.size();
        long confirmSize = orderResumes.stream()
                .filter(resume -> resume.getStatus() == OrderResumeStatus.CONFIRM)
                .count();
        long cancelSize = orderResumes.stream()
                .filter(resume -> resume.getStatus() == OrderResumeStatus.CANCEL)
                .count();
        int confirmOrderSize = selectOrder.size();

        // Determine the order status
        if (cancelSize > 0 && (cancelSize + confirmOrderSize + confirmSize == totalSize)) {
            return OrderStatus.PARTIAL_CONFIRM;
        }

        if (confirmSize + confirmOrderSize == totalSize) {
            return OrderStatus.CONFIRM;
        }

        return order.getStatus();
    }
}
