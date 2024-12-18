package project.forwork.api.domain.cartresume.service.port;

import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.resume.model.Resume;

import java.util.List;
import java.util.Optional;

public interface CartResumeRepository {

    CartResume save(CartResume cartResume);
    void deleteAll(List<CartResume> cartResumeList);
    void deleteByIds(Long cartId, List<Long> cartResumeIds);
    List<CartResume> findByConfirmedResumes(Long userId, List<Long> resumeIds);
    void deleteAllInCart(Long userId);
    Optional<CartResume> findById(Long cartResumeId);
    boolean existsByCartAndResume(Cart cart, Resume resume);
    CartResume getByIdWithThrow(Long cartResumeId);
    //List<CartResume> findByIds(List<Long> cartResumeIds);
    List<CartResume> findByUserAndSelected(Long userId, List<Long> cartResumeIds);
    List<CartResume> findAllInCart(Long userId);
    void deleteAllByResumeId(Long resumeId);
}
