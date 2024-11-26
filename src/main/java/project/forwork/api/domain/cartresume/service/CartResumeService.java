package project.forwork.api.domain.cartresume.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.CartResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.cart.service.port.CartRepository;
import project.forwork.api.domain.cartresume.controller.model.CartResumeDetailResponse;
import project.forwork.api.domain.cartresume.controller.model.CartResumeResponse;
import project.forwork.api.domain.cartresume.controller.model.SelectCartResumeRequest;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.cartresume.service.port.CartResumeRepository;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Builder
@Transactional
@Slf4j
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

    public void deleteBySelected(CurrentUser currentUser, SelectCartResumeRequest body){
        List<CartResume> cartResumes = cartResumeRepository.findByUserAndSelected(currentUser.getId(), body.getCartResumeIds());
        cartResumeRepository.deleteAll(cartResumes);
    }

    public void deleteByPaidResumeIds(CurrentUser currentUser, List<Long> resumeIds){
        List<CartResume> cartResumes = cartResumeRepository.findByConfirmedResumes(currentUser.getId(), resumeIds);
        cartResumeRepository.deleteAll(cartResumes);
    }

    public void deleteAllInCart(CurrentUser currentUser){
        cartResumeRepository.deleteAllInCart(currentUser.getId());
    }

    @Transactional(readOnly = true)
    public CartResumeDetailResponse selectAll(CurrentUser currentUser){
        List<CartResumeResponse> cartResumeResponses = cartResumeRepository.findAllInCart(currentUser.getId()).stream()
                .map(CartResumeResponse::from)
                .toList();

        return createCartResumeDetailResponse(cartResumeResponses);
    }

    private static CartResumeDetailResponse createCartResumeDetailResponse(List<CartResumeResponse> cartResumeResponses) {
        BigDecimal totalPrice = cartResumeResponses.stream()
                .map(CartResumeResponse::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalQuantity = cartResumeResponses.size();

        return CartResumeDetailResponse.builder()
                .totalQuantity(totalQuantity)
                .totalPrice(totalPrice)
                .cartResumeResponses(cartResumeResponses)
                .build();
    }
}
