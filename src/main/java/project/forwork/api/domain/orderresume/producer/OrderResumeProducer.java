package project.forwork.api.domain.orderresume.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.infrastructure.Producer;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeMailMessage;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom;
import project.forwork.api.domain.resume.service.ResumeQuantityService;

import java.util.List;

import static project.forwork.api.common.config.rabbitmq.RabbitMqConfig.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderResumeProducer {

    private final Producer producer;
    private final OrderResumeRepositoryCustom orderResumeRepositoryCustom;

    @Transactional
    public void setupConfirmedResumesAndSendEmail(List<OrderResume> orderResumes) {
        List<OrderResumeMailMessage> messages = orderResumeRepositoryCustom.findAllPurchaseResume(orderResumes);
        messages.forEach(this::produceMailMessage);
    }

    public void produceMailMessage(OrderResumeMailMessage message) {
        producer.producer(MAIL_EXCHANGE, ROUTE_MAIL_KEY, message);
    }
}

