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
import project.forwork.api.common.producer.enums.Exchange;
import project.forwork.api.common.producer.enums.QueueType;
import project.forwork.api.common.producer.enums.RoutingKey;

@Configuration
@Slf4j
public class RabbitMqConfig {
    public static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";

    @Bean
    public DirectExchange authExchange() {
        return new DirectExchange(Exchange.AUTH.getExchange());
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(Exchange.USER.getExchange());
    }

    @Bean
    public FanoutExchange retryExchange() {
        return new FanoutExchange(Exchange.RETRY.getExchange());
    }

    // 큐 설정에 x-dead-letter-exchange 추가 -> 실패 시 RETRY_EXCHANGE 메시지 전달
    @Bean
    public Queue verifyQueue() {
        return QueueBuilder.durable(QueueType.VERIFY.getQueue())
                .withArgument(X_DEAD_LETTER_EXCHANGE, Exchange.RETRY.getExchange())
                .build();
    }
    @Bean
    public Queue userBuyerQueue() {
        return QueueBuilder.durable(QueueType.USER_BUYER.getQueue())
                .withArgument(X_DEAD_LETTER_EXCHANGE, Exchange.RETRY.getExchange())
                .build();
    }

    @Bean
    public Queue userSellerSellingQueue() {
        return QueueBuilder.durable(QueueType.USER_SELLER_SELLING.getQueue())
                .withArgument(X_DEAD_LETTER_EXCHANGE, Exchange.RETRY.getExchange())
                .build();
    }

    @Bean
    public Queue userSellerResultQueue() {
        return QueueBuilder.durable(QueueType.USER_SELLER_RESULT.getQueue())
                .withArgument(X_DEAD_LETTER_EXCHANGE, Exchange.RETRY.getExchange())
                .build();
    }

    @Bean
    public Queue userNoticeQueue() {
        return QueueBuilder.durable(QueueType.USER_NOTICE.getQueue())
                .withArgument(X_DEAD_LETTER_EXCHANGE, Exchange.RETRY.getExchange())
                .build();
    }

    @Bean
    public Queue userAdminInquiryQueue() {
        return QueueBuilder.durable(QueueType.USER_ADMIN_INQUIRY.getQueue())
                .withArgument(X_DEAD_LETTER_EXCHANGE, Exchange.RETRY.getExchange())
                .build();
    }

    @Bean
    public Queue userPasswordQueue() {
        return QueueBuilder.durable(QueueType.USER_PASSWORD.getQueue())
                .withArgument(X_DEAD_LETTER_EXCHANGE, Exchange.RETRY.getExchange())
                .build();
    }

    @Bean
    public Queue retryQueue() {
        return QueueBuilder.durable(QueueType.RETRY.getQueue()).build();
    }

    @Bean
    public Binding verifyBinding(DirectExchange authExchange, Queue verifyQueue) {
        return BindingBuilder.bind(verifyQueue).to(authExchange).with(RoutingKey.AUTH_VERIFY.getKey());
    }

    @Bean
    public Binding passwordBinding(TopicExchange userExchange, Queue userPasswordQueue) {
        return BindingBuilder.bind(userPasswordQueue).to(userExchange).with(RoutingKey.USER_PASSWORD.getKey());
    }

    @Bean
    public Binding userBuyerBinding(TopicExchange userExchange, Queue userBuyerQueue) {
        return BindingBuilder.bind(userBuyerQueue).to(userExchange).with(RoutingKey.USER_BUYER.getKey());
    }

    @Bean
    public Binding userSellerSellingBinding(TopicExchange userExchange, Queue userSellerSellingQueue) {
        return BindingBuilder.bind(userSellerSellingQueue).to(userExchange).with(RoutingKey.USER_SELLER_SELLING.getKey());
    }

    @Bean
    public Binding userSellerResultBinding(TopicExchange userExchange, Queue userSellerResultQueue) {
        return BindingBuilder.bind(userSellerResultQueue).to(userExchange).with(RoutingKey.USER_SELLER_RESULT.getKey());
    }

    @Bean
    public Binding userNoticeBinding(TopicExchange userExchange, Queue userNoticeQueue) {
        return BindingBuilder.bind(userNoticeQueue).to(userExchange).with(RoutingKey.USER_NOTICE.getKey());
    }

    @Bean
    public Binding userAminInquiry(TopicExchange userExchange, Queue userAdminInquiryQueue) {
        return BindingBuilder.bind(userAdminInquiryQueue).to(userExchange).with(RoutingKey.USER_ADMIN_INQUIRY.getKey());
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
