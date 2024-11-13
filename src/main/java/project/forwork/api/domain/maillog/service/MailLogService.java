package project.forwork.api.domain.maillog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.domain.maillog.model.MailLog;
import project.forwork.api.domain.maillog.service.port.MailLogRepository;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.port.OrderRepository;
import project.forwork.api.domain.orderresume.controller.model.OrderResumePurchaseInfo;

@Service
@RequiredArgsConstructor
public class MailLogService {

    private final MailLogRepository mailLogRepository;
    private final OrderRepository orderRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerFailLog(OrderResumePurchaseInfo body, Exception e){
        Order order = orderRepository.getByIdWithThrow(body.getOrderId());
        MailLog mailLog = MailLog.create(body.getBuyerEmail(), order.getRequestId(), body.getResumeId(), e);
        mailLogRepository.save(mailLog);
    }
}
