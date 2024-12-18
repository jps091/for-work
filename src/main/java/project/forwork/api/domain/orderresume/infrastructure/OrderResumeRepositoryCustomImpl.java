package project.forwork.api.domain.orderresume.infrastructure;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;
import project.forwork.api.domain.orderresume.controller.model.OrderTitleResponse;
import project.forwork.api.domain.orderresume.controller.model.OrderResumePurchaseInfo;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom;
import project.forwork.api.domain.user.infrastructure.QUserEntity;

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

    @Override
    public List<OrderResumePurchaseInfo> findAllPurchaseResume(List<OrderResume> orderResumes) {
        List<Long> orderResumeIds = orderResumes.stream().map(OrderResume::getId).toList();
        QUserEntity sellerUser = new QUserEntity("sellerUser");

        return queryFactory
                .select(Projections.fields(OrderResumePurchaseInfo.class,
                        orderEntity.id.as("orderId"),
                        userEntity.email.as("buyerEmail"),
                        resumeEntity.resumeUrl.as("resumeUrl"),
                        resumeEntity.id.as("resumeId"),
                        resumeEntity.fieldType.as("field"),
                        resumeEntity.levelType.as("level"),
                        sellerUser.email.as("sellerEmail")
                        ))
                .from(orderResumeEntity)
                .join(orderResumeEntity.orderEntity, orderEntity)
                .join(orderResumeEntity.resumeEntity, resumeEntity)
                .join(orderEntity.userEntity, userEntity)
                .join(resumeEntity.sellerEntity, sellerUser)
                .where(orderResumeEntity.id.in(orderResumeIds))
                .fetch();
    }

    @Override
    public List<OrderTitleResponse> findOrderTitleByOrderId(Long orderId) {
        return queryFactory
                .select(Projections.fields(OrderTitleResponse.class,
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

    @Override
    public List<OrderResumeResponse> findByOrderId(Long orderId){
        return queryFactory
                .select(Projections.fields(OrderResumeResponse.class,
                        orderResumeEntity.id.as("orderResumeId"),
                        resumeEntity.price.as("price"),
                        orderResumeEntity.status.as("status"),
                        orderResumeEntity.sentAt.as("sentAt"),
                        orderResumeEntity.canceledAt.as("canceledAt"),
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
