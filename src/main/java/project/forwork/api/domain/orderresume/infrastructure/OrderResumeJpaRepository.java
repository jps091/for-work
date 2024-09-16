package project.forwork.api.domain.orderresume.infrastructure;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.forwork.api.domain.order.infrastructure.OrderEntity;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;

import java.util.List;

public interface OrderResumeJpaRepository extends JpaRepository<OrderResumeEntity, Long> {
    @Query("select ore from OrderResumeEntity ore" +
            " join fetch ore.orderEntity o" +
            " where o IN :orderEntity" +
            " and ore.status = :status")
    List<OrderResumeEntity> findByStatusAndOrder(@Param("status") OrderResumeStatus status, @Param("orderEntity") OrderEntity orderEntity);

    @Query("select ore from OrderResumeEntity ore" +
            " join fetch ore.orderEntity o" +
            " where o IN :orderEntities" +
            " and ore.status = :status")
    List<OrderResumeEntity> findByStatusAndOrder(@Param("status") OrderResumeStatus status, @Param("orderEntities") List<OrderEntity> orderEntities);
}
