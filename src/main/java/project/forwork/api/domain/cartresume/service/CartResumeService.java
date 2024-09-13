package project.forwork.api.domain.cartresume.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.CartResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.cart.service.port.CartRepository;
import project.forwork.api.domain.cartresume.controller.model.CartResumeResponse;
import project.forwork.api.domain.cartresume.controller.model.CartSummary;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.cartresume.service.port.CartResumeRepository;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
public class CartResumeService {

    private final CartResumeRepository cartResumeRepository;
    private final CartRepository cartRepository;
    private final ResumeRepository resumeRepository;

    public CartResume register(CurrentUser currentUser, Long resumeId){
        Cart cart = cartRepository.getByUserIdWithThrow(currentUser.getId());
        Resume resume = resumeRepository.getByIdWithThrow(resumeId);

        if(cartResumeRepository.existsByCartAndResume(cart, resume)){
            throw new ApiException(CartResumeErrorCode.RESUME_EXISTS_CART);
        }

        CartResume cartResume = CartResume.create(cart, resume);
        cartResume = cartResumeRepository.save(cartResume);

        return cartResume;
    }

    public void deleteBySelected(CurrentUser currentUser, List<Long> cartResumeIds){
        List<CartResume> cartResumeList = cartResumeRepository.findByUserAndSelected(currentUser.getId(), cartResumeIds);
        cartResumeRepository.delete(cartResumeList);
    }

    @Transactional(readOnly = true)
    public CartSummary calculateSummary(List<Long> cartResumeIds){
        List<CartResume> cartResumeList = cartResumeRepository.findBySelected(cartResumeIds);

        BigDecimal totalPrice = cartResumeList.stream()
                .map(CartResume::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //TODO 불변객체 cartResumeList.forEach(cartResume -> totalPrice.add(cartResume.getPrice()));

        int totalQuantity = cartResumeList.size();

        return new CartSummary(totalPrice, totalQuantity);
    }

    @Transactional(readOnly = true)
    public List<CartResumeResponse> findAll(CurrentUser currentUser){
        return cartResumeRepository.findAllInCart(currentUser.getId()).stream()
                .map(CartResumeResponse::from)
                .toList();
    }
}
