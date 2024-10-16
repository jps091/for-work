package project.forwork.api.domain.orderresume.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.service.port.MailSender;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.port.OrderRepository;
import project.forwork.api.domain.orderresume.controller.model.PurchaseResponse;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SendPurchaseResumeService {

    private final OrderResumeRepositoryCustom orderResumeRepositoryCustom;
    private final MailSender mailSender;

    public void sendAllPurchaseResume(List<OrderResume> orderResumes){
        List<PurchaseResponse> purchaseResponses = orderResumeRepositoryCustom.findAllPurchaseResume(orderResumes);
        purchaseResponses.forEach(this::sendEmail);
    }

    public void sendEmail(PurchaseResponse purchaseResponse){
        String title = "for-work 구매 이력서 : " + purchaseResponse.getSalesPostTitle();
        String content = "주문 번호 #" + purchaseResponse.getOrderId() +" <URL> : "+ purchaseResponse.getResumeUrl();
        //mailSender.send(purchaseResponse.getEmail(), title, content); //TODO 주석해제
        //log.info("sendEmail={}",purchaseResponse.getEmail());
    }
}
