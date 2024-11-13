package project.forwork.api.common.config.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMqConfig {
    // 인증, 결제, 환불 - Direct Exchange 설정
    public static final String AUTH_EXCHANGE = "auth.direct.exchange";
    // 회원 관련 - Topic Exchange 설정
    public static final String MEMBER_EXCHANGE = "member.topic.exchange";
    // 라우팅 키
    public static final String AUTH_VERIFY_KEY = "auth.verify";
    public static final String MEMBER_BUYER_KEY = "member.buyer";
    public static final String MEMBER_SELLER_KEY = "member.seller";
    public static final String MEMBER_NOTICE_KEY = "member.#"; // 모든 회원에게 공통 메시지

    // 큐 이름 정의
    public static final String VERIFY_QUEUE = "verify.queue";
    public static final String MEMBER_BUYER_QUEUE = "member.buyer.queue";
    public static final String MEMBER_SELLER_QUEUE = "member.seller.queue";
    public static final String MEMBER_NOTICE_QUEUE = "member.notice.queue";

    // Direct Exchange 생성
    @Bean
    public DirectExchange authExchange() {
        return new DirectExchange(AUTH_EXCHANGE);
    }

    // Topic Exchange 생성
    @Bean
    public TopicExchange memberExchange() {
        return new TopicExchange(MEMBER_EXCHANGE);
    }

    // 각각의 큐 생성
    @Bean
    public Queue verifyQueue() {
        return new Queue(VERIFY_QUEUE);
    }

    @Bean
    public Queue memberBuyerQueue() {
        return new Queue(MEMBER_BUYER_QUEUE);
    }

    @Bean
    public Queue memberSellerQueue() {
        return new Queue(MEMBER_SELLER_QUEUE);
    }

    @Bean
    public Queue memberNoticeQueue() {
        return new Queue(MEMBER_NOTICE_QUEUE);
    }

    // Direct 바인딩 설정 (인증, 결제, 환불)
    @Bean
    public Binding verifyBinding(DirectExchange authExchange, Queue verifyQueue) {
        return BindingBuilder.bind(verifyQueue).to(authExchange).with(AUTH_VERIFY_KEY);
    }

    // Topic 바인딩 설정 (회원 관련)
    @Bean
    public Binding memberBuyerBinding(TopicExchange memberExchange, Queue memberBuyerQueue) {
        return BindingBuilder.bind(memberBuyerQueue).to(memberExchange).with(MEMBER_BUYER_KEY);
    }

    @Bean
    public Binding memberSellerBinding(TopicExchange memberExchange, Queue memberSellerQueue) {
        log.info("memberSellerBinding");
        return BindingBuilder.bind(memberSellerQueue).to(memberExchange).with(MEMBER_SELLER_KEY);
    }

    @Bean
    public Binding memberNoticeBinding(TopicExchange memberExchange, Queue memberNoticeQueue) {
        return BindingBuilder.bind(memberNoticeQueue).to(memberExchange).with(MEMBER_NOTICE_KEY);
    }

    // RabbitTemplate 및 메시지 변환기 설정
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper){
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
