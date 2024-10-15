package project.forwork.api.domain.salespost.infrastructure;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.forwork.api.domain.salespost.controller.model.*;
import project.forwork.api.domain.salespost.infrastructure.enums.FieldCond;
import project.forwork.api.domain.salespost.infrastructure.enums.LevelCond;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salespost.service.port.SalesPostRepositoryCustom;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static project.forwork.api.domain.resume.infrastructure.QResumeEntity.*;
import static project.forwork.api.domain.salespost.infrastructure.QSalesPostEntity.*;
import static project.forwork.api.domain.thumbnailimage.infrastructure.QThumbnailImageEntity.*;

@Repository
public class SalesPostRepositoryCustomImpl implements SalesPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public SalesPostRepositoryCustomImpl(EntityManager em) {
        this.queryFactory  = new JPAQueryFactory(em);
    }

    /***
     * 1. 기본적으로 최신 등록순으로 정렬
     * 2. 정렬 조건 : 최신 등록순, 조회순, 많이팔린순
     * 3. 검색 조건 : 가격(범위), 분야, 년차
     * 4. 페이징처리 :
     */

    public SalesPostDetailResponse getDetailSalesPost(Long salesPostId){
        return queryFactory
                .select(Projections.fields(SalesPostDetailResponse.class,
                        salesPostEntity.id.as("id"),
                        salesPostEntity.title.as("title"),
                        resumeEntity.salesQuantity.as("salesQuantity"),
                        thumbnailImageEntity.url.as("thumbnailImageUrl"),
                        resumeEntity.price.as("price"),
                        resumeEntity.descriptionImageUrl.as("descriptionImageUrl"),
                        resumeEntity.description.as("description"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        salesPostEntity.salesStatus.as("status"),
                        salesPostEntity.registeredAt.as("registeredAt")))
                .from(salesPostEntity)
                .join(salesPostEntity.resumeEntity, resumeEntity)
                .join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity)
                .where(
                        salesPostEntity.id.eq(salesPostId)
                )
                .fetchOne();
    }

    public List<SalesPostSellerResponse> findBySeller(Long sellerId){

        return queryFactory
                .select(Projections.fields(SalesPostSellerResponse.class,
                        salesPostEntity.id.as("id"),
                        salesPostEntity.title.as("title"),
                        resumeEntity.salesQuantity.as("salesQuantity"),
                        salesPostEntity.registeredAt.as("registeredAt"),
                        salesPostEntity.salesStatus.as("status")
                ))
                .from(salesPostEntity)
                .join(salesPostEntity.resumeEntity, resumeEntity)
                .where(resumeEntity.sellerEntity.id.eq(sellerId))
                .fetch();
    }


    public List<SalesPostResponse> findFirstPage(SalesPostFilterCond cond, int limit){
        OrderSpecifier<?>[] orderSpecifier = createOrderSpecifier(cond.getSortType());

        return queryFactory
                .select(Projections.fields(SalesPostResponse.class,
                        salesPostEntity.id.as("id"),
                        salesPostEntity.title.as("title"),
                        resumeEntity.price.as("price"),
                        thumbnailImageEntity.url.as("thumbnailImageUrl"),
                        resumeEntity.salesQuantity.as("salesQuantity"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        salesPostEntity.salesStatus.as("status"),
                        salesPostEntity.registeredAt.as("registeredAt")))
                .from(salesPostEntity)
                .join(salesPostEntity.resumeEntity, resumeEntity)
                .join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity)
                .where(
                        priceRangeCond(cond.getMinPrice(), cond.getMaxPrice()),
                        fieldEqual(cond.getField()),
                        levelEqual(cond.getLevel()),
                        salesPostEntity.salesStatus.eq(SalesStatus.SELLING)
                )
                .orderBy(orderSpecifier)
                .limit(limit).fetch();
    }

    public List<SalesPostResponse> findLastPage(SalesPostFilterCond cond, int limit){
        OrderSpecifier<?>[] orderSpecifier = createReversedOrderSpecifier(cond.getSortType());

        List<SalesPostResponse> results = queryFactory
                .select(Projections.fields(SalesPostResponse.class,
                        salesPostEntity.id.as("id"),
                        salesPostEntity.title.as("title"),
                        resumeEntity.price.as("price"),
                        thumbnailImageEntity.url.as("thumbnailImageUrl"),
                        resumeEntity.salesQuantity.as("salesQuantity"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        salesPostEntity.salesStatus.as("status"),
                        salesPostEntity.registeredAt.as("registeredAt")))
                .from(salesPostEntity)
                .join(salesPostEntity.resumeEntity, resumeEntity)
                .join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity)
                .where(
                        priceRangeCond(cond.getMinPrice(), cond.getMaxPrice()),
                        fieldEqual(cond.getField()),
                        levelEqual(cond.getLevel()),
                        salesPostEntity.salesStatus.eq(SalesStatus.SELLING)
                )
                .orderBy(orderSpecifier)
                .limit(limit).fetch();

        Collections.reverse(results);
        return results;
    }

    public List<SalesPostResponse> findNextPage(SalesPostFilterCond cond, Long lastId, int limit){
        OrderSpecifier<?>[] orderSpecifier = createOrderSpecifier(cond.getSortType());

        return queryFactory
                .select(Projections.fields(SalesPostResponse.class,
                        salesPostEntity.id.as("id"),
                        salesPostEntity.title.as("title"),
                        resumeEntity.price.as("price"),
                        thumbnailImageEntity.url.as("thumbnailImageUrl"),
                        resumeEntity.salesQuantity.as("salesQuantity"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        salesPostEntity.salesStatus.as("status"),
                        salesPostEntity.registeredAt.as("registeredAt")))
                .from(salesPostEntity)
                .join(salesPostEntity.resumeEntity, resumeEntity)
                .join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity)
                .where(priceRangeCond(cond.getMinPrice(), cond.getMaxPrice()),
                        fieldEqual(cond.getField()),
                        levelEqual(cond.getLevel()),
                        compareCondBySortType(cond.getSortType(), lastId),
                        (salesPostEntity.salesStatus.eq(SalesStatus.SELLING))
                )
                .orderBy(orderSpecifier)
                .limit(limit).fetch();
    }

    public List<SalesPostResponse> findPreviousPage(SalesPostFilterCond cond, Long lastId, int limit){
        OrderSpecifier<?>[] orderSpecifier = createReversedOrderSpecifier(cond.getSortType());

        List<SalesPostResponse> results = queryFactory
                .select(Projections.fields(SalesPostResponse.class,
                        salesPostEntity.id.as("id"),
                        salesPostEntity.title.as("title"),
                        resumeEntity.price.as("price"),
                        thumbnailImageEntity.url.as("thumbnailImageUrl"),
                        resumeEntity.salesQuantity.as("salesQuantity"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        salesPostEntity.salesStatus.as("status"),
                        salesPostEntity.registeredAt.as("registeredAt")))
                .from(salesPostEntity)
                .join(salesPostEntity.resumeEntity, resumeEntity)
                .join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity)
                .where(reverseCompareCondBySortType(cond.getSortType(), lastId),
                        (salesPostEntity.salesStatus.eq(SalesStatus.SELLING)),
                        priceRangeCond(cond.getMinPrice(), cond.getMaxPrice()),
                        fieldEqual(cond.getField()),
                        levelEqual(cond.getLevel())
                )
                .orderBy(orderSpecifier)
                .limit(limit).fetch();

        Collections.reverse(results);
        return results;
    }

    private SalesPostQueryDto getSalesPostDto(Long salesPostId){

        return queryFactory
                .select(Projections.fields(SalesPostQueryDto.class,
                        salesPostEntity.id.as("salesPostId"),
                        resumeEntity.salesQuantity.as("salesQuantity"),
                        resumeEntity.price.as("price"),
                        resumeEntity.id.as("resumeId"),
                        resumeEntity.modifiedAt.as("modifiedAt")
                ))
                .from(salesPostEntity)
                .join(salesPostEntity.resumeEntity, resumeEntity)
                .where(salesPostEntity.id.eq(salesPostId))
                .fetchOne();
    }

    private BooleanExpression compareCondBySortType(SalesPostSortType sortType, Long lastId) {
        SalesPostQueryDto salesPostDto = getSalesPostDto(lastId);

        if(SalesPostSortType.OLD.equals(sortType)){
            return (resumeEntity.modifiedAt.eq(salesPostDto.getModifiedAt())
                    .and(resumeEntity.id.gt(salesPostDto.getResumeId())))
                    .or(resumeEntity.modifiedAt.gt(salesPostDto.getModifiedAt()));
        }

        if(SalesPostSortType.LOWEST_PRICE.equals(sortType)){
            return (resumeEntity.price.eq(salesPostDto.getPrice())
                    .and(resumeEntity.id.gt(salesPostDto.getResumeId())))
                    .or(resumeEntity.price.gt(salesPostDto.getPrice()));
        }

        if(SalesPostSortType.HIGHEST_PRICE.equals(sortType)){
            return (resumeEntity.price.eq(salesPostDto.getPrice())
                    .and(resumeEntity.id.lt(salesPostDto.getResumeId())))
                    .or(resumeEntity.price.lt(salesPostDto.getPrice()));
        }

        if(SalesPostSortType.BEST_SELLING.equals(sortType)){
            return (resumeEntity.salesQuantity.eq(salesPostDto.getSalesQuantity())
                    .and(resumeEntity.id.lt(salesPostDto.getSalesPostId())))
                    .or(resumeEntity.salesQuantity.lt(salesPostDto.getSalesQuantity()));
        }

        // NEW, DEFAULT
        return (resumeEntity.modifiedAt.eq(salesPostDto.getModifiedAt())
                .and(resumeEntity.id.lt(salesPostDto.getResumeId())))
                .or(resumeEntity.modifiedAt.lt(salesPostDto.getModifiedAt()));
    }

    private OrderSpecifier<?>[] createOrderSpecifier(SalesPostSortType sortType) {
        return switch (sortType) {
            case OLD -> new OrderSpecifier[] {
                    new OrderSpecifier<>(Order.ASC, resumeEntity.modifiedAt),
                    new OrderSpecifier<>(Order.ASC, resumeEntity.id)
            };
            case NEW, DEFAULT -> new OrderSpecifier[] {
                    new OrderSpecifier<>(Order.DESC, resumeEntity.modifiedAt),
                    new OrderSpecifier<>(Order.DESC, resumeEntity.id)
            };
            case HIGHEST_PRICE -> new OrderSpecifier[] {
                    new OrderSpecifier<>(Order.DESC, resumeEntity.price),
                    new OrderSpecifier<>(Order.DESC, resumeEntity.id)
            };
            case LOWEST_PRICE -> new OrderSpecifier[] {
                    new OrderSpecifier<>(Order.ASC, resumeEntity.price),
                    new OrderSpecifier<>(Order.ASC, resumeEntity.id)
            };
            case BEST_SELLING -> new OrderSpecifier[] {
                    new OrderSpecifier<>(Order.DESC, resumeEntity.salesQuantity),
                    new OrderSpecifier<>(Order.DESC, resumeEntity.id)
            };
        };
    }

    private BooleanExpression reverseCompareCondBySortType(SalesPostSortType sortType, Long lastId) {

        SalesPostQueryDto salesPostDto = getSalesPostDto(lastId);

        if(SalesPostSortType.OLD.equals(sortType)){
            return (resumeEntity.modifiedAt.eq(salesPostDto.getModifiedAt())
                    .and(resumeEntity.id.lt(salesPostDto.getResumeId())))
                    .or(resumeEntity.modifiedAt.lt(salesPostDto.getModifiedAt()));
        }

        if(SalesPostSortType.LOWEST_PRICE.equals(sortType)){
            return (resumeEntity.price.eq(salesPostDto.getPrice())
                    .and(resumeEntity.id.lt(salesPostDto.getResumeId())))
                    .or(resumeEntity.price.lt(salesPostDto.getPrice()));
        }

        if(SalesPostSortType.HIGHEST_PRICE.equals(sortType)){
            return (resumeEntity.price.eq(salesPostDto.getPrice())
                    .and(resumeEntity.id.gt(salesPostDto.getResumeId())))
                    .or(resumeEntity.price.gt(salesPostDto.getPrice()));
        }

        if(SalesPostSortType.BEST_SELLING.equals(sortType)){
            return (resumeEntity.salesQuantity.eq(salesPostDto.getSalesQuantity())
                    .and(resumeEntity.id.gt(salesPostDto.getSalesPostId())))
                    .or(resumeEntity.salesQuantity.gt(salesPostDto.getSalesQuantity()));
        }
        // NEW, DEFAULT
        return (resumeEntity.modifiedAt.eq(salesPostDto.getModifiedAt())
                .and(resumeEntity.id.gt(salesPostDto.getResumeId())))
                .or(resumeEntity.modifiedAt.gt(salesPostDto.getModifiedAt()));
    }

    private OrderSpecifier<?>[] createReversedOrderSpecifier(SalesPostSortType sortType) {
        return switch (sortType) {
            case OLD -> new OrderSpecifier[] {
                    new OrderSpecifier<>(Order.DESC, resumeEntity.modifiedAt),
                    new OrderSpecifier<>(Order.DESC, resumeEntity.id)
            };
            case NEW, DEFAULT -> new OrderSpecifier[] {
                    new OrderSpecifier<>(Order.ASC, resumeEntity.modifiedAt),
                    new OrderSpecifier<>(Order.ASC, resumeEntity.id)
            };
            case HIGHEST_PRICE -> new OrderSpecifier[] { // 살짝 느림
                    new OrderSpecifier<>(Order.ASC, resumeEntity.price),
                    new OrderSpecifier<>(Order.ASC, resumeEntity.id)
            };
            case LOWEST_PRICE -> new OrderSpecifier[] { // 아주 빠름
                    new OrderSpecifier<>(Order.DESC, resumeEntity.price),
                    new OrderSpecifier<>(Order.DESC, resumeEntity.id)
            };
            case BEST_SELLING -> new OrderSpecifier[] { //
                    new OrderSpecifier<>(Order.ASC, resumeEntity.salesQuantity),
                    new OrderSpecifier<>(Order.ASC, resumeEntity.id)
            };
        };
    }

    private BooleanExpression fieldEqual(FieldCond fieldCond) {
        if (fieldCond.getFieldType() == null) {
            return null;
        }
        return resumeEntity.fieldType.eq(fieldCond.getFieldType());
    }

    private BooleanExpression levelEqual(LevelCond levelCond) {
        if (levelCond.getLevelType() == null) {
            return null;
        }
        return resumeEntity.levelType.eq(levelCond.getLevelType());
    }

    private BooleanExpression priceRangeCond(BigDecimal minPrice, BigDecimal maxPrice) {
        BooleanExpression expression = null;

        if (minPrice != null) {
            expression = resumeEntity.price.goe(minPrice);
        }

        if (maxPrice != null) {
            BooleanExpression maxCondition = resumeEntity.price.loe(maxPrice);
            expression = (expression != null) ? expression.and(maxCondition) : maxCondition;
        }

        return expression;
    }
}
