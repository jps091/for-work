package project.forwork.api.domain.cart.service.port;

import project.forwork.api.domain.cart.model.Cart;

public interface CartRepository {
    Cart save(Cart cart);

    Cart getByUserIdWithThrow(Long userId);
}
