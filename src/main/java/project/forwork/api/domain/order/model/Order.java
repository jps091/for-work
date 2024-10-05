package project.forwork.api.domain.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.common.error.OrderErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.user.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
@Builder
public class Order {
    private final Long id;
    private final User user;
    private final String requestId;
    private final BigDecimal totalAmount;
    private final OrderStatus status;
    private final LocalDateTime orderedAt;
    private final LocalDateTime canceledAt;
    private final LocalDateTime confirmedAt;
    private final LocalDateTime sentAt;

    public static Order create(User user, Resume resume, String requestId, ClockHolder clockHolder){
        return Order.builder()
                .user(user)
                .requestId(requestId)
                .totalAmount(resume.getPrice())
                .status(OrderStatus.ORDERED)
                .orderedAt(clockHolder.now())
                .build();
    }

    public static Order create(User user, List<CartResume> cartResumes, String requestId, ClockHolder clockHolder){
        BigDecimal totalPrice = cartResumes.stream()
                .map(CartResume::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Order.builder()
                .user(user)
                .requestId(requestId)
                .totalAmount(totalPrice)
                .status(OrderStatus.ORDERED)
                .orderedAt(clockHolder.now())
                .build();
    }

    public Order updateConfirm(OrderStatus status, ClockHolder clockHolder){

        if(OrderStatus.CONFIRM.equals(status)){
            return Order.builder()
                    .id(id)
                    .user(user)
                    .requestId(requestId)
                    .totalAmount(totalAmount)
                    .status(status)
                    .orderedAt(orderedAt)
                    .confirmedAt(clockHolder.now())
                    .build();
        }

        if(OrderStatus.PAYMENT_FAILED.equals(status)){
            return Order.builder()
                    .id(id)
                    .user(user)
                    .requestId(requestId)
                    .totalAmount(totalAmount)
                    .status(status)
                    .orderedAt(orderedAt)
                    .build();
        }

        return Order.builder()
                .id(id)
                .user(user)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(status)
                .orderedAt(orderedAt)
                .build();
    }

    public Order updateStatus(OrderStatus status){
        return Order.builder()
                .id(id)
                .user(user)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(status)
                .orderedAt(orderedAt)
                .build();
    }

    public Order sendAuto(ClockHolder clockHolder){
        return Order.builder()
                .id(id)
                .user(user)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(status)
                .sentAt(clockHolder.now())
                .build();
    }

    public Order orderConfirmNow(Long userId, ClockHolder clockHolder){
        if(!Objects.equals(user.getId(), userId)){
            throw new ApiException(OrderErrorCode.ORDER_NOT_PERMISSION, userId);
        }

        if(status.equals(OrderStatus.SEND)){
            throw new ApiException(OrderErrorCode.RESUME_ALREADY_SEND);
        }

        return Order.builder()
                .id(id)
                .user(user)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(OrderStatus.SEND)
                .orderedAt(orderedAt)
                .sentAt(clockHolder.now())
                .build();
    }

    public Order cancelOrder(Long userId, ClockHolder clockHolder){
        if(!Objects.equals(user.getId(), userId)){
            throw new ApiException(OrderErrorCode.ORDER_NOT_PERMISSION, userId);
        }

        if(status.equals(OrderStatus.SEND)){
            throw new ApiException(OrderErrorCode.RESUME_ALREADY_SEND);
        }

        return Order.builder()
                .id(id)
                .user(user)
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(OrderStatus.CANCEL)
                .orderedAt(orderedAt)
                .canceledAt(clockHolder.now())
                .build();
    }

    public Order cancelPartialOrder(Long userId, List<OrderResume> orderResumes, ClockHolder clockHolder){
        if(!Objects.equals(user.getId(), userId)){
            throw new ApiException(OrderErrorCode.ORDER_NOT_PERMISSION, userId);
        }

        if(status.equals(OrderStatus.SEND)){
            throw new ApiException(OrderErrorCode.RESUME_ALREADY_SEND);
        }

        BigDecimal canceledPrice = orderResumes.stream()
                .map(OrderResume::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Order.builder()
                .id(id)
                .user(user)
                .requestId(requestId)
                .totalAmount(totalAmount.subtract(canceledPrice))
                .status(OrderStatus.PARTIAL_CANCEL)
                .orderedAt(orderedAt)
                .canceledAt(clockHolder.now())
                .build();
    }

    public String getBuyerEmail(){
        return user.getEmail();
    }

    public BigDecimal getPartialCancelAmount(List<OrderResume> orderResumes){
        return orderResumes.stream()
                .map(OrderResume::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}