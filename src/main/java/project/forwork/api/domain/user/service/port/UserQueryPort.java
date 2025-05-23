package project.forwork.api.domain.user.service.port;

import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.user.model.User;

import java.util.Optional;

public interface UserQueryPort {
    User getByIdWithThrow(Long id);
    Optional<User> findByEmail(String email);
    Cart getCart(Long id);
    boolean existsByEmail(String email);
}
