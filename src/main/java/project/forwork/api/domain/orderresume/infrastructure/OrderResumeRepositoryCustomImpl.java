package project.forwork.api.domain.orderresume.infrastructure;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.controller.model.PurchaseResponse;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom;

import java.util.List;

import static project.forwork.api.domain.order.infrastructure.QOrderEntity.*;
import static project.forwork.api.domain.orderresume.infrastructure.QOrderResumeEntity.*;
import static project.forwork.api.domain.resume.infrastructure.QResumeEntity.resumeEntity;
import static project.forwork.api.domain.user.infrastructure.QUserEntity.userEntity;

@Repository
public class OrderResumeRepositoryCustomImpl implements OrderResumeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Autowired
    public OrderResumeRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<PurchaseResponse> findPurchaseResume() {

        return queryFactory
                .select(Projections.fields(PurchaseResponse.class,
                        orderEntity.id.as("orderId"),
                        userEntity.email.as("email"),
                        resumeEntity.resumeUrl.as("resumeUrl"),
                        resumeEntity.id.as("resumeId"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level")
                        ))
                .from(orderResumeEntity)
                .join(orderResumeEntity.orderEntity, orderEntity)
                .join(orderResumeEntity.resumeEntity, resumeEntity)
                .join(orderEntity.userEntity, userEntity)
                .where(orderResumeEntity.status.eq(OrderResumeStatus.CONFIRM))
                .fetch();
    }

    public List<OrderResumeResponse> findByUserIdAndStatus(Long userId, List<OrderResumeStatus> statuses){
        return queryFactory
                .select(Projections.fields(OrderResumeResponse.class,
                        orderEntity.id.as("orderId"),
                        resumeEntity.price.as("price"),
                        orderEntity.orderedAt.as("orderedAt"),
                        orderResumeEntity.status.as("status"),
                        Expressions.stringTemplate(
                                "CONCAT({0}, ' ', {1}, ' 이력서 #', {2})",
                                resumeEntity.levelType.stringValue(),
                                resumeEntity.fieldType.stringValue(),
                                resumeEntity.id
                        ).as("title")
                ))
                .from(orderResumeEntity)
                .join(orderResumeEntity.orderEntity, orderEntity)
                .join(orderResumeEntity.resumeEntity, resumeEntity)
                .join(orderEntity.userEntity, userEntity)
                .where(orderResumeEntity.status.in(statuses))
                .where(userEntity.id.eq(userId))
                .orderBy(orderEntity.orderedAt.desc())
                .fetch();
    }

    public List<OrderResumeResponse> findByOrderId(Long orderId){
        return queryFactory
                .select(Projections.fields(OrderResumeResponse.class,
                        orderEntity.id.as("orderId"),
                        resumeEntity.price.as("price"),
                        orderEntity.orderedAt.as("orderedAt"),
                        orderResumeEntity.status.as("status"),
                        Expressions.stringTemplate(
                                "CONCAT({0}, ' ', {1}, ' 이력서 #', {2})",
                                resumeEntity.levelType.stringValue(),
                                resumeEntity.fieldType.stringValue(),
                                resumeEntity.id
                        ).as("title")
                ))
                .from(orderResumeEntity)
                .join(orderResumeEntity.orderEntity, orderEntity)
                .join(orderResumeEntity.resumeEntity, resumeEntity)
                .where(orderEntity.id.eq(orderId))
                .fetch();
    }
}
