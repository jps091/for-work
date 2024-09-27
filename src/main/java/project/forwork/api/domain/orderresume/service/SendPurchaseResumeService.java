package project.forwork.api.domain.orderresume.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.service.port.MailSender;
import project.forwork.api.domain.orderresume.infrastructure.OrderResumeRepositoryCustomImpl;
import project.forwork.api.domain.orderresume.controller.model.PurchaseResponse;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SendPurchaseResumeService {

    private final OrderResumeRepositoryCustom orderResumeRepositoryCustom;
    private final MailSender mailSender;

    public void sendPurchaseResume(){
        List<PurchaseResponse> purchaseResponses = orderResumeRepositoryCustom.findPurchaseResume();
        purchaseResponses.forEach(this::sendEmail);
    }

    @Async //TODO 공부필요 1. 톰캣의 쓰레드, 스프링 쓰레드 별개의 것 2. 하지만 둘다 JVM 리소스 사용
    public void sendEmail(PurchaseResponse purchaseResponse){
        String title = "for-work 구매 이력서 : " + purchaseResponse.getSalesPostTitle();
        String content = "주문 번호 #" + purchaseResponse.getOrderId() +" <URL> : "+ purchaseResponse.getResumeUrl();
        mailSender.send(purchaseResponse.getEmail(), title, content);
        log.info("sendEmail={}",purchaseResponse.getEmail());
    }
}
