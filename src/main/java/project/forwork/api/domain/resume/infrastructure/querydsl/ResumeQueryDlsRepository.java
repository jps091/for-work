package project.forwork.api.domain.resume.infrastructure.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import project.forwork.api.domain.resume.controller.model.ResumeResponse;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;

import java.util.List;

import static project.forwork.api.domain.resume.infrastructure.QResumeEntity.*;
import static project.forwork.api.domain.user.infrastructure.QUserEntity.*;

@Repository
public class ResumeQueryDlsRepository {

    private final JPAQueryFactory queryFactory;
    @Autowired
    public ResumeQueryDlsRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<ResumeResponse> search(ResumeSearchCond cond, Pageable pageable) {
        List<ResumeResponse> content = queryFactory
                .select(Projections.fields(ResumeResponse.class,
                        resumeEntity.id.as("id"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        resumeEntity.resumeStatus.as("status"),
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

        JPAQuery<Long> count = queryFactory
                .select(resumeEntity.count())
                .from(resumeEntity)
                .leftJoin(resumeEntity.sellerEntity, userEntity)
                .where(resumeStatusEqual(cond.getResumeStatus()),
                        fieldEqual(cond.getField()),
                        levelEqual(cond.getLevel())
                );

        // 전체 카운트 쿼리 없이 Page 객체 생성 TODO
        //return new PageImpl<>(content, pageable, pageable.isPaged() ? content.size() : 0);
        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
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
