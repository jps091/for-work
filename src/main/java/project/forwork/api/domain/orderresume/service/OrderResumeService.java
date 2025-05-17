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


    @Transactional(readOnly = true)
    public List<OrderResume> getCancelRequestOrderResumes(List<Long> orderResumeIds, Long orderId){
        List<OrderResume> orderResumes = orderResumeRepository.findByOrderIdAndStatus(orderResumeIds, orderId, OrderResumeStatus.PAID);
        validSelected(orderResumeIds, orderResumes);

        return orderResumes;
    }

    private void validSelected(List<Long> orderResumeIds, List<OrderResume> orderResumes) {
        if(orderResumes.size() != orderResumeIds.size()){
            throw new ApiException(OrderResumeErrorCode.CANCEL_FAIL);
        }
    }
}
