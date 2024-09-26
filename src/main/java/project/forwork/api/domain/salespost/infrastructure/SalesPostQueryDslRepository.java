package project.forwork.api.domain.salespost.infrastructure;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.salespost.controller.model.SalesPostDetailResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostSellerResponse;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static project.forwork.api.domain.resume.infrastructure.QResumeEntity.*;
import static project.forwork.api.domain.salespost.infrastructure.QSalesPostEntity.*;

@Repository
public class SalesPostQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public SalesPostQueryDslRepository(EntityManager em) {
        this.queryFactory  = new JPAQueryFactory(em);
    }

    public Page<SalesPostResponse> searchByCondition(SalesPostSearchCond cond, Pageable pageable, SalesPostSortType sortType){

        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(sortType);


        List<SalesPostResponse> content = queryFactory
                .select(Projections.constructor(SalesPostResponse.class,
                        salesPostEntity.id,
                        salesPostEntity.title,
                        resumeEntity.price,
                        //thumbnailImageEntity.url, TODO 썸네일
                        salesPostEntity.viewCount,
                        salesPostEntity.salesQuantity,
                        resumeEntity.fieldType,
                        resumeEntity.levelType,
                        salesPostEntity.salesStatus,
                        salesPostEntity.modifiedAt))
                .from(salesPostEntity)
                .join(salesPostEntity.resumeEntity, resumeEntity)
                //.join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity) // 썸네일
                .where(
                        priceRangeCond(cond.getMinPrice(), cond.getMaxPrice()),
                        fieldEqual(cond.getField()),
                        levelEqual(cond.getLevel()),
                        salesPostEntity.salesStatus.eq(SalesStatus.SELLING)
                )
                .orderBy(orderSpecifier, salesPostEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, pageable.isPaged() ? content.size() : 0);
    }
    /***
     * 1. 기본적으로 최신 등록순으로 정렬
     * 2. 정렬 조건 : 최신 등록순, 조회순, 많이팔린순
     * 3. 검색 조건 : 가격(범위), 분야, 년차
     * 4. 페이징처리 :
     */

    public SalesPostDetailResponse getSellingPostWithThrow(Long salesPostId){
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


    public List<SalesPostResponse> findFirstPage(
            SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
            FieldType field, LevelType level, int limit
    ){
        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(sortType);

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
                        priceRangeCond(minPrice, maxPrice),
                        fieldEqual(field),
                        levelEqual(level),
                        salesPostEntity.salesStatus.eq(SalesStatus.SELLING)
                )
                .orderBy(orderSpecifier)
                .limit(limit).fetch();
    }

    public List<SalesPostResponse> findLastPage(
            SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
            FieldType field, LevelType level, int limit
    ){
        OrderSpecifier<?> orderSpecifier = createReversedOrderSpecifier(sortType);

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
                        priceRangeCond(minPrice, maxPrice),
                        fieldEqual(field),
                        levelEqual(level),
                        salesPostEntity.salesStatus.eq(SalesStatus.SELLING)
                )
                .orderBy(orderSpecifier)
                .limit(limit).fetch();

        Collections.reverse(results);
        return results;
    }

    public List<SalesPostResponse> findNextPage(
            SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
            FieldType field, LevelType level,
            Long lastId, int limit
    ){
        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(sortType);

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
                        priceRangeCond(minPrice, maxPrice),
                        fieldEqual(field),
                        levelEqual(level)
                )
                .orderBy(orderSpecifier)
                .limit(limit).fetch();
    }

    public List<SalesPostResponse> findPreviousPage(
            SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
            FieldType field, LevelType level,
            Long lastId, int limit
    ){
        OrderSpecifier<?> orderSpecifier = createReversedOrderSpecifier(sortType);

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
                        priceRangeCond(minPrice, maxPrice),
                        fieldEqual(field),
                        levelEqual(level)
                )
                .orderBy(orderSpecifier)
                .limit(limit).fetch();

        Collections.reverse(results);
        return results;
    }

    private BooleanExpression fieldEqual(FieldType field) {
        return field != null ? resumeEntity.fieldType.eq(field) : null;
    }

    private BooleanExpression levelEqual(LevelType level) {
        return level != null ? resumeEntity.levelType.eq(level) : null;
    }

    private BooleanExpression priceRangeCond3(SalesPostSearchCond cond){
        BigDecimal minPrice = cond.getMinPrice() != null ? cond.getMinPrice() : BigDecimal.ZERO;
        BigDecimal maxPrice = cond.getMaxPrice() != null ? cond.getMaxPrice() : new BigDecimal("100000");

        return resumeEntity.price.goe(minPrice).and(resumeEntity.price.loe(maxPrice));
    }

    private BooleanExpression priceRangeCond2(BigDecimal minPrice, BigDecimal maxPrice){
        BooleanExpression priceCondition = null;

        priceCondition = resumeEntity.price.goe(Objects.requireNonNullElse(minPrice, BigDecimal.ZERO));
        priceCondition = priceCondition.and(resumeEntity.price.loe(Objects.requireNonNullElseGet(maxPrice, () -> new BigDecimal("100000"))));

        return priceCondition;
    }

    private BooleanExpression priceRangeCond(BigDecimal minPrice, BigDecimal maxPrice){
        BooleanExpression priceCondition = null;

        if(minPrice != null){
            priceCondition = resumeEntity.price.goe(minPrice);
        }

        if(maxPrice != null){
            priceCondition = priceCondition != null ? priceCondition.and(resumeEntity.price.loe(maxPrice)) : resumeEntity.price.loe(maxPrice);
        }

        return priceCondition;
    }

    private OrderSpecifier<?> createOrderSpecifier(SalesPostSortType sortType){

        if(sortType == null){
            return new OrderSpecifier<>(Order.DESC, salesPostEntity.id);
        }

        return switch(sortType){
            case OLD -> new OrderSpecifier<>(Order.ASC, salesPostEntity.id);
            case NEW -> new OrderSpecifier<>(Order.DESC, salesPostEntity.id);
            case HIGHEST_PRICE -> new OrderSpecifier<>(Order.DESC, resumeEntity.price);
            case LOWEST_PRICE -> new OrderSpecifier<>(Order.ASC, resumeEntity.price);
            case VIEW_COUNT -> new OrderSpecifier<>(Order.DESC, salesPostEntity.viewCount);
            case BEST_SELLING -> new OrderSpecifier<>(Order.DESC, salesPostEntity.salesQuantity);
        };
    }

    private OrderSpecifier<?> createReversedOrderSpecifier(SalesPostSortType sortType) {

        if(sortType == null){
            return new OrderSpecifier<>(Order.ASC, salesPostEntity.id);
        }

        return switch (sortType) {
            case OLD -> new OrderSpecifier<>(Order.DESC, salesPostEntity.id);
            case NEW -> new OrderSpecifier<>(Order.ASC, salesPostEntity.id);
            case HIGHEST_PRICE -> new OrderSpecifier<>(Order.ASC, resumeEntity.price);
            case LOWEST_PRICE -> new OrderSpecifier<>(Order.DESC, resumeEntity.price);
            case VIEW_COUNT -> new OrderSpecifier<>(Order.ASC, salesPostEntity.viewCount);
            case BEST_SELLING -> new OrderSpecifier<>(Order.ASC, salesPostEntity.salesQuantity);
        };
    }
}
