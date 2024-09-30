package project.forwork.api.domain.salespost.service.port;


import project.forwork.api.domain.salespost.controller.model.SalesPostDetailResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostFilterCond;
import project.forwork.api.domain.salespost.controller.model.SalesPostResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostSellerResponse;



import java.util.List;

public interface SalesPostRepositoryCustom {
    SalesPostDetailResponse getDetailSalesPost(Long salesPostId);

    List<SalesPostSellerResponse> findBySeller(Long sellerId);

    List<SalesPostResponse> findFirstPage(SalesPostFilterCond cond, int limit);

    List<SalesPostResponse> findLastPage(SalesPostFilterCond cond, int limit);

    List<SalesPostResponse> findNextPage(SalesPostFilterCond cond, Long lastId, int limit);

    List<SalesPostResponse> findPreviousPage(SalesPostFilterCond cond, Long lastId, int limit);
}
