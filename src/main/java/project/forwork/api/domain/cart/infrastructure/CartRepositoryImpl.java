package project.forwork.api.domain.cart.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.CartErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.cart.service.port.CartRepository;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {

    private final  CartJpaRepository cartJpaRepository;

    @Override
    public Cart save(Cart cart) {
        return cartJpaRepository.save(CartEntity.from(cart)).toModel();
    }

    @Override
    public Cart getByUserIdWithThrow(Long userId) {
        return cartJpaRepository.findByUserEntity_Id(userId)
                .orElseThrow(() -> new ApiException(CartErrorCode.CART_NOT_FOUND, userId))
                .toModel();
    }

    @Override
    public void delete(Long userId) {
        cartJpaRepository.deleteByUserEntity_Id(userId);
    }
}
