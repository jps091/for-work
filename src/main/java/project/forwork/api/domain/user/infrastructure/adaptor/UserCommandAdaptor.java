package project.forwork.api.domain.user.infrastructure.adaptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.domain.cart.infrastructure.CartEntity;
import project.forwork.api.domain.cart.infrastructure.CartJpaRepository;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.user.infrastructure.UserEntity;
import project.forwork.api.domain.user.infrastructure.UserJpaRepository;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserCommandPort;

@Repository
@RequiredArgsConstructor
public class UserCommandAdaptor implements UserCommandPort {

    private final UserJpaRepository userJpaRepository;
    private final CartJpaRepository cartJpaRepository;

    @Override
    public User register(User user) {
        User savedUser = userJpaRepository.save(UserEntity.from(user)).toModel();
        Cart cart = Cart.create(savedUser.getId());
        cartJpaRepository.save(CartEntity.from(cart));
        return savedUser;
    }

    @Override
    public User update(User user) {
        return userJpaRepository.save(UserEntity.from(user)).toModel();
    }

    @Override
    public void delete(User user) {
        User deletedUser = userJpaRepository.save(UserEntity.from(user)).toModel();
        cartJpaRepository.deleteByUserId(deletedUser.getId());
    }
}
