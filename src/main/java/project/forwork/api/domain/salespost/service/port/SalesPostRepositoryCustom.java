package project.forwork.api.domain.salespost.service.port;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.salespost.controller.model.SalesPostDetailResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostFilterCond;
import project.forwork.api.domain.salespost.controller.model.SalesPostResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostSellerResponse;
import project.forwork.api.domain.salespost.infrastructure.SalesPostSearchCond;
import project.forwork.api.domain.salespost.infrastructure.enums.FieldCond;
import project.forwork.api.domain.salespost.infrastructure.enums.LevelCond;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType;

import java.math.BigDecimal;
import java.util.List;

public interface SalesPostRepositoryCustom {
    SalesPostDetailResponse getDetailSalesPost(Long salesPostId);

    List<SalesPostSellerResponse> findBySeller(Long sellerId);

    List<SalesPostResponse> findFirstPage(SalesPostFilterCond cond, int limit);

    List<SalesPostResponse> findLastPage(SalesPostFilterCond cond, int limit);

    List<SalesPostResponse> findNextPage(SalesPostFilterCond cond, Long lastId, int limit);

    List<SalesPostResponse> findPreviousPage(SalesPostFilterCond cond, Long lastId, int limit);
}
