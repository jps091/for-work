package project.forwork.api.domain.orderresume.service;

import io.netty.channel.ConnectTimeoutException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.common.service.port.MailSender;
import project.forwork.api.domain.maillog.service.MailLogService;
import project.forwork.api.domain.orderresume.controller.model.OrderResumePurchaseInfo;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom;
import project.forwork.api.domain.resume.service.ResumeQuantityService;

import java.net.SocketTimeoutException;
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

        List<OrderResumePurchaseInfo> purchaseRespons = orderResumeRepositoryCustom.findAllPurchaseResume(orderResumes);
        purchaseRespons.forEach(this::sendEmail);
    }

    @Retryable(
            value = { MailException.class, SocketTimeoutException.class, ConnectTimeoutException.class },
            maxAttempts = 1,
            backoff =  @Backoff(delay = 2000)
    )
    public void sendEmail(OrderResumePurchaseInfo orderResumePurchaseInfo){
        String title = "for-work 구매 이력서 : "; //+ orderResumeMailMessage.getSalesPostTitle();
        String content = "주문 번호 #" + orderResumePurchaseInfo.getOrderId() +" <URL> : "+ orderResumePurchaseInfo.getResumeUrl();

        try{
            mailSender.send(orderResumePurchaseInfo.getBuyerEmail(), title, content);
        }catch (Exception e){
            log.error("send email fail orderId={}", orderResumePurchaseInfo.getOrderId(), e);
            mailLogService.registerFailLog(orderResumePurchaseInfo, e);
            throw e;
        }
    }
}
