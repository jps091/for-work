package project.forwork.api.domain.orderresume.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.common.service.port.MailSender;
import project.forwork.api.domain.orderresume.controller.model.PurchaseResponse;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom;
import project.forwork.api.domain.resume.service.ResumeQuantityService;

import java.util.List;

@Service
@Transactional
@Slf4j
@Builder
@RequiredArgsConstructor
public class OrderResumeMailService {

    private final OrderResumeRepositoryCustom orderResumeRepositoryCustom;
    private final OrderResumeRepository orderResumeRepository;
    private final ResumeQuantityService resumeQuantityService;
    private final MailSender mailSender;
    private final ClockHolder clockHolder;

    public void sendResumeMail(List<OrderResume> orderResumes){
        List<PurchaseResponse> purchaseResponses = orderResumeRepositoryCustom.findAllPurchaseResume(orderResumes);
        purchaseResponses.forEach(this::sendEmail);

        List<OrderResume> sentResumes = orderResumes.stream()
                .map(orderResume -> orderResume.updateStatusSend((clockHolder))).toList();

        List<Long> resumeIds = orderResumeRepository.saveAll(sentResumes).stream()
                .map(OrderResume::getResumeId)
                .toList();

        resumeQuantityService.addSalesQuantityWithOnePessimistic(resumeIds);
    }

    public void sendEmail(PurchaseResponse purchaseResponse){
        String title = "for-work 구매 이력서 : " + purchaseResponse.getSalesPostTitle();
        String content = "주문 번호 #" + purchaseResponse.getOrderId() +" <URL> : "+ purchaseResponse.getResumeUrl();
        //mailSender.send(purchaseResponse.getEmail(), title, content); //TODO 주석해제
        //log.info("sendEmail={}",purchaseResponse.getEmail());
    }
}
