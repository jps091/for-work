package project.forwork.api.domain.salepost.service.port;

import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salepost.model.SalePost;

import java.util.Optional;

public interface SalePostRepository {

    SalePost save(SalePost salePost);

    SalePost getByIdWithThrow(Long salePostId);

    Optional<SalePost> findById(Long salePostId);

    SalePost getSellingPostWithThrow(Long resumeId);

    SalePost getByResumeWithThrow(Resume resume);
}
