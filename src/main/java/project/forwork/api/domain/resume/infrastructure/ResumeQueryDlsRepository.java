package project.forwork.api.domain.resume.infrastructure;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.resume.controller.model.ResumeResponse;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.PeriodCond;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static project.forwork.api.domain.resume.infrastructure.QResumeEntity.*;
import static project.forwork.api.domain.user.infrastructure.QUserEntity.*;

@Repository
public class ResumeQueryDlsRepository {

    private final JPAQueryFactory queryFactory;
    private final ClockHolder clockHolder;
    @Autowired
    public ResumeQueryDlsRepository(EntityManager em, ClockHolder clockHolder) {
        this.queryFactory = new JPAQueryFactory(em);
        this.clockHolder = clockHolder;
    }

    public Page<ResumeResponse> search(ResumeSearchCond cond, Pageable pageable) {
        List<ResumeResponse> content = queryFactory
                .select(Projections.fields(ResumeResponse.class,
                        resumeEntity.id.as("id"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        resumeEntity.resumeStatus.as("status"),
                        resumeEntity.modifiedAt.as("modifiedAt"),
                        userEntity.email.as("email")))
                .from(resumeEntity)
                .join(resumeEntity.sellerEntity, userEntity)
                .where(resumeStatusEqual(cond.getResumeStatus()),
                        fieldEqual(cond.getField()),
                        levelEqual(cond.getLevel()))
                .orderBy(getSortOrders(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, pageable.isPaged() ? content.size() : 0);
    }

    public List<ResumeResponse> findFirstPage(ResumeSearchCond cond, int limit) {
        return  queryFactory
                .select(Projections.fields(ResumeResponse.class,
                        resumeEntity.id.as("id"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        resumeEntity.resumeStatus.as("status"),
                        resumeEntity.modifiedAt.as("modifiedAt"),
                        userEntity.email.as("email")))
                .from(resumeEntity)
                .join(resumeEntity.sellerEntity, userEntity)
                .where(dateRangeCond(cond.getPeriodCond()),
                        resumeStatusEqual(cond.getResumeStatus())
                )
                .orderBy(resumeEntity.modifiedAt.asc(), resumeEntity.id.asc())
                .limit(limit)
                .fetch();
    }

    public List<ResumeResponse> findLastPage(ResumeSearchCond cond, int limit) {
        List<ResumeResponse> results = queryFactory
                .select(Projections.fields(ResumeResponse.class,
                        resumeEntity.id.as("id"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        resumeEntity.resumeStatus.as("status"),
                        resumeEntity.modifiedAt.as("modifiedAt"),
                        userEntity.email.as("email")))
                .from(resumeEntity)
                .join(resumeEntity.sellerEntity, userEntity)
                .where(dateRangeCond(cond.getPeriodCond()),
                        resumeStatusEqual(cond.getResumeStatus())
                )
                .orderBy(resumeEntity.modifiedAt.desc(), resumeEntity.id.desc())
                .limit(limit)
                .fetch();

        // 서버에서 데이터를 오름차순으로 정렬
        Collections.reverse(results);

        return results;
    }

    public List<ResumeResponse> findNextPage(
            ResumeSearchCond cond, LocalDateTime lastModifiedAt,
            Long lastId, int limit
    ) {
        return  queryFactory
                .select(Projections.fields(ResumeResponse.class,
                        resumeEntity.id.as("id"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        resumeEntity.resumeStatus.as("status"),
                        resumeEntity.modifiedAt.as("modifiedAt"),
                        userEntity.email.as("email")))
                .from(resumeEntity)
                .join(resumeEntity.sellerEntity, userEntity)
                // TODO 필터링 조건 순서에 따른 성능 테스트 필요
                .where(dateRangeCond(cond.getPeriodCond()),
                        resumeStatusEqual(cond.getResumeStatus()),
                        resumeEntity.modifiedAt.eq(lastModifiedAt)
                                .and(resumeEntity.id.gt(lastId))
                                .or(resumeEntity.modifiedAt.gt(lastModifiedAt))
                )
                .orderBy(resumeEntity.modifiedAt.asc(), resumeEntity.id.asc())
                .limit(limit)
                .fetch();
    }

    public List<ResumeResponse> findPreviousPage(
            ResumeSearchCond cond, LocalDateTime firstModifiedAt,
            Long firstId, int limit
    ) {
        List<ResumeResponse> results = queryFactory
                .select(Projections.fields(ResumeResponse.class,
                        resumeEntity.id.as("id"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        resumeEntity.resumeStatus.as("status"),
                        resumeEntity.modifiedAt.as("modifiedAt"),
                        userEntity.email.as("email")))
                .from(resumeEntity)
                .join(resumeEntity.sellerEntity, userEntity)
                .where(dateRangeCond(cond.getPeriodCond()),
                        resumeStatusEqual(cond.getResumeStatus()),
                        (
                                resumeEntity.modifiedAt.eq(firstModifiedAt)
                                        .and(resumeEntity.id.lt(firstId))
                        )
                                .or(resumeEntity.modifiedAt.lt(firstModifiedAt))
                )
                .orderBy(resumeEntity.modifiedAt.desc(), resumeEntity.id.desc())
                .limit(limit)
                .fetch();

        // 서버에서 데이터를 오름차순으로 정렬
        Collections.reverse(results);

        return results;
    }

    private Predicate dateRangeCond(PeriodCond cond){

        if(cond == null){
            return null;
        }

        LocalDateTime now = clockHolder.now();
        LocalDateTime startOfToday = clockHolder.nowDate().atStartOfDay();

        return switch(cond){
            case TODAY -> resumeEntity.modifiedAt.goe(startOfToday).and(resumeEntity.modifiedAt.lt(now));
            case WEEK -> resumeEntity.modifiedAt.goe(startOfToday.minusWeeks(1)).and(resumeEntity.modifiedAt.lt(now));
            case MONTH -> resumeEntity.modifiedAt.goe(startOfToday.minusMonths(1)).and(resumeEntity.modifiedAt.lt(now));
        };
        //yield resumeEntity.modifiedAt.between(start, now); // 성능 측정 필요 TODO
    }

    private BooleanExpression resumeStatusEqual(ResumeStatus status) {
        return status == null ? null : resumeEntity.resumeStatus.eq(status);
    }

    private BooleanExpression fieldEqual(FieldType field) {
        return field == null ? null : resumeEntity.fieldType.eq(field);
    }

    private BooleanExpression levelEqual(LevelType level) {
        return level == null ? null : resumeEntity.levelType.eq(level);
    }

    private OrderSpecifier<?> getSortOrder(String property, Order order) {
        PathBuilder<Object> entityPath = new PathBuilder<>(resumeEntity.getType(), resumeEntity.getMetadata());

        ComparableExpressionBase<?> path = entityPath.getComparable(property, Comparable.class);

        return new OrderSpecifier<>(order, path);
    }

    private OrderSpecifier<?>[] getSortOrders(Pageable pageable) {
        return pageable.getSort().stream()
                .map(order -> getSortOrder(order.getProperty(), order.isAscending() ? Order.ASC : Order.DESC))
                .toArray(OrderSpecifier[]::new);
    }
}
