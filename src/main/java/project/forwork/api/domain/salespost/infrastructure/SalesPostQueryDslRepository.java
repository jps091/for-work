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
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.salespost.controller.model.SalesPostResponse;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static project.forwork.api.domain.resume.infrastructure.QResumeEntity.*;

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
                        QSalesPostEntity.salesPostEntity.id,
                        QSalesPostEntity.salesPostEntity.title,
                        resumeEntity.price,
                        //thumbnailImageEntity.url, TODO 썸네일
                        QSalesPostEntity.salesPostEntity.viewCount,
                        QSalesPostEntity.salesPostEntity.quantity,
                        resumeEntity.fieldType,
                        resumeEntity.levelType,
                        QSalesPostEntity.salesPostEntity.salesStatus,
                        QSalesPostEntity.salesPostEntity.modifiedAt))
                .from(QSalesPostEntity.salesPostEntity)
                .join(QSalesPostEntity.salesPostEntity.resumeEntity, resumeEntity)
                //.join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity) // 썸네일
                .where(
                        priceRangeCond(cond.getMinPrice(), cond.getMaxPrice()),
                        fieldEqual(cond.getField()),
                        levelEqual(cond.getLevel()),
                        QSalesPostEntity.salesPostEntity.salesStatus.eq(SalesStatus.SELLING)
                )
                .orderBy(orderSpecifier, QSalesPostEntity.salesPostEntity.id.desc())
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
    public List<SalesPostResponse> findFirstPage(
            SalesPostSearchCond cond,
            SalesPostSortType sortType, int limit
    ){
        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(sortType);

        return queryFactory
                .select(Projections.constructor(SalesPostResponse.class,
                        QSalesPostEntity.salesPostEntity.id,
                        QSalesPostEntity.salesPostEntity.title,
                        resumeEntity.price,
                        //thumbnailImageEntity.url, TODO 썸네일
                        QSalesPostEntity.salesPostEntity.viewCount,
                        QSalesPostEntity.salesPostEntity.quantity,
                        resumeEntity.fieldType,
                        resumeEntity.levelType,
                        QSalesPostEntity.salesPostEntity.salesStatus,
                        QSalesPostEntity.salesPostEntity.modifiedAt))
                .from(QSalesPostEntity.salesPostEntity)
                .join(QSalesPostEntity.salesPostEntity.resumeEntity, resumeEntity)
                //.join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity) // 썸네일
                .where(
                        priceRangeCond(cond.getMinPrice(), cond.getMaxPrice()),
                        fieldEqual(cond.getField()),
                        levelEqual(cond.getLevel()),
                        QSalesPostEntity.salesPostEntity.salesStatus.eq(SalesStatus.SELLING)
                )
                .orderBy(orderSpecifier)
                .limit(limit).fetch();
    }

    public List<SalesPostResponse> findLastPage(
            SalesPostSearchCond cond,
            SalesPostSortType sortType, int limit
    ){
        OrderSpecifier<?> orderSpecifier = createReversedOrderSpecifier(sortType);

        List<SalesPostResponse> results = queryFactory
                .select(Projections.constructor(SalesPostResponse.class,
                        QSalesPostEntity.salesPostEntity.id,
                        QSalesPostEntity.salesPostEntity.title,
                        resumeEntity.price,
                        //thumbnailImageEntity.url, TODO 썸네일
                        QSalesPostEntity.salesPostEntity.viewCount,
                        QSalesPostEntity.salesPostEntity.quantity,
                        resumeEntity.fieldType,
                        resumeEntity.levelType,
                        QSalesPostEntity.salesPostEntity.salesStatus,
                        QSalesPostEntity.salesPostEntity.modifiedAt))
                .from(QSalesPostEntity.salesPostEntity)
                .join(QSalesPostEntity.salesPostEntity.resumeEntity, resumeEntity)
                //.join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity) // 썸네일
                .where(
                        priceRangeCond(cond.getMinPrice(), cond.getMaxPrice()),
                        fieldEqual(cond.getField()),
                        levelEqual(cond.getLevel()),
                        QSalesPostEntity.salesPostEntity.salesStatus.eq(SalesStatus.SELLING)
                )
                .orderBy(orderSpecifier)
                .limit(limit).fetch();

        Collections.reverse(results);
        return results;
    }

    public List<SalesPostResponse> findNextPage(
            SalesPostSearchCond cond, Long lastId,
            SalesPostSortType sortType, int limit
    ){
        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(sortType);

        return queryFactory
                .select(Projections.constructor(SalesPostResponse.class,
                        QSalesPostEntity.salesPostEntity.id,
                        QSalesPostEntity.salesPostEntity.title,
                        resumeEntity.price,
                        //thumbnailImageEntity.url, TODO 썸네일
                        QSalesPostEntity.salesPostEntity.viewCount,
                        QSalesPostEntity.salesPostEntity.quantity,
                        resumeEntity.fieldType,
                        resumeEntity.levelType,
                        QSalesPostEntity.salesPostEntity.salesStatus,
                        QSalesPostEntity.salesPostEntity.modifiedAt))
                .from(QSalesPostEntity.salesPostEntity)
                .join(QSalesPostEntity.salesPostEntity.resumeEntity, resumeEntity)
                //.join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity) // 썸네일
                .where(QSalesPostEntity.salesPostEntity.id.lt(lastId).
                                and(QSalesPostEntity.salesPostEntity.salesStatus.eq(SalesStatus.SELLING)),
                        priceRangeCond(cond.getMinPrice(), cond.getMaxPrice()),
                        fieldEqual(cond.getField()),
                        levelEqual(cond.getLevel())
                )
                .orderBy(orderSpecifier)
                .limit(limit).fetch();
    }

    public List<SalesPostResponse> findPreviousPage(
            SalesPostSearchCond cond, Long lastId,
            SalesPostSortType sortType, int limit
    ){
        OrderSpecifier<?> orderSpecifier = createReversedOrderSpecifier(sortType);

        List<SalesPostResponse> results = queryFactory
                .select(Projections.constructor(SalesPostResponse.class,
                        QSalesPostEntity.salesPostEntity.id,
                        QSalesPostEntity.salesPostEntity.title,
                        resumeEntity.price,
                        //thumbnailImageEntity.url, TODO 썸네일
                        QSalesPostEntity.salesPostEntity.viewCount,
                        QSalesPostEntity.salesPostEntity.quantity,
                        resumeEntity.fieldType,
                        resumeEntity.levelType,
                        QSalesPostEntity.salesPostEntity.salesStatus,
                        QSalesPostEntity.salesPostEntity.modifiedAt))
                .from(QSalesPostEntity.salesPostEntity)
                .join(QSalesPostEntity.salesPostEntity.resumeEntity, resumeEntity)
                //.join(salesPostEntity.thumbnailImageEntity, thumbnailImageEntity) // 썸네일
                .where(QSalesPostEntity.salesPostEntity.id.gt(lastId).
                                and(QSalesPostEntity.salesPostEntity.salesStatus.eq(SalesStatus.SELLING)),
                        priceRangeCond(cond.getMinPrice(), cond.getMaxPrice()),
                        fieldEqual(cond.getField()),
                        levelEqual(cond.getLevel())
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
            return new OrderSpecifier<>(Order.DESC, QSalesPostEntity.salesPostEntity.id);
        }

        return switch(sortType){
            case OLD -> new OrderSpecifier<>(Order.ASC, QSalesPostEntity.salesPostEntity.id);
            case NEW -> new OrderSpecifier<>(Order.DESC, QSalesPostEntity.salesPostEntity.id);
            case HIGHEST_PRICE -> new OrderSpecifier<>(Order.DESC, resumeEntity.price);
            case LOWEST_PRICE -> new OrderSpecifier<>(Order.ASC, resumeEntity.price);
            case VIEW_COUNT -> new OrderSpecifier<>(Order.DESC, QSalesPostEntity.salesPostEntity.viewCount);
            case BEST_SELLING -> new OrderSpecifier<>(Order.ASC, QSalesPostEntity.salesPostEntity.quantity);
        };
    }

    private OrderSpecifier<?> createReversedOrderSpecifier(SalesPostSortType sortType) {

        if(sortType == null){
            return new OrderSpecifier<>(Order.ASC, QSalesPostEntity.salesPostEntity.id);
        }

        return switch (sortType) {
            case OLD -> new OrderSpecifier<>(Order.DESC, QSalesPostEntity.salesPostEntity.id);
            case NEW -> new OrderSpecifier<>(Order.ASC, QSalesPostEntity.salesPostEntity.id);
            case HIGHEST_PRICE -> new OrderSpecifier<>(Order.ASC, resumeEntity.price);
            case LOWEST_PRICE -> new OrderSpecifier<>(Order.DESC, resumeEntity.price);
            case VIEW_COUNT -> new OrderSpecifier<>(Order.ASC, QSalesPostEntity.salesPostEntity.viewCount);
            case BEST_SELLING -> new OrderSpecifier<>(Order.DESC, QSalesPostEntity.salesPostEntity.quantity);
        };
    }
}
