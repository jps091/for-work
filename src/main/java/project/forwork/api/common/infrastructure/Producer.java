package project.forwork.api.common.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import project.forwork.api.common.infrastructure.model.NoticeMessage;
import project.forwork.api.common.infrastructure.model.SalesRequestResultMessage;
import project.forwork.api.common.infrastructure.model.SellerMessage;
import project.forwork.api.common.infrastructure.model.BuyerMessage;

import static project.forwork.api.common.config.rabbitmq.RabbitMqConfig.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class Producer {

    private final RabbitTemplate rabbitTemplate;

    // 인증 메일 전송
    public void sendAuthMail(String message) {
        sendMail(AUTH_EXCHANGE, AUTH_VERIFY_KEY, message);
    }

    // 회원 - 구매자 메일 전송
    public void sendBuyerMail(BuyerMessage message) {
        sendMail(MEMBER_EXCHANGE, MEMBER_BUYER_KEY, message);
    }

    // 회원 - 판매자 메일 전송
    public void sendSellingMail(SellerMessage message) {
        sendMail(MEMBER_EXCHANGE, MEMBER_SELLER_KEY, message);
    }

    public void sendSalesRequestResultMail(SalesRequestResultMessage message) {
        log.info("sendSalesRequestResultMail={}", message);
        sendMail(MEMBER_EXCHANGE, MEMBER_SELLER_KEY, message);
    }

    // 회원 공통 공지 메일 전송
    public void sendMemberNotice(NoticeMessage message) {
        sendMail(MEMBER_NOTICE_KEY, MEMBER_EXCHANGE, message);
    }

    private void sendMail(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
