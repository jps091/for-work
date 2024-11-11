package project.forwork.api.domain.salespost.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.error.SalesPostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.salespost.controller.model.*;
import project.forwork.api.domain.salespost.infrastructure.model.SalesPostSearchDto;
import project.forwork.api.domain.salespost.service.port.SalesPostRepository;
import project.forwork.api.domain.salespost.service.port.SalesPostRepositoryCustom;
import project.forwork.api.domain.thumbnailimage.service.ThumbnailImageService;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.List;
import java.util.Objects;

import static project.forwork.api.common.config.cache.redis.RedisCacheConfig.FIRST;


@Service
@Builder
@RequiredArgsConstructor
public class SalesPostPageService {

    private final SalesPostRepository salesPostRepository;
    private final SalesPostRepositoryCustom salesPostRepositoryCustom;
    private final UserRepository userRepository;
    private final ThumbnailImageService thumbnailImageService;


    @Transactional(readOnly = true)
    @Cacheable(cacheNames = FIRST, key = "'salespost:field:' + #cond.field + ':firstpage:sort:' + #cond.sortType",
            condition = "#cond.sortType == T(project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType).BEST_SELLING && " +
                    "#cond.level == T(project.forwork.api.domain.salespost.infrastructure.enums.LevelCond).UNSELECTED &&" +
                    "#cond.minPrice == null && #cond.maxPrice == null",
            cacheManager = "redisCacheManager")
    public SalesPostPage findFirstPage(SalesPostFilterCond cond, int limit){
        List<SalesPostSearchDto> results = salesPostRepositoryCustom.searchFirstPage(cond, limit);
        return createSearchResponseByDto(results, true, false);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findLastPage(SalesPostFilterCond cond, int limit){
        List<SalesPostSearchDto> results = salesPostRepositoryCustom.searchLastPage(cond, limit);
        return createSearchResponseByDto(results, false, true);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findNextPage(SalesPostFilterCond cond, Long lastId, int limit){
        List<SalesPostSearchDto> results = salesPostRepositoryCustom.searchNextPage(cond, lastId, limit + 1);
        boolean isLastPage = results.size() <= limit; // limit + 1과 비교하여 마지막 페이지 여부 판단
        if (!isLastPage) {
            results = results.subList(0, limit); // limit 개수만큼
        }

        return createSearchResponseByDto(results, false, isLastPage);
    }

    @Transactional(readOnly = true)
    public SalesPostPage findPreviousPage(SalesPostFilterCond cond, Long lastId, int limit){
        List<SalesPostSearchDto> results = salesPostRepositoryCustom.searchPreviousPage(cond, lastId, limit + 1);
        boolean isFirstPage = results.size() <= limit; // limit + 1과 비교하여 마지막 페이지 여부 판단
        if (!isFirstPage) {
            results = results.subList(1, limit + 1); // limit 개수만큼
        }

        return createSearchResponseByDto(results, isFirstPage, false);
    }

    private SalesPostPage createSearchResponseByDto(List<SalesPostSearchDto> searchDtos, boolean isFirstPage, boolean isLastPage) {
        validSearchDtoEmpty(searchDtos);
        List<SalesPostSearchResponse> result = searchDtos.stream()
                .map(dto -> {
                    String title = createSalesPostTitle(dto);
                    String thumbnailUrl = thumbnailImageService.getThumbnailUrl(dto.getField());
                    return SalesPostSearchResponse.from(dto, title, thumbnailUrl);
                })
                .toList();
        return SalesPostPage.from(result, isFirstPage, isLastPage);
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
