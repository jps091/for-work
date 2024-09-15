package project.forwork.api.domain.orderresume.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderResumeService {

    private final OrderResumeRepository orderResumeRepository;
    private final SendPurchaseResumeService sendPurchaseResumeService;

    public void registerByCartResume(Order order, List<CartResume> cartResumes){
        List<OrderResume> orderResumes = cartResumes.stream()
                .map(cartResume -> OrderResume.create(order, cartResume.getResume()))
                .toList();
        orderResumes.forEach(orderResumeRepository::save);
    }

    public void sendMailForConfirmedOrder(Order order){
        List<OrderResume> orderResumes = orderResumeRepository.findByOrder(order).stream()
                .map(orderResume -> orderResume.changeStatus(OrderResumeStatus.CONFIRM))
                .toList();
        orderResumeRepository.saveAll(orderResumes);

        confirmAndSendMails(orderResumes);
    }

    public void sendMailForConfirmedOrders(List<Order> orders){
        List<OrderResume> orderResumes = orderResumeRepository.findByOrders(orders).stream()
                .map(orderResume -> orderResume.changeStatus(OrderResumeStatus.CONFIRM))
                .toList();
        orderResumeRepository.saveAll(orderResumes);

        confirmAndSendMails(orderResumes);
    }

    public void confirmAndSendMails(List<OrderResume> orderResumes){
        sendPurchaseResumeService.sendPurchaseResume();

        orderResumes = orderResumes.stream()
                .map(orderResume -> orderResume.changeStatus(OrderResumeStatus.SENT)).toList();
        orderResumeRepository.saveAll(orderResumes);
    }

    public void cancel(Order order){
        List<OrderResume> orderResumes = orderResumeRepository.findByOrder(order).stream()
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
}
