package project.forwork.api.domain.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.OrderErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.infrastructure.model.ResumeDto;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.model.OrderResumes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@Slf4j
public class Order {
    private final Long id;
    private final Long userId;
    private final String requestId;
    private final BigDecimal totalAmount;
    private final OrderStatus status;
    private final LocalDateTime paidAt;
    private final OrderResumes orderResumes;

    public static Order create(Long userId, String requestId, BigDecimal totalAmount, ClockHolder clockHolder){
        return Order.builder()
                .userId(userId)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(OrderStatus.PAID)
                .paidAt(clockHolder.now())
                .build();
    }

    public Order updateStatus(OrderStatus status){
        return Order.builder()
                .id(id)
                .userId(userId)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(status)
                .paidAt(paidAt)
                .orderResumes(orderResumes)
                .build();
    }

    public Order addOrderResumes(List<ResumeDto> resumeDtos){
        if(status == OrderStatus.CANCEL){
            throw new ApiException(OrderErrorCode.ORDER_ALREADY_CANCEL);
        }

        OrderResumes updated = OrderResumes.of(new ArrayList<>());
        for (ResumeDto dto : resumeDtos) {
            OrderResume orderResume = OrderResume.create(id, dto.getResumeId(), dto.getResumePrice());
            updated = updated.add(orderResume);
        }

        return Order.builder()
                .id(id)
                .userId(userId)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(status)
                .paidAt(paidAt)
                .orderResumes(updated)
                .build();
    }

    public Order confirmOrderResumes(List<Long> orderResumeIds){
        OrderResumes updatedOrderResumes = orderResumes.confirmIfPaidAndContained(orderResumeIds);

        OrderStatus newStatus = status;
        if(orderResumes.isAllWithStatus(OrderResumeStatus.CONFIRM)){
            newStatus = OrderStatus.CONFIRM;
        }

        return Order.builder()
                .id(id)
                .userId(userId)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(newStatus)
                .paidAt(paidAt)
                .orderResumes(updatedOrderResumes)
                .build();
    }

    public Order confirmPaidOrderResumes() {
        OrderResumes updated = orderResumes.confirmPaidOrderResumes();

        return Order.builder()
                .id(id)
                .userId(userId)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(status)
                .paidAt(paidAt)
                .orderResumes(updated)
                .build();
    }

    public Order cancelOrderWithThrow(ClockHolder clockHolder){
        if(OrderStatus.CONFIRM.equals(status)){
            throw new ApiException(OrderErrorCode.RESUME_ALREADY_CONFIRM);
        }

        OrderResumes canceledOrderResumes = orderResumes.updateStatusCancel(clockHolder);

        return Order.builder()
                .id(id)
                .userId(userId)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(OrderStatus.CANCEL)
                .paidAt(paidAt)
                .orderResumes(canceledOrderResumes)
                .build();
    }

    public Order cancelPartialOrder(Long userId, OrderResumes orderResumes, ClockHolder clockHolder){
        if(OrderStatus.CONFIRM.equals(status)){
            throw new ApiException(OrderErrorCode.RESUME_ALREADY_CONFIRM);
        }

        BigDecimal resultAmount = calculateCancelAmount(orderResumes);
        OrderStatus newStatus = getCancelStatus();
        OrderResumes canceledOrderResumes = orderResumes.cancelIfContainedByIds(orderResumes.getOrderResumeIds(), clockHolder);

        return Order.builder()
                .id(id)
                .userId(userId)
                .requestId(requestId)
                .totalAmount(resultAmount)
                .status(newStatus)
                .paidAt(paidAt)
                .orderResumes(canceledOrderResumes)
                .build();
    }

    private OrderStatus getCancelStatus() {
        OrderStatus newStatus = OrderStatus.PARTIAL_CANCEL;
        if(status.equals(OrderStatus.PARTIAL_WAIT)){
            newStatus = OrderStatus.PARTIAL_WAIT;
        }
        return newStatus;
    }

    private BigDecimal calculateCancelAmount(OrderResumes orderResumes) {
        BigDecimal canceledPrice = orderResumes.calculateAmount();
        return totalAmount.subtract(canceledPrice);
    }

    public boolean isAllCancel(BigDecimal cancelAmount){
        return cancelAmount.compareTo(totalAmount) == 0;
    }

    public void validBuyer(CurrentUser currentUser){
        if(!this.userId.equals(currentUser.getId())){
            throw new ApiException(OrderErrorCode.ORDER_NOT_PERMISSION);
        }
    }
}