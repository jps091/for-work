package project.forwork.api.domain.order.model;

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
public class Order {
    private final Long id;
    private final User user;
    private final BigDecimal totalPrice;
    private final OrderStatus status;
    private final LocalDateTime orderedAt;
    private final LocalDateTime canceledAt;
    private final LocalDateTime confirmedAt;

    @Builder
    public Order(Long id, User user, BigDecimal totalPrice, OrderStatus status, LocalDateTime orderedAt, LocalDateTime canceledAt, LocalDateTime confirmedAt) {
        this.id = id;
        this.user = user;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderedAt = orderedAt;
        this.canceledAt = canceledAt;
        this.confirmedAt = confirmedAt;
    }

    public static Order create(User user, Resume resume, ClockHolder clockHolder){
        return Order.builder()
                .user(user)
                .totalPrice(resume.getPrice())
                .status(OrderStatus.ORDER)
                .orderedAt(clockHolder.now())
                .build();
    }

    public static Order create(User user, List<CartResume> cartResumes, ClockHolder clockHolder){
        BigDecimal totalPrice = cartResumes.stream()
                .map(CartResume::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Order.builder()
                .user(user)
                .totalPrice(totalPrice)
                .status(OrderStatus.ORDER)
                .orderedAt(clockHolder.now())
                .build();
    }

    public Order markAsWaiting(OrderStatus status){
        return Order.builder()
                .id(id)
                .user(user)
                .totalPrice(totalPrice)
                .status(status)
                .orderedAt(orderedAt)
                .build();
    }

    public Order markAsConfirm(OrderStatus status, ClockHolder clockHolder){
        return Order.builder()
                .id(id)
                .user(user)
                .totalPrice(totalPrice)
                .status(status)
                .orderedAt(orderedAt)
                .confirmedAt(clockHolder.now())
                .build();
    }

    public Order confirmOrderNow(Long userId, ClockHolder clockHolder){
        if(!Objects.equals(user.getId(), userId)){
            throw new ApiException(OrderErrorCode.ORDER_NOT_PERMISSION, userId);
        }

        return Order.builder()
                .id(id)
                .user(user)
                .totalPrice(totalPrice)
                .status(OrderStatus.CONFIRM)
                .orderedAt(orderedAt)
                .confirmedAt(clockHolder.now())
                .build();
    }

    public Order cancelOrder(Long userId, ClockHolder clockHolder){
        if(!Objects.equals(user.getId(), userId)){
            throw new ApiException(OrderErrorCode.ORDER_NOT_PERMISSION, userId);
        }

        return Order.builder()
                .id(id)
                .user(user)
                .totalPrice(totalPrice)
                .status(OrderStatus.CANCEL)
                .orderedAt(orderedAt)
                .canceledAt(clockHolder.now())
                .build();
    }

    public Order cancelPartialOrder(Long userId, List<OrderResume> orderResumes, ClockHolder clockHolder){
        if(!Objects.equals(user.getId(), userId)){
            throw new ApiException(OrderErrorCode.ORDER_NOT_PERMISSION, userId);
        }

        BigDecimal canceledPrice = orderResumes.stream()
                .map(OrderResume::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Order.builder()
                .id(id)
                .user(user)
                .totalPrice(totalPrice.subtract(canceledPrice))
                .status(OrderStatus.PARTIAL_CANCEL)
                .orderedAt(orderedAt)
                .canceledAt(clockHolder.now())
                .build();
    }
}
