package project.forwork.api.domain.cartresume.infrastructure;

import lombok.RequiredArgsConstructor;
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
    public void delete(List<CartResume> cartResumes) {

        List<CartResumeEntity> cartResumeEntities = cartResumes.stream().map(CartResumeEntity::from).toList();
        cartResumeJpaRepository.deleteAll(cartResumeEntities);
    }

    @Override
    public Optional<CartResume> findById(Long cartResumeId) {
        return cartResumeJpaRepository.findById(cartResumeId).map(CartResumeEntity::toModel);
    }

    @Override
    public boolean existsByCartAndResume(Cart cart, Resume resume) {
        return cartResumeJpaRepository.findByCartEntityAndResumeEntity(CartEntity.from(cart), ResumeEntity.from(resume));
    }

    @Override
    public CartResume getByIdWithThrow(Long cartResumeId) {
        return findById(cartResumeId)
                .orElseThrow(() -> new ApiException(CartResumeErrorCode.CART_RESUME_NOT_FOUND, cartResumeId));
    }

    @Override
    public List<CartResume> findBySelected(List<Long> cartResumeIds) {
        return cartResumeJpaRepository.findBySelected(cartResumeIds).stream()
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
}
