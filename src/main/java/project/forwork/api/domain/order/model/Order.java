package project.forwork.api.domain.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import project.forwork.api.common.error.OrderErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.user.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
@Builder
@Slf4j
public class Order {
    private final Long id;
    private final User user;
    private final String requestId;
    private final BigDecimal totalAmount;
    private final OrderStatus status;
    private final LocalDateTime paidAt;

    public static Order create(User user, String requestId, BigDecimal totalAmount, ClockHolder clockHolder){
        return Order.builder()
                .user(user)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(OrderStatus.PAID)
                .paidAt(clockHolder.now())
                .build();
    }

    public Order updateStatus(OrderStatus status){
        return Order.builder()
                .id(id)
                .user(user)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(status)
                .paidAt(paidAt)
                .build();
    }

    public Order confirmOrderNow(Long userId, OrderStatus status){
        if(!Objects.equals(user.getId(), userId)){
            throw new ApiException(OrderErrorCode.ORDER_NOT_PERMISSION, userId);
        }

        return Order.builder()
                .id(id)
                .user(user)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(status)
                .paidAt(paidAt)
                .build();
    }
    public Order cancelOrder(Long userId){
        if(!Objects.equals(user.getId(), userId)){
            throw new ApiException(OrderErrorCode.ORDER_NOT_PERMISSION, userId);
        }

        if(status.equals(OrderStatus.CONFIRM)){
            throw new ApiException(OrderErrorCode.RESUME_ALREADY_CONFIRM);
        }

        return Order.builder()
                .id(id)
                .user(user)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(OrderStatus.CANCEL)
                .paidAt(paidAt)
                .build();
    }

    public Order cancelPartialOrder(Long userId, List<OrderResume> orderResumes){
        if(!Objects.equals(user.getId(), userId)){
            throw new ApiException(OrderErrorCode.ORDER_NOT_PERMISSION, userId);
        }

        if(status.equals(OrderStatus.CONFIRM)){
            throw new ApiException(OrderErrorCode.RESUME_ALREADY_CONFIRM);
        }

        BigDecimal canceledPrice = orderResumes.stream()
                .map(OrderResume::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderStatus updateStatus = OrderStatus.PARTIAL_CANCEL;
        BigDecimal resultAmount = totalAmount.subtract(canceledPrice);
        if(resultAmount.compareTo(BigDecimal.ZERO) == 0){
            updateStatus = OrderStatus.CANCEL;
        }

        return Order.builder()
                .id(id)
                .user(user)
                .requestId(requestId)
                .totalAmount(resultAmount)
                .status(updateStatus)
                .paidAt(paidAt)
                .build();
    }

    public String getBuyerEmail(){
        return user.getEmail();
    }
}