package project.forwork.api.mock;

import project.forwork.api.common.error.CartErrorCode;
import project.forwork.api.common.error.CartResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.cart.service.port.CartRepository;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.cartresume.service.port.CartResumeRepository;
import project.forwork.api.domain.resume.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeCartResumeRepository implements CartResumeRepository {

    private final AtomicLong id = new AtomicLong(0);
    private final List<CartResume> data = new ArrayList<>();


    @Override
    public CartResume save(CartResume cartResume) {
        if(cartResume.getId() == null || cartResume.getId() == 0){
            CartResume newCartResume = CartResume.builder()
                    .id(id.incrementAndGet())
                    .cart(cartResume.getCart())
                    .resume(cartResume.getResume())
                    .build();
            data.add(newCartResume);
            return newCartResume;
        }else{
            data.removeIf(c -> Objects.equals(c.getId(), cartResume.getId()));
            data.add(cartResume);
            return cartResume;
        }
    }

    @Override
    public void deleteAll(List<CartResume> cartResumeList) {
        cartResumeList.forEach(cartResume ->
                data.removeIf(c -> Objects.equals(c.getId(), cartResume.getId()))
        );
    }

    @Override
    public void deleteAllInCart(Long userId) {
        data.forEach(cartResume ->
                data.removeIf(c -> Objects.equals(c.getCart().getUser().getId(), userId))
        );
    }

    @Override
    public Optional<CartResume> findById(Long cartResumeId) {
        return data.stream().filter(c -> Objects.equals(c.getId(), cartResumeId)).findAny();
    }

    @Override
    public boolean existsByCartAndResume(Cart cart, Resume resume) {
        return data.stream()
                    .anyMatch(cr -> Objects.equals(cr.getCart().getId(), cart.getId()) &&
                        Objects.equals(cr.getResume().getId(), resume.getId()));
    }

    @Override
    public CartResume getByIdWithThrow(Long cartResumeId) {
        return findById(cartResumeId).orElseThrow(() -> new ApiException(CartResumeErrorCode.CART_RESUME_NOT_FOUND, cartResumeId));
    }


    @Override
    public List<CartResume> findByUserAndSelected(Long userId, List<Long> cartResumeIds) {
        return data.stream()
                .filter(cartResume -> cartResumeIds.contains(cartResume.getId()) &&
                        Objects.equals(cartResume.getCart().getUser().getId(), userId))
                .toList();
    }

    @Override
    public List<CartResume> findAllInCart(Long userId) {
        return data.stream()
                .filter(cartResume -> Objects.equals(cartResume.getCart().getUser().getId(), userId))
                .toList();
    }

//    @Override
//    public List<CartResume> findByIds(List<Long> resumeIds) {
//        return data.stream()
//                .filter(cartResume -> resumeIds.contains(cartResume.getResume().getId()))
//                .toList();
//    }

    @Override
    public void deleteAllByResumeId(Long resumeId) {
        data.removeIf(cr -> Objects.equals(cr.getResume().getId(), resumeId));
    }

    @Override
    public void deleteByIds(Long cartId, List<Long> cartResumeIds) {
        data.removeIf(cr ->
                Objects.equals(cr.getCart().getId(), cartId) && cartResumeIds.contains(cr.getResume().getId())
        );
    }

    @Override
    public List<CartResume> findByConfirmedResumes(Long userId, List<Long> resumeIds) {
        return data.stream()
                .filter(cartResume -> resumeIds.contains(cartResume.getResume().getId()) &&
                        Objects.equals(cartResume.getCart().getUser().getId(), userId))
                .toList();
    }
}