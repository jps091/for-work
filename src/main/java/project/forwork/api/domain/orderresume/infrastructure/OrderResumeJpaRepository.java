package project.forwork.api.domain.orderresume.infrastructure;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.forwork.api.domain.order.infrastructure.OrderEntity;

import java.util.List;

public interface OrderResumeJpaRepository extends JpaRepository<OrderResumeEntity, Long> {
    @Query("select or from OrderResumeEntity or" +
            " join fetch or.orderEntity o" +
            " where o IN :orderEntity")
    List<OrderResumeEntity> findByOrderEntity(@Param("orderEntity") OrderEntity orderEntity);

    @Query("select or from OrderResumeEntity or" +
            " join fetch or.orderEntity o" +
            " where o IN :orderEntities")
    List<OrderResumeEntity> findByOrderEntity(@Param("orderEntities") List<OrderEntity> orderEntities);
}
