package project.forwork.api.domain.orderresume.infrastructure;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.PurchaseInfo;

import java.util.List;

import static project.forwork.api.domain.order.infrastructure.QOrderEntity.*;
import static project.forwork.api.domain.orderresume.infrastructure.QOrderResumeEntity.*;
import static project.forwork.api.domain.resume.infrastructure.QResumeEntity.resumeEntity;
import static project.forwork.api.domain.user.infrastructure.QUserEntity.userEntity;

@Repository
public class OrderResumeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    @Autowired
    public OrderResumeQueryDslRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<PurchaseInfo> getPurchaseInfo() {

        PageRequest pageRequest = PageRequest.of(0, 20);

        List<PurchaseInfo> content = queryFactory
                .select(Projections.fields(PurchaseInfo.class,
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
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageRequest, content.size());
    }
}
