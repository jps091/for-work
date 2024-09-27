package project.forwork.api.domain.salespost.service.port;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.salespost.controller.model.SalesPostDetailResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostSellerResponse;
import project.forwork.api.domain.salespost.infrastructure.SalesPostSearchCond;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType;

import java.math.BigDecimal;
import java.util.List;

public interface SalesPostRepositoryCustom {
    Page<SalesPostResponse> searchByCondition(SalesPostSearchCond cond, Pageable pageable, SalesPostSortType sortType);
    SalesPostDetailResponse getSellingPostWithThrow(Long salesPostId);
    List<SalesPostSellerResponse>findBySeller(Long sellerId);
    List<SalesPostResponse> findFirstPage(SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
                                          FieldType field, LevelType level, int limit);
    List<SalesPostResponse> findLastPage(SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
                                         FieldType field, LevelType level, int limit);
    List<SalesPostResponse> findNextPage(SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
                                         FieldType field, LevelType level,
                                         Long lastId, int limit);
    List<SalesPostResponse> findPreviousPage(SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
                                             FieldType field, LevelType level,
                                             Long lastId, int limit);
}
