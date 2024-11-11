package project.forwork.api.domain.resume.infrastructure;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.resume.controller.model.ResumeAdminResponse;
import project.forwork.api.domain.resume.infrastructure.enums.PeriodCond;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.service.port.ResumeRepositoryCustom;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static project.forwork.api.domain.resume.infrastructure.QResumeEntity.*;

@Repository
public class ResumeRepositoryCustomImpl implements ResumeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final ClockHolder clockHolder;
    @Autowired
    public ResumeRepositoryCustomImpl(EntityManager em, ClockHolder clockHolder) {
        this.queryFactory = new JPAQueryFactory(em);
        this.clockHolder = clockHolder;
    }

    public List<ResumeAdminResponse> findFirstPage(
            PeriodCond periodCond, ResumeStatus status, int limit
    ){
        return  queryFactory
                .select(Projections.fields(ResumeAdminResponse.class,
                        resumeEntity.id.as("id"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        resumeEntity.resumeStatus.as("status"),
                        resumeEntity.registeredAt.as("registeredAt"),
                        Expressions.stringTemplate("concat('#', {0}, ' ', substring({1}, 1, 6), '..')",
                                resumeEntity.id, resumeEntity.description).as("summary")))
                .from(resumeEntity)
                .where(dateRangeCond(periodCond),
                        resumeStatusEqual(status)
                )
                .orderBy(resumeEntity.id.asc())
                .limit(limit)
                .fetch();
    }

    public List<ResumeAdminResponse> findLastPage(
            PeriodCond periodCond, ResumeStatus status, int limit
    ){
        List<ResumeAdminResponse> results = queryFactory
                .select(Projections.fields(ResumeAdminResponse.class,
                        resumeEntity.id.as("id"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        resumeEntity.resumeStatus.as("status"),
                        resumeEntity.registeredAt.as("registeredAt"),
                        Expressions.stringTemplate("concat('#', {0}, ' ', substring({1}, 1, 6), '..')",
                                resumeEntity.id, resumeEntity.description).as("summary")))
                .from(resumeEntity)
                .where(dateRangeCond(periodCond),
                        resumeStatusEqual(status)
                )
                .orderBy(resumeEntity.id.desc())
                .limit(limit)
                .fetch();

        // 서버에서 데이터를 오름차순으로 정렬
        Collections.reverse(results);
        return results;
    }

    public List<ResumeAdminResponse> findNextPage(
            PeriodCond periodCond, ResumeStatus status,
            Long lastId, int limit
    ){
        return  queryFactory
                .select(Projections.fields(ResumeAdminResponse.class,
                        resumeEntity.id.as("id"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        resumeEntity.resumeStatus.as("status"),
                        resumeEntity.registeredAt.as("registeredAt"),
                        Expressions.stringTemplate("concat('#', {0}, ' ', substring({1}, 1, 6), '..')",
                                resumeEntity.id, resumeEntity.description).as("summary")))
                .from(resumeEntity)
                .where(dateRangeCond(periodCond),
                        resumeStatusEqual(status),
                        (resumeEntity.id.gt(lastId)))
                .orderBy(resumeEntity.id.asc())
                .limit(limit)
                .fetch();
    }

    public List<ResumeAdminResponse> findPreviousPage(
            PeriodCond periodCond, ResumeStatus status,
            Long lastId, int limit
    ){
        List<ResumeAdminResponse> results = queryFactory
                .select(Projections.fields(ResumeAdminResponse.class,
                        resumeEntity.id.as("id"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        resumeEntity.resumeStatus.as("status"),
                        resumeEntity.registeredAt.as("registeredAt"),
                        Expressions.stringTemplate("concat('#', {0}, ' ', substring({1}, 1, 6), '..')",
                                resumeEntity.id, resumeEntity.description).as("summary")))
                .from(resumeEntity)
                .where(dateRangeCond(periodCond),
                        resumeStatusEqual(status),
                        (resumeEntity.id.lt(lastId)))
                .orderBy(resumeEntity.id.desc())
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
    }

    private BooleanExpression resumeStatusEqual(ResumeStatus status) {
        return status == null ? null : resumeEntity.resumeStatus.eq(status);
    }
}
