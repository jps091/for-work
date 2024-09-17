package project.forwork.api.domain.salepost.service.port;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salepost.controller.model.SalePostResponse;
import project.forwork.api.domain.salepost.infrastructure.SalePostSearchCond;
import project.forwork.api.domain.salepost.infrastructure.enums.SalePostSortType;
import project.forwork.api.domain.salepost.model.SalePost;

import java.util.Optional;

public interface SalePostRepository {

    SalePost save(SalePost salePost);

    SalePost getByIdWithThrow(Long salePostId);

    Optional<SalePost> findById(Long salePostId);

    SalePost getSellingPostWithThrow(Long resumeId);

    SalePost getByResumeWithThrow(Resume resume);
}
