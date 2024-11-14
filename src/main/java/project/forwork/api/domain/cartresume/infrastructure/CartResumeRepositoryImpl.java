package project.forwork.api.domain.cartresume.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.CartResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.cart.infrastructure.CartEntity;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.cartresume.service.port.CartResumeRepository;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;
import project.forwork.api.domain.resume.model.Resume;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartResumeRepositoryImpl implements CartResumeRepository {

    private final CartResumeJpaRepository cartResumeJpaRepository;

    @Override
    public CartResume save(CartResume cartResume) {
        return cartResumeJpaRepository.save(CartResumeEntity.from(cartResume)).toModel();
    }

    @Override
    public void deleteAll(List<CartResume> cartResumes) {
        List<CartResumeEntity> cartResumeEntities = cartResumes.stream().map(CartResumeEntity::from).toList();
        cartResumeJpaRepository.deleteAll(cartResumeEntities);
    }

    @Override
    public void deleteByIds(Long cartId, List<Long> cartResumeIds) {
        cartResumeJpaRepository.deleteByIds(cartId, cartResumeIds);
    }

    @Override
    public void deleteAllInCart(Long userId) {
        cartResumeJpaRepository.deleteAllInCart(userId);
    }

    @Override
    public Optional<CartResume> findById(Long cartResumeId) {
        return cartResumeJpaRepository.findById(cartResumeId).map(CartResumeEntity::toModel);
    }

    @Override
    public boolean existsByCartAndResume(Cart cart, Resume resume) {
        return cartResumeJpaRepository.existsByCartEntityAndResumeEntity(CartEntity.from(cart), ResumeEntity.from(resume));
    }

    @Override
    public CartResume getByIdWithThrow(Long cartResumeId) {
        return findById(cartResumeId)
                .orElseThrow(() -> new ApiException(CartResumeErrorCode.CART_RESUME_NOT_FOUND, cartResumeId));
    }

    @Override
    public List<CartResume> findByIds(List<Long> cartResumeIds) {
        return cartResumeJpaRepository.findByConfirmedResumes(cartResumeIds).stream()
                .map(CartResumeEntity::toModel)
                .toList();
    }

    @Override
    public List<CartResume> findByUserAndSelected(Long userId, List<Long> cartResumeIds) {
        return cartResumeJpaRepository.findByUserAndSelected(userId, cartResumeIds).stream()
                .map(CartResumeEntity::toModel)
                .toList();
    }

    @Override
    public List<CartResume> findAllInCart(Long userId) {
        return cartResumeJpaRepository.findAllInCart(userId).stream()
                .map(CartResumeEntity::toModel)
                .toList();
    }

    @Override
    public void deleteAllByResumeId(Long resumeId){
        cartResumeJpaRepository.deleteAllByResumeEntity_Id(resumeId);
    }
}
