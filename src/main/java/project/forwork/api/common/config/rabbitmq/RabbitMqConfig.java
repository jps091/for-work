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

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class RabbitMqConfig {
    // 인증 - Direct
    public static final String AUTH_EXCHANGE = "auth.direct.exchange";
    // 회원 관련 - Topic
    public static final String USER_EXCHANGE = "user.topic.exchange";
    // 예외 처리 - Fanout
    public static final String RETRY_EXCHANGE = "retry.fanout.exchange";
    // 라우팅 키
    public static final String AUTH_VERIFY_KEY = "auth.verify";
    public static final String USER_BUYER_KEY = "user.buyer";
    public static final String USER_SELLER_KEY = "user.seller";
    public static final String USER_NOTICE_KEY = "user.notice";

    // Queue
    public static final String VERIFY_QUEUE = "verify.queue";
    public static final String USER_BUYER_QUEUE = "user.buyer.queue";
    public static final String USER_SELLER_QUEUE = "user.seller.queue";
    public static final String USER_NOTICE_QUEUE = "user.notice.queue";
    public static final String RETRY_QUEUE = "retry.queue";

    @Bean
    public DirectExchange authExchange() {
        return new DirectExchange(AUTH_EXCHANGE);
    }


    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public FanoutExchange retryExchange() {
        return new FanoutExchange(RETRY_EXCHANGE);
    }

    // 큐 설정에 x-dead-letter-exchange 추가 -> 실패 시 RETRY_EXCHANGE 메시지 전달
    @Bean
    public Queue verifyQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", RETRY_EXCHANGE);
        return new Queue(VERIFY_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue userBuyerQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", RETRY_EXCHANGE);
        return new Queue(USER_BUYER_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue userSellerQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", RETRY_EXCHANGE);
        return new Queue(USER_SELLER_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue userNoticeQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", RETRY_EXCHANGE);
        return new Queue(USER_NOTICE_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue retryQueue() {
        return new Queue(RETRY_QUEUE);
    }

    @Bean
    public Binding verifyBinding(DirectExchange authExchange, Queue verifyQueue) {
        return BindingBuilder.bind(verifyQueue).to(authExchange).with(AUTH_VERIFY_KEY);
    }

    @Bean
    public Binding userBuyerBinding(TopicExchange userExchange, Queue userBuyerQueue) {
        return BindingBuilder.bind(userBuyerQueue).to(userExchange).with(USER_BUYER_KEY);
    }

    @Bean
    public Binding userSellerBinding(TopicExchange userExchange, Queue userSellerQueue) {
        log.info("userSellerBinding");
        return BindingBuilder.bind(userSellerQueue).to(userExchange).with(USER_SELLER_KEY);
    }

    @Bean
    public Binding userNoticeBinding(TopicExchange userExchange, Queue userNoticeQueue) {
        return BindingBuilder.bind(userNoticeQueue).to(userExchange).with(USER_NOTICE_KEY);
    }

    @Bean
    public Binding failAlarmBinding(FanoutExchange retryExchange, Queue retryQueue) {
        return BindingBuilder.bind(retryQueue).to(retryExchange);
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
