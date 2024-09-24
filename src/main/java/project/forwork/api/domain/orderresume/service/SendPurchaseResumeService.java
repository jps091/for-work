package project.forwork.api.domain.orderresume.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.service.port.MailSender;
import project.forwork.api.domain.orderresume.infrastructure.OrderResumeQueryDslRepository;
import project.forwork.api.domain.orderresume.controller.model.PurchaseResponse;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SendPurchaseResumeService {

    private final OrderResumeQueryDslRepository orderResumeQueryDslRepository;
    private final MailSender mailSender;

    public void sendPurchaseResume(){
        Page<PurchaseResponse> purchaseResumePage = orderResumeQueryDslRepository.findPurchaseResume();
        List<PurchaseResponse> purchaseResponses = purchaseResumePage.getContent();

        purchaseResponses.forEach(this::sendEmail);
    }

    @Async //TODO 공부필요 1. 톰캣의 쓰레드, 스프링 쓰레드 별개의 것 2. 하지만 둘다 JVM 리소스 사용
    public void sendEmail(PurchaseResponse purchaseResponse){
        String title = "for-work 구매 이력서 : " + purchaseResponse.getSalesPostTitle();
        String content = "주문 번호 #" + purchaseResponse.getOrderId() +" <URL> : "+ purchaseResponse.getResumeUrl();
        mailSender.send(purchaseResponse.getEmail(), title, content);
    }
}
