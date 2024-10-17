package project.forwork.api.domain.orderresume.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import project.forwork.api.common.error.OrderResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.resume.model.Resume;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@Slf4j
public class OrderResume {
    private final Long id;
    private final Order order;
    private final Resume resume;
    private final OrderResumeStatus status;
    private final LocalDateTime sentAt;
    private final LocalDateTime canceledAt;

    public static OrderResume create(Order order, Resume resume){
        return OrderResume.builder()
                .order(order)
                .resume(resume)
                .status(OrderResumeStatus.ORDERED)
                .build();
    }
    public OrderResume updateStatusSend(ClockHolder clockHolder){
        return OrderResume.builder()
                .id(id)
                .order(order)
                .resume(resume)
                .status(OrderResumeStatus.SENT)
                .sentAt(clockHolder.now())
                .build();
    }

    public OrderResume updateStatusCancel(ClockHolder clockHolder){
        return OrderResume.builder()
                .id(id)
                .order(order)
                .resume(resume)
                .status(OrderResumeStatus.CANCEL)
                .canceledAt(clockHolder.now())
                .build();
    }

    public OrderResume updateStatusConfirm(){
        return OrderResume.builder()
                .id(id)
                .order(order)
                .resume(resume)
                .status(OrderResumeStatus.CONFIRM)
                .build();
    }

    public BigDecimal getPrice(){
        return resume.getPrice();
    }

    public Long getResumeId(){
        return resume.getId();
    }
}
