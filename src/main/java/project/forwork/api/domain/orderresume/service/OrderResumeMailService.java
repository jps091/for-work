package project.forwork.api.domain.orderresume.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.common.service.port.MailSender;
import project.forwork.api.domain.maillog.service.MailLogService;
import project.forwork.api.domain.orderresume.controller.model.PurchaseResponse;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom;
import project.forwork.api.domain.resume.service.ResumeQuantityService;

import java.util.List;

@Service
@Slf4j
@Builder
@RequiredArgsConstructor
public class OrderResumeMailService {

    private final OrderResumeRepositoryCustom orderResumeRepositoryCustom;
    private final OrderResumeRepository orderResumeRepository;
    private final ResumeQuantityService resumeQuantityService;
    private final MailSender mailSender;
    private final ClockHolder clockHolder;
    private final MailLogService mailLogService;

    @Transactional
    public void setupConfirmedResumesAndSendEmail(List<OrderResume> orderResumes){
        List<OrderResume> sentResumes = orderResumes.stream()
                .map(orderResume -> orderResume.updateStatusSent((clockHolder))).toList();

        List<Long> resumeIds = orderResumeRepository.saveAll(sentResumes).stream()
                .map(OrderResume::getResumeId)
                .toList();

        resumeQuantityService.addSalesQuantityWithOnePessimistic(resumeIds); // 판매량 1증가

        List<PurchaseResponse> purchaseResponses = orderResumeRepositoryCustom.findAllPurchaseResume(orderResumes);
        purchaseResponses.forEach(this::sendEmail);
    }

    @Retryable(
            value = { MailSendException.class, MailException.class },
            maxAttempts = 1,
            backoff =  @Backoff(delay = 2000)
    )
    public void sendEmail(PurchaseResponse purchaseResponse){
        String title = "for-work 구매 이력서 : " + purchaseResponse.getSalesPostTitle();
        String content = "주문 번호 #" + purchaseResponse.getOrderId() +" <URL> : "+ purchaseResponse.getResumeUrl();

        try{
            mailSender.send(purchaseResponse.getEmail(), title, content);
            mailLogService.registerSuccessLog(purchaseResponse);
        }catch (Exception e){
            log.error("send email fail orderId={}", purchaseResponse.getOrderId(), e);
            mailLogService.registerFailLog(purchaseResponse, e);
            throw e;
        }
    }
}
