package project.forwork.api.domain.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.cart.service.port.CartRepository;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public Cart register(CurrentUser currentUser){

        User user = userRepository.getByIdWithThrow(currentUser.getId());
        Cart cart = Cart.create(user);
        return cartRepository.save(cart);
    }
}
