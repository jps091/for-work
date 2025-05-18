package project.forwork.api.domain.order.service.port;

import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.infrastructure.model.ResumeDto;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.model.Orders;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.resume.model.Resume;

import java.util.List;
import java.util.Optional;

/***
 * index : 1. status 2. requestId
 */
public interface OrderCommandPort {
    Order save(Order order, List<ResumeDto> resumeDtos);
    Order update(Order order);

    Orders updateAll(Orders orders);
}
