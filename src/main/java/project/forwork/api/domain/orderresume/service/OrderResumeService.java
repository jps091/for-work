package project.forwork.api.domain.orderresume.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.cartresume.model.CartResume;
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

    public void registerByCartResume(Order order, List<CartResume> cartResumes){
        List<OrderResume> orderResumes = cartResumes.stream()
                .map(cartResume -> OrderResume.create(order, cartResume.getResume()))
                .toList();
        orderResumes.forEach(orderResumeRepository::save);
    }

    // 즉시 주문확정에 대해
    public void sendMailForConfirmedOrder(Order order){
        List<OrderResume> orderResumes = orderResumeRepository.findByStatusAndOrder(OrderResumeStatus.ORDERED, order).stream()
                .map(orderResume -> orderResume.changeStatus(OrderResumeStatus.CONFIRM))
                .toList();
        orderResumeRepository.saveAll(orderResumes);

        confirmAndSendMail(order, orderResumes);
    }

    public void confirmAndSendMail(Order order, List<OrderResume> orderResumes){
        sendPurchaseResumeService.sendNowPurchaseResume(order);

        orderResumes = orderResumes.stream()
                .map(orderResume -> orderResume.changeStatus(OrderResumeStatus.SENT)).toList();
        log.info("confirmAndSendMails");
        orderResumeRepository.saveAll(orderResumes);
    }

    // 자동 주문 확정에 대해
    public void sendMailForConfirmedOrders(List<Order> orders){
        List<OrderResume> orderResumes = orderResumeRepository.findByStatusAndOrders(OrderResumeStatus.ORDERED, orders).stream()
                .map(orderResume -> orderResume.changeStatus(OrderResumeStatus.CONFIRM))
                .toList();
        orderResumeRepository.saveAll(orderResumes);
        log.info("sendMailForConfirmedOrders");
        confirmAndSendMails(orderResumes);
    }

    public void confirmAndSendMails(List<OrderResume> orderResumes){
        sendPurchaseResumeService.sendAllPurchaseResume();

        orderResumes = orderResumes.stream()
                .map(orderResume -> orderResume.changeStatus(OrderResumeStatus.SENT)).toList();
        log.info("confirmAndSendMails");
        orderResumeRepository.saveAll(orderResumes);
    }

    public void cancel(Order order){
        List<OrderResume> orderResumes = orderResumeRepository.findByStatusAndOrder(OrderResumeStatus.ORDERED, order).stream()
                .map(orderResume -> orderResume.changeStatus(OrderResumeStatus.CANCEL))
                .toList();
        orderResumeRepository.saveAll(orderResumes);
    }

    public List<OrderResume> cancelPartialOrderResumes(List<Long> orderResumeIds){

        List<OrderResume> orderResumes = orderResumeRepository.findByIds(orderResumeIds).stream()
                .map(orderResume -> orderResume.changeStatus(OrderResumeStatus.CANCEL))
                .toList();
        orderResumeRepository.saveAll(orderResumes);

        return orderResumes;
    }
    @Transactional(readOnly = true)
    public List<OrderResumeResponse> getOrderResumeList(CurrentUser currentUser){
        List<OrderResumeStatus> statuses = List.of(OrderResumeStatus.ORDERED, OrderResumeStatus.CONFIRM, OrderResumeStatus.SENT);
        return orderResumeRepositoryCustom.findByUserIdAndStatus(currentUser.getId(), statuses);
    }
    @Transactional(readOnly = true)
    public List<OrderResumeResponse> getCanceledOrderResumeList(CurrentUser currentUser){
        List<OrderResumeStatus> statuses = List.of(OrderResumeStatus.CANCEL);
        return orderResumeRepositoryCustom.findByUserIdAndStatus(currentUser.getId(), statuses);
    }

    @Transactional(readOnly = true)
    public OrderResume getByIdWithThrow(Long orderResumeId){
        return orderResumeRepository.getByIdWithThrow(orderResumeId);
    }
}
