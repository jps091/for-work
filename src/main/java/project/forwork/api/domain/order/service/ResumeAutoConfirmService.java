package project.forwork.api.domain.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;



@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeAutoConfirmService {

    private final OrderService orderService;

    @Scheduled(cron = "0 0,30 * * * *")
    public void markAsWaiting(){
        orderService.updatedOrderStatus(OrderStatus.PAID, OrderStatus.WAIT);
    }

    @Scheduled(cron = "0 0,30 * * * *")
    public void markPartialAsWaiting(){
        orderService.updatedOrderStatus(OrderStatus.PARTIAL_CANCEL, OrderStatus.PARTIAL_WAIT);
    }

    @Scheduled(cron = "0 0,30 * * * *")
    public void markAsConfirm(){
        orderService.updatedOrderStatus(OrderStatus.WAIT, OrderStatus.CONFIRM);
    }

    @Scheduled(cron = "0 0,30 * * * *")
    public void markPartialAsConfirm(){
        orderService.updatedOrderStatus(OrderStatus.PARTIAL_WAIT, OrderStatus.PARTIAL_CONFIRM);
    }
}
