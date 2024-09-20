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

    @Async
    public void sendEmail(PurchaseResponse purchaseResponse){
        String title = "for-work 구매 이력서 : " + purchaseResponse.getSalePostTitle();
        String content = "주문 번호 #" + purchaseResponse.getOrderId() +" <URL> : "+ purchaseResponse.getResumeUrl();
        mailSender.send(purchaseResponse.getEmail(), title, content);
    }
}
