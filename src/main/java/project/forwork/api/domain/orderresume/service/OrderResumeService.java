package project.forwork.api.domain.orderresume.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.error.OrderResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.cartresume.service.port.CartResumeRepository;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.producer.OrderResumeProducer;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;
import project.forwork.api.domain.resume.model.Resume;

import java.util.List;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
public class OrderResumeService {

    private final OrderResumeRepository orderResumeRepository;
    private final OrderResumeMailService orderResumeMailService;
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
        int totalOrderSize = orderResumeRepository.findByStatusAndOrder(OrderResumeStatus.ORDERED, order).size();
        List<OrderResume> orderedResumes = orderResumeRepository.findByStatusAndOrder(OrderResumeStatus.ORDERED, order)
                .stream()
                .filter(orderResume -> orderResumeIds.contains(orderResume.getId()))
                .map(OrderResume::updateStatusConfirm)
                .toList();

        List<OrderResume> confirmedResumes = orderResumeRepository.saveAll(orderedResumes);
        orderResumeProducer.setupConfirmedResumesAndSendEmail(confirmedResumes);

        int confirmOrderSize = confirmedResumes.size();
        OrderStatus updateOrderStatus = getOrderStatusCheckConfirm(totalOrderSize, confirmOrderSize);

        return order.confirmOrderNow(userId, updateOrderStatus);
    }

    // 자동 주문 확정
    public void sendMailForAutoConfirmedOrder(List<Order> orders){
        List<OrderResume> orderedResumes = orderResumeRepository.findByStatusAndOrders(OrderResumeStatus.ORDERED, orders)
                .stream()
                .map(OrderResume::updateStatusConfirm)
                .toList();
        List<OrderResume> confirmedResumes = orderResumeRepository.saveAll(orderedResumes);
        orderResumeProducer.setupConfirmedResumesAndSendEmail(confirmedResumes);
    }

    public void cancelByOrder(Order order){
        List<OrderResume> canceledResumes = orderResumeRepository.findByStatusAndOrder(OrderResumeStatus.ORDERED, order).stream()
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
        List<OrderResume> orderResumes = orderResumeRepository.findByOrderIdAndStatus(orderResumeIds, orderId, OrderResumeStatus.ORDERED);
        validSelected(orderResumeIds, orderResumes);

        return orderResumes;
    }

    private void validSelected(List<Long> orderResumeIds, List<OrderResume> orderResumes) {
        if(orderResumes.size() != orderResumeIds.size()){
            throw new ApiException(OrderResumeErrorCode.NOT_SELECTED);
        }
    }

    private OrderStatus getOrderStatusCheckConfirm(int totalOrderSize, int confirmOrderSize) {
        if(totalOrderSize != confirmOrderSize){ // 같지 않으면 주문내 개별로 주문확정
            return OrderStatus.PARTIAL_CONFIRM;
        }else{
            return OrderStatus.CONFIRM; // 요청 ids 갯수와 쿼리 결과 갯수가 일칠하면 전체 주무확정
        }
    }
}
