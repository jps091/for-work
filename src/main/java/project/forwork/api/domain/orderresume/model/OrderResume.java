package project.forwork.api.domain.orderresume.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import project.forwork.api.common.error.OrderResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@Slf4j
@EqualsAndHashCode(of = "id")
public class OrderResume {
    private final Long id;
    private final Long orderId;
    private final Long resumeId;
    private final BigDecimal price;
    private final OrderResumeStatus status;
    private final LocalDateTime sentAt;
    private final LocalDateTime canceledAt;

    public static OrderResume create(Long orderId, Long resumeId, BigDecimal resumePrice){
        return OrderResume.builder()
                .orderId(orderId)
                .resumeId(resumeId)
                .price(resumePrice)
                .status(OrderResumeStatus.PAID)
                .build();
    }

    public OrderResume updateStatusCancel(ClockHolder clockHolder){
        if(!OrderResumeStatus.PAID.equals(status)){
            throw new ApiException(OrderResumeErrorCode.CANCEL_FAIL);
        }

        return OrderResume.builder()
                .id(id)
                .orderId(orderId)
                .resumeId(resumeId)
                .status(OrderResumeStatus.CANCEL)
                .canceledAt(clockHolder.now())
                .build();
    }

    public OrderResume updateStatusConfirm(){
        return OrderResume.builder()
                .id(id)
                .orderId(orderId)
                .resumeId(resumeId)
                .status(OrderResumeStatus.CONFIRM)
                .build();
    }
}
