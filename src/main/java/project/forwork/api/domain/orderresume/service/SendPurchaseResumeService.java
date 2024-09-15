package project.forwork.api.domain.orderresume.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.service.port.MailSender;
import project.forwork.api.domain.orderresume.model.PurchaseInfo;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SendPurchaseResumeService {

    private final OrderResumeRepository orderResumeRepository;
    private final MailSender mailSender;

    public void sendPurchaseResume(){
        Page<PurchaseInfo> purchaseResumePage = orderResumeRepository.getPurchaseResume();
        List<PurchaseInfo> purchaseInfos = purchaseResumePage.getContent();

        purchaseInfos.forEach(this::sendEmail);
    }


    private void sendEmail(PurchaseInfo purchaseInfo){
        String title = "for-work 구매 이력서 : " + purchaseInfo.getSalePostTitle();
        String content = "주문 번호  : #" + purchaseInfo.getOrderId() +" <URL> : "+ purchaseInfo.getResumeUrl();
        mailSender.send(purchaseInfo.getEmail(), title, content);
    }
}
