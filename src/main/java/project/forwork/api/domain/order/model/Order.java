package project.forwork.api.domain.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.OrderErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.infrastructure.model.ResumeDto;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @Singular("orderResume")
    private final List<OrderResume> orderResumes;

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

    public void addOrderResumes(List<ResumeDto> resumeDtos){
        if(status == OrderStatus.CANCEL){
            throw new ApiException(OrderErrorCode.ORDER_ALREADY_CANCEL);
        }

        resumeDtos.forEach(re -> orderResumes.add(
                OrderResume.create(id, re.getResumeId(), re.getResumePrice())
        ));
    }

    public Order confirmOrderResumes(List<Long> orderResumeIds){
        List<OrderResume> updatedOrderResumes = orderResumes.stream()
                .map(or -> or.getStatus() == OrderResumeStatus.PAID
                        && orderResumeIds.contains(or.getId())
                        ? or.updateStatusConfirm() : or)
                .toList();

        OrderStatus newStatus = status;
        boolean allConfirmed = updatedOrderResumes.stream()
                .allMatch(or -> or.getStatus() == OrderResumeStatus.CONFIRM);

        if(allConfirmed){
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

    public Order cancelOrderWithThrow(ClockHolder clockHolder){
        if(OrderStatus.CONFIRM.equals(status)){
            throw new ApiException(OrderErrorCode.RESUME_ALREADY_CONFIRM);
        }

        List<OrderResume> updatedOrderResumes = orderResumes.stream()
                .map(or -> or.updateStatusCancel(clockHolder))
                .toList();

        return Order.builder()
                .id(id)
                .userId(userId)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(OrderStatus.CANCEL)
                .paidAt(paidAt)
                .orderResumes(updatedOrderResumes)
                .build();
    }

    public Order cancelPartialOrder(Long userId, List<OrderResume> orderResumes, ClockHolder clockHolder){
        if(OrderStatus.CONFIRM.equals(status)){
            throw new ApiException(OrderErrorCode.RESUME_ALREADY_CONFIRM);
        }

        BigDecimal resultAmount = calculateCancelAmount(orderResumes);
        OrderStatus newStatus = getCancelStatus();

        List<OrderResume> updatedOrderResumes = orderResumes.stream()
                .map(or -> this.orderResumes.contains(or)
                        ? or.updateStatusCancel(clockHolder) : or)
                .toList();

        return Order.builder()
                .id(id)
                .userId(userId)
                .requestId(requestId)
                .totalAmount(resultAmount)
                .status(newStatus)
                .paidAt(paidAt)
                .orderResumes(updatedOrderResumes)
                .build();
    }

    private OrderStatus getCancelStatus() {
        OrderStatus newStatus = OrderStatus.PARTIAL_CANCEL;
        if(status.equals(OrderStatus.PARTIAL_WAIT)){
            newStatus = OrderStatus.PARTIAL_WAIT;
        }
        return newStatus;
    }

    private BigDecimal calculateCancelAmount(List<OrderResume> orderResumes) {
        BigDecimal canceledPrice = orderResumes.stream()
                .map(OrderResume::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalAmount.subtract(canceledPrice);
    }

//    public String getBuyerEmail(){
//        return user.getEmail();
//    } TODO

    public boolean isAllCancel(BigDecimal cancelAmount){
        return cancelAmount.compareTo(totalAmount) == 0;
    }

    public void validBuyer(CurrentUser currentUser){
        if(!this.userId.equals(currentUser.getId())){
            throw new ApiException(OrderErrorCode.ORDER_NOT_PERMISSION);
        }
    }
}