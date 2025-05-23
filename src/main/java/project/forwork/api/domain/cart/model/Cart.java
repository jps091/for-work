package project.forwork.api.domain.cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.domain.cart.infrastructure.enums.CartStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.user.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class Cart {
    private final Long id;
    private final Long userId;
    private final CartStatus status;

    public static Cart create(Long userId){
        return Cart.builder()
                .userId(userId)
                .status(CartStatus.ACTIVE)
                .build();
    }
}
