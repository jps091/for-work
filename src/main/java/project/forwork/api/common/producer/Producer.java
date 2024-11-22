package project.forwork.api.common.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import project.forwork.api.common.producer.enums.Exchange;
import project.forwork.api.common.producer.enums.RoutingKey;
import project.forwork.api.domain.order.infrastructure.message.SellingMessage;
import project.forwork.api.domain.orderresume.infrastructure.message.BuyerMessage;
import project.forwork.api.domain.resumedecision.infrastructure.message.SalesRequestResultMessage;
import project.forwork.api.domain.user.infrastructure.message.AdminInquiryMessage;
import project.forwork.api.domain.user.infrastructure.message.NoticeMessage;
import project.forwork.api.domain.user.infrastructure.message.TempPasswordMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class Producer {

    private final RabbitTemplate rabbitTemplate;

    // 인증 메일 전송
    public void sendAutCodeMail(String message) {
        sendMail(Exchange.AUTH.getExchange(), RoutingKey.AUTH_VERIFY.getKey(), message);
    }
    public void sendPasswordMail(TempPasswordMessage message) {
        sendMail(Exchange.USER.getExchange(), RoutingKey.USER_PASSWORD.getKey(), message);
    }
    // 회원 - 구매자 메일 전송
    public void sendBuyerMail(BuyerMessage message) {
        sendMail(Exchange.USER.getExchange(), RoutingKey.USER_BUYER.getKey(), message);
    }

    // 회원 - 판매자 메일 전송
    public void sendSellingMail(SellingMessage message) {
        sendMail(Exchange.USER.getExchange(), RoutingKey.USER_SELLER_SELLING.getKey(), message);
    }

    public void sendSalesRequestResultMail(SalesRequestResultMessage message) {
        sendMail(Exchange.USER.getExchange(), RoutingKey.USER_SELLER_RESULT.getKey(), message);
    }

    // 회원 공통 공지 메일 전송
    public void sendNotice(NoticeMessage message) {
        sendMail(Exchange.USER.getExchange(), RoutingKey.USER_NOTICE.getKey(), message);
    }

    public void sendAdminInquiry(AdminInquiryMessage message) {
        sendMail(Exchange.USER.getExchange(), RoutingKey.USER_ADMIN_INQUIRY.getKey(), message);
    }

    private void sendMail(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message, msg -> {
            msg.getMessageProperties().getHeaders().remove("__TypeId__"); // __TypeId__ 헤더 제거
            return msg;
        });
    }
}
