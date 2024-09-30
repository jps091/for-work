package project.forwork.api.domain.salespost.infrastructure;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.salespost.controller.model.SalesPostDetailResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostFilterCond;
import project.forwork.api.domain.salespost.controller.model.SalesPostResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostSellerResponse;
import project.forwork.api.domain.salespost.infrastructure.enums.FieldCond;
import project.forwork.api.domain.salespost.infrastructure.enums.LevelCond;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salespost.service.port.SalesPostRepositoryCustom;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static project.forwork.api.domain.resume.infrastructure.QResumeEntity.*;
import static project.forwork.api.domain.salespost.infrastructure.QSalesPostEntity.*;

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
                        resumeEntity.price.as("price"),
                        //thumbnailImageEntity.url, TODO 썸네일
                        salesPostEntity.viewCount.as("viewCount"),
                        resumeEntity.descriptionImageUrl.as("descriptionImageUrl"),
                        resumeEntity.description.as("description"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        salesPostEntity.salesStatus.as("status"),
                        salesPostEntity.registeredAt.as("registeredAt")))
                .from(salesPostEntity)
                .join(salesPostEntity.resumeEntity, resumeEntity)
                //.join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity) // 썸네일
                .where(
                        salesPostEntity.id.eq(salesPostId),
                        resumeEntity.resumeStatus.eq(ResumeStatus.ACTIVE),
                        salesPostEntity.salesStatus.eq(SalesStatus.SELLING)
                )
                .fetchOne();
    }

    public List<SalesPostSellerResponse> findBySeller(Long sellerId){

        return queryFactory
                .select(Projections.fields(SalesPostSellerResponse.class,
                        salesPostEntity.id.as("id"),
                        salesPostEntity.title.as("title"),
                        salesPostEntity.salesQuantity.as("salesQuantity"),
                        salesPostEntity.registeredAt.as("registeredAt"),
                        salesPostEntity.salesStatus.as("status")
                        ))
                .from(salesPostEntity)
                .join(salesPostEntity.resumeEntity, resumeEntity)
                .where(resumeEntity.sellerEntity.id.eq(sellerId))
                .fetch();
    }


    public List<SalesPostResponse> findFirstPage(SalesPostFilterCond cond, int limit){
        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(cond.getSortType());

        return queryFactory
                .select(Projections.fields(SalesPostResponse.class,
                        salesPostEntity.id.as("id"),
                        salesPostEntity.title.as("title"),
                        resumeEntity.price.as("price"),
                        //thumbnailImageEntity.url, TODO 썸네일
                        salesPostEntity.viewCount.as("viewCount"),
                        salesPostEntity.salesQuantity.as("salesQuantity"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        salesPostEntity.salesStatus.as("status"),
                        salesPostEntity.registeredAt.as("registeredAt")))
                .from(salesPostEntity)
                .join(salesPostEntity.resumeEntity, resumeEntity)
                //.join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity) // 썸네일
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
        OrderSpecifier<?> orderSpecifier = createReversedOrderSpecifier(cond.getSortType());

        List<SalesPostResponse> results = queryFactory
                .select(Projections.fields(SalesPostResponse.class,
                        salesPostEntity.id.as("id"),
                        salesPostEntity.title.as("title"),
                        resumeEntity.price.as("price"),
                        //thumbnailImageEntity.url, TODO 썸네일
                        salesPostEntity.viewCount.as("viewCount"),
                        salesPostEntity.salesQuantity.as("salesQuantity"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        salesPostEntity.salesStatus.as("status"),
                        salesPostEntity.registeredAt.as("registeredAt")))
                .from(salesPostEntity)
                .join(salesPostEntity.resumeEntity, resumeEntity)
                //.join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity) // 썸네일
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
        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(cond.getSortType());

        return queryFactory
                .select(Projections.fields(SalesPostResponse.class,
                        salesPostEntity.id.as("id"),
                        salesPostEntity.title.as("title"),
                        resumeEntity.price.as("price"),
                        //thumbnailImageEntity.url, TODO 썸네일
                        salesPostEntity.viewCount.as("viewCount"),
                        salesPostEntity.salesQuantity.as("salesQuantity"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        salesPostEntity.salesStatus.as("status"),
                        salesPostEntity.registeredAt.as("registeredAt")))
                .from(salesPostEntity)
                .join(salesPostEntity.resumeEntity, resumeEntity)
                //.join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity) // 썸네일
                .where(salesPostEntity.id.lt(lastId).
                                and(salesPostEntity.salesStatus.eq(SalesStatus.SELLING)),
                        priceRangeCond(cond.getMinPrice(), cond.getMaxPrice()),
                        fieldEqual(cond.getField()),
                        levelEqual(cond.getLevel())
                )
                .orderBy(orderSpecifier)
                .limit(limit).fetch();
    }

    public List<SalesPostResponse> findPreviousPage(SalesPostFilterCond cond, Long lastId, int limit){
        OrderSpecifier<?> orderSpecifier = createReversedOrderSpecifier(cond.getSortType());

        List<SalesPostResponse> results = queryFactory
                .select(Projections.fields(SalesPostResponse.class,
                        salesPostEntity.id.as("id"),
                        salesPostEntity.title.as("title"),
                        resumeEntity.price.as("price"),
                        //thumbnailImageEntity.url, TODO 썸네일
                        salesPostEntity.viewCount.as("viewCount"),
                        salesPostEntity.salesQuantity.as("salesQuantity"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        salesPostEntity.salesStatus.as("status"),
                        salesPostEntity.registeredAt.as("registeredAt")))
                .from(salesPostEntity)
                .join(salesPostEntity.resumeEntity, resumeEntity)
                //.join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity) // 썸네일
                .where(salesPostEntity.id.gt(lastId).
                                and(salesPostEntity.salesStatus.eq(SalesStatus.SELLING)),
                        priceRangeCond(cond.getMinPrice(), cond.getMaxPrice()),
                        fieldEqual(cond.getField()),
                        levelEqual(cond.getLevel())
                )
                .orderBy(orderSpecifier)
                .limit(limit).fetch();

        Collections.reverse(results);
        return results;
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
        return resumeEntity.price.goe(minPrice).and(resumeEntity.price.loe(maxPrice));
    }

    private OrderSpecifier<?> createOrderSpecifier(SalesPostSortType sortType){

        return switch(sortType){
            case OLD -> new OrderSpecifier<>(Order.ASC, salesPostEntity.id);
            case NEW, DEFAULT -> new OrderSpecifier<>(Order.DESC, salesPostEntity.id);
            case HIGHEST_PRICE -> new OrderSpecifier<>(Order.DESC, resumeEntity.price);
            case LOWEST_PRICE -> new OrderSpecifier<>(Order.ASC, resumeEntity.price);
            case VIEW_COUNT -> new OrderSpecifier<>(Order.DESC, salesPostEntity.viewCount);
            case BEST_SELLING -> new OrderSpecifier<>(Order.DESC, salesPostEntity.salesQuantity);
        };
    }

    private OrderSpecifier<?> createReversedOrderSpecifier(SalesPostSortType sortType) {

        return switch (sortType) {
            case OLD -> new OrderSpecifier<>(Order.DESC, salesPostEntity.id);
            case NEW, DEFAULT -> new OrderSpecifier<>(Order.ASC, salesPostEntity.id);
            case HIGHEST_PRICE -> new OrderSpecifier<>(Order.ASC, resumeEntity.price);
            case LOWEST_PRICE -> new OrderSpecifier<>(Order.DESC, resumeEntity.price);
            case VIEW_COUNT -> new OrderSpecifier<>(Order.ASC, salesPostEntity.viewCount);
            case BEST_SELLING -> new OrderSpecifier<>(Order.ASC, salesPostEntity.salesQuantity);
        };
    }
}
