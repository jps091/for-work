package project.forwork.api.domain.orderresume.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.producer.Producer;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.orderresume.infrastructure.message.BuyerMessage;
import project.forwork.api.domain.order.infrastructure.message.SellingMessage;
import project.forwork.api.domain.orderresume.controller.model.OrderResumePurchaseInfo;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderResumeProducer {

    private final Producer producer;
    private final OrderResumeRepositoryCustom orderResumeRepositoryCustom;

    @Transactional
    public void setupConfirmedResumesAndSendEmail(List<OrderResume> orderResumes) {
        List<OrderResumePurchaseInfo> infoList = orderResumeRepositoryCustom.findAllPurchaseResume(orderResumes);
        infoList.forEach(this::produceBuyerMail);
        infoList.forEach(this::produceSellerMail);
    }

    public void produceBuyerMail(OrderResumePurchaseInfo info) {
        String title = createPurchaseTitle(info.getLevel(), info.getField(), info.getResumeId());
        String content = createContent(info);
        BuyerMessage message = BuyerMessage.from(title, content, info);
        producer.sendBuyerMail(message);
    }

    public void produceSellerMail(OrderResumePurchaseInfo info) {
        String email = info.getSellerEmail();
        String title = "for-work #" + info.getResumeId() + " 이력서 판매 내역";
        String content = "해당 #" + info.getResumeId() + " 이력서가 판매 되었습니다.";
        SellingMessage message = SellingMessage.from(email, title, content);
        producer.sendSellingMail(message);
    }

    private String createPurchaseTitle(LevelType level, FieldType field, Long resumeId){
        return "for-work 구매 이력서 : " + level.getDescription() + " " + field.getDescription() + " 이력서 #" + resumeId;
    }

    private static String createContent(OrderResumePurchaseInfo info) {
        return "주문 번호 #" + info.getOrderId() + " <URL> : " + info.getResumeUrl();
    }

    @Async("emailTaskExecutor")
    public void sendLogTest(String message) {
        try {
            Thread.sleep(1); // (1 ms) 각 작업에 소요시간을 Thread.sleep(1)로 대체
            log.info("message : {}", message);
        }catch (Exception e) {
            log.error("[Error] : {} ",e.getMessage());
        }
    }
}

