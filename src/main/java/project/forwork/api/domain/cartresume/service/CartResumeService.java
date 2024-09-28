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
import project.forwork.api.domain.cartresume.controller.model.CartResumeDetailResponse;
import project.forwork.api.domain.cartresume.controller.model.CartResumeResponse;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.cartresume.service.port.CartResumeRepository;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.salespost.service.port.SalesPostRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
public class CartResumeService {

    private final CartResumeRepository cartResumeRepository;
    private final CartRepository cartRepository;
    private final SalesPostRepository salesPostRepository;

    public CartResume register(CurrentUser currentUser, Long salesPostId){
        Cart cart = cartRepository.getByUserIdWithThrow(currentUser.getId());
        SalesPost salesPost = salesPostRepository.getByIdWithThrow(salesPostId);
        Resume resume = salesPost.getResume();

        if(cartResumeRepository.existsByCartAndResume(cart, resume)){
            throw new ApiException(CartResumeErrorCode.RESUME_EXISTS_CART);
        }

        CartResume cartResume = CartResume.create(cart, resume);
        cartResume = cartResumeRepository.save(cartResume);

        return cartResume;
    }

    public void deleteBySelected(CurrentUser currentUser, List<Long> cartResumeIds){
        List<CartResume> cartResumes = cartResumeRepository.findByUserAndSelected(currentUser.getId(), cartResumeIds);
        cartResumeRepository.delete(cartResumes);
    }

    public void deleteAllInCart(CurrentUser currentUser){
        cartResumeRepository.deleteAllInCart(currentUser.getId());
    }

    @Transactional(readOnly = true)
    public CartResumeDetailResponse selectCartResumes(List<Long> cartResumeIds){
        List<CartResumeResponse> cartResumeResponses = cartResumeRepository.findBySelected(cartResumeIds).stream()
                .map(CartResumeResponse::from)
                .toList();

        return createCartResumeDetailResponse(cartResumeResponses);
    }

    @Transactional(readOnly = true)
    public CartResumeDetailResponse selectAll(CurrentUser currentUser){
        List<CartResumeResponse> cartResumeResponses = cartResumeRepository.findAllInCart(currentUser.getId()).stream()
                .map(CartResumeResponse::from)
                .toList();

        return createCartResumeDetailResponse(cartResumeResponses);
    }

    private static CartResumeDetailResponse createCartResumeDetailResponse(List<CartResumeResponse> cartResumeResponses) {
        //TODO 불변객체 cartResumeList.forEach(cartResume -> totalPrice.add(cartResume.getPrice()));
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
