package project.forwork.api.domain.salespost.service.port;


import project.forwork.api.domain.salespost.controller.model.SalesPostDetailResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostFilterCond;
import project.forwork.api.domain.salespost.infrastructure.model.SalesPostSearchDto;
import project.forwork.api.domain.salespost.controller.model.SalesPostSellerResponse;


import java.util.List;

public interface SalesPostRepositoryCustom {
    SalesPostDetailResponse getDetailSalesPost(Long resumeId);

    List<SalesPostSellerResponse> findBySeller(Long sellerId);

    List<SalesPostSearchDto> searchFirstPage(SalesPostFilterCond cond, int limit);

    List<SalesPostSearchDto> searchLastPage(SalesPostFilterCond cond, int limit);

    List<SalesPostSearchDto> searchNextPage(SalesPostFilterCond cond, Long lastId, int limit);

    List<SalesPostSearchDto> searchPreviousPage(SalesPostFilterCond cond, Long lastId, int limit);

}
