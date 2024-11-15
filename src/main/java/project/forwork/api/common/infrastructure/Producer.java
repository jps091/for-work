package project.forwork.api.common.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import project.forwork.api.common.config.rabbitmq.enums.ExchangeType;
import project.forwork.api.common.config.rabbitmq.enums.RoutingKey;
import project.forwork.api.common.infrastructure.message.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class Producer {

    private final RabbitTemplate rabbitTemplate;

    // 인증 메일 전송
    public void sendAutCodeMail(String message) {
        sendMail(ExchangeType.AUTH.getExchange(), RoutingKey.AUTH_VERIFY.getKey(), message);
    }
    public void sendTempPasswordMail(TempPasswordMessage message) {
        sendMail(ExchangeType.USER.getExchange(), RoutingKey.USER_TEMP_PASSWORD.getKey(), message);
    }
    // 회원 - 구매자 메일 전송
    public void sendBuyerMail(BuyerMessage message) {
        sendMail(ExchangeType.USER.getExchange(), RoutingKey.USER_BUYER.getKey(), message);
    }

    // 회원 - 판매자 메일 전송
    public void sendSellingMail(SellerMessage message) {
        sendMail(ExchangeType.USER.getExchange(), RoutingKey.USER_SELLER.getKey(), message);
    }

    public void sendSalesRequestResultMail(SalesRequestResultMessage message) {
        sendMail(ExchangeType.USER.getExchange(), RoutingKey.USER_SELLER.getKey(), message);
    }

    // 회원 공통 공지 메일 전송
    public void sendNotice(NoticeMessage message) {
        sendMail(ExchangeType.USER.getExchange(), RoutingKey.USER_NOTICE.getKey(), message);
    }

    private void sendMail(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
