package project.forwork.api.domain.salepost.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.domain.salepost.model.SalePost;
import project.forwork.api.domain.salepost.service.port.SalePostRepository;
@Repository
@RequiredArgsConstructor
public class SalePostRepositoryImpl implements SalePostRepository {

    private final SalePostJpaRepository salePostJpaRepository;
    @Override
    public SalePost save(SalePost salePost) {
        return salePostJpaRepository.save(SalePostEntity.from(salePost)).toModel();
    }
}
