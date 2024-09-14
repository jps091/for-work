package project.forwork.api.domain.orderresume.infrastructure;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderResumeJpaRepository extends JpaRepository<OrderResumeEntity, Long> {
    List<OrderResumeEntity> findAllByOrderEntity_Id(long orderId);
}
