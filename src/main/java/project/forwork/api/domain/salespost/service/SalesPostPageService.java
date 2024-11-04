package project.forwork.api.domain.salespost.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.SalesPostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.infrastructure.enums.PageStep;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.controller.model.*;
import project.forwork.api.domain.salespost.infrastructure.enums.FieldCond;
import project.forwork.api.domain.salespost.infrastructure.enums.LevelCond;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salespost.infrastructure.model.SalesPostSearchDto;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.salespost.service.port.SalesPostRepository;
import project.forwork.api.domain.salespost.service.port.SalesPostRepositoryCustom;
import project.forwork.api.domain.thumbnailimage.service.ThumbnailImageService;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
public class SalesPostPageService {

    private final SalesPostRepository salesPostRepository;
    private final SalesPostRepositoryCustom salesPostRepositoryCustom;
    private final UserRepository userRepository;
    private final ThumbnailImageService thumbnailImageService;


    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "firstPageByCond", key = "'salespost:field:' + #cond.field + ':firstpage:sort:' + #cond.sortType",
            condition = "#cond.sortType == T(project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType).BEST_SELLING && " +
                    "#cond.level == T(project.forwork.api.domain.salespost.infrastructure.enums.LevelCond).UNSELECTED &&" +
                    "#cond.minPrice == null && #cond.maxPrice == null",
            cacheManager = "redisCacheManager")
    public SalesPostPage findFirstPage(SalesPostFilterCond cond, int limit){
        List<SalesPostSearchDto> results = salesPostRepositoryCustom.searchFirstPage(cond, limit);
        return createSalesPostPage(results);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findLastPage(SalesPostFilterCond cond, int limit){
        List<SalesPostSearchDto> results = salesPostRepositoryCustom.searchLastPage(cond, limit);
        return createReverseSalesPostPage(results);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findNextPage(SalesPostFilterCond cond, Long lastId, int limit){
        List<SalesPostSearchDto> results = salesPostRepositoryCustom.searchNextPage(cond, lastId, limit);
        return createSalesPostPage(results);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findPreviousPage(SalesPostFilterCond cond, Long lastId, int limit){
        List<SalesPostSearchDto> results = salesPostRepositoryCustom.searchPreviousPage(cond, lastId, limit);
        return createReverseSalesPostPage(results);
    }

    private SalesPostPage createSalesPostPage(List<SalesPostSearchDto> searchDtos) {
        validSearchDtoEmpty(searchDtos);

        List<SalesPostSearchResponse> results = createSearchResponseByDto(searchDtos);

        SalesPostSearchResponse lastRecord = results.get(results.size() - 1);
        return SalesPostPage.from(results, lastRecord);
    }

    private SalesPostPage createReverseSalesPostPage(List<SalesPostSearchDto> searchDtos) {
        validSearchDtoEmpty(searchDtos);

        List<SalesPostSearchResponse> results = createSearchResponseByDto(searchDtos);

        SalesPostSearchResponse firstRecord = results.get(0);
        return SalesPostPage.from(results, firstRecord);
    }

    private List<SalesPostSearchResponse> createSearchResponseByDto(List<SalesPostSearchDto> searchDtos) {
        return searchDtos.stream()
                .map(dto -> {
                    String title = createSalesPostTitle(dto);
                    String thumbnailUrl = thumbnailImageService.getThumbnailUrl(dto.getField());
                    return SalesPostSearchResponse.from(dto, title, thumbnailUrl);
                })
                .toList();
    }

    private void validSearchDtoEmpty(List<SalesPostSearchDto> searchDtos) {
        if(searchDtos.isEmpty()){
            throw new ApiException(SalesPostErrorCode.SALES_POST_NO_CONTENT);
        }
    }

    private String createSalesPostTitle(SalesPostSearchDto dto){
        return dto.getField().toString() + " " + dto.getLevel().toString() + " 이력서 #" + dto.getResumeId();
    }
}
