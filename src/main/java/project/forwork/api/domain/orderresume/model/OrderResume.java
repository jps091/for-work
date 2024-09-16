package project.forwork.api.domain.orderresume.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.resume.model.Resume;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
@Slf4j
public class OrderResume {
    private final Long id;
    private final Order order;
    private final Resume resume;
    private final OrderResumeStatus status;

    public static OrderResume create(Order order, Resume resume){
        return OrderResume.builder()
                .order(order)
                .resume(resume)
                .status(OrderResumeStatus.ORDER)
                .build();
    }

    public OrderResume changeStatus(OrderResumeStatus status){
        return OrderResume.builder()
                .id(id)
                .order(order)
                .resume(resume)
                .status(status)
                .build();
    }

    public BigDecimal getPrice(){
        return resume.getPrice();
    }
}
