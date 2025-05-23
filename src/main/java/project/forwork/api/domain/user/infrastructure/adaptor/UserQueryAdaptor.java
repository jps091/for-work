package project.forwork.api.domain.user.infrastructure.adaptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.CartErrorCode;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.cart.infrastructure.CartEntity;
import project.forwork.api.domain.cart.infrastructure.CartJpaRepository;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.user.infrastructure.UserEntity;
import project.forwork.api.domain.user.infrastructure.UserJpaRepository;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserQueryPort;

import java.util.Optional;
@Repository
@RequiredArgsConstructor
public class UserQueryAdaptor implements UserQueryPort {

    private final UserJpaRepository userJpaRepository;
    private final CartJpaRepository cartJpaRepository;
    @Override
    public User getByIdWithThrow(Long id) {
        return userJpaRepository.findById(id)
                .map(UserEntity::toModel)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserEntity::toModel);
    }

    @Override
    public Cart getCart(Long id) {
        return cartJpaRepository.findByUserId(id)
                .map(CartEntity::toModel)
                .orElseThrow(() -> new ApiException(CartErrorCode.CART_NOT_FOUND, id));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
