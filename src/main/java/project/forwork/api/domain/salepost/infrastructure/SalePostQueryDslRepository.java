package project.forwork.api.domain.salepost.infrastructure;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.salepost.controller.model.SalePostResponse;
import project.forwork.api.domain.salepost.infrastructure.enums.SalePostSortType;
import project.forwork.api.domain.salepost.infrastructure.enums.SalesStatus;

import java.math.BigDecimal;
import java.util.List;

import static project.forwork.api.domain.resume.infrastructure.QResumeEntity.*;
import static project.forwork.api.domain.salepost.infrastructure.QSalePostEntity.*;

@Repository
public class SalePostQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public SalePostQueryDslRepository(EntityManager em) {
        this.queryFactory  = new JPAQueryFactory(em);
    }

    public Page<SalePostResponse> searchByCondition(SalePostSearchCond cond, Pageable pageable, SalePostSortType sortType){

        OrderSpecifier orderSpecifier = createOrderSpecifier(sortType);


        List<SalePostResponse> content = queryFactory
                .select(Projections.constructor(SalePostResponse.class,
                        salePostEntity.id,
                        salePostEntity.title,
                        resumeEntity.price,
                        //thumbnailImageEntity.url, TODO 썸네일
                        salePostEntity.viewCount,
                        salePostEntity.quantity,
                        resumeEntity.fieldType,
                        resumeEntity.levelType,
                        salePostEntity.salesStatus,
                        salePostEntity.modifiedAt))
                .from(salePostEntity)
                .join(salePostEntity.resumeEntity, resumeEntity)
                //.join(salePostEntity.thumbnailImageEntity, thumbnailImageEntity) // 썸네일
                .where(salePostEntity.salesStatus.eq(SalesStatus.SELLING),
                        priceRangeCond(cond.getMinPrice(), cond.getMaxPrice()),
                        fieldEqual(cond.getField()),
                        levelEqual(cond.getLevel()))
                .orderBy(orderSpecifier, salePostEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, pageable.isPaged() ? content.size() : 0);
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

    private OrderSpecifier createOrderSpecifier(SalePostSortType sortType){
        return switch(sortType){
            case OLD -> new OrderSpecifier<>(Order.ASC, salePostEntity.id);
            case HIGHEST_PRICE -> new OrderSpecifier<>(Order.DESC, resumeEntity.price);
            case LOWEST_PRICE -> new OrderSpecifier<>(Order.ASC, resumeEntity.price);
            case VIEW_COUNT -> new OrderSpecifier<>(Order.DESC, salePostEntity.viewCount);
            case BEST_SELLING -> new OrderSpecifier<>(Order.ASC, salePostEntity.quantity);
            default -> new OrderSpecifier<>(Order.DESC, salePostEntity.id);
        };
    }
}
