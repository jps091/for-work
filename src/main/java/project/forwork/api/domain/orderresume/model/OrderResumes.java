package project.forwork.api.domain.orderresume.model;

import lombok.Getter;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.infrastructure.OrderResumeEntity;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderResumes {
    private final List<OrderResume> orderResumes;

    private OrderResumes(List<OrderResume> orderResumes){
        this.orderResumes = orderResumes;
    }

    public static OrderResumes of(List<OrderResume> orderResumes){
        return new OrderResumes(orderResumes);
    }

    public List<OrderResumeEntity> convertToEntityList(Order order){
        return orderResumes.stream()
                .map(or -> OrderResumeEntity.from(or, order))
                .toList();
    }

    public List<Long> getOrderResumeIds(){
        return orderResumes.stream()
                .map(OrderResume::getId)
                .toList();
    }

    public OrderResumes add(OrderResume orderResume){
        List<OrderResume> newList = new ArrayList<>(this.orderResumes);
        newList.add(orderResume);
        return of(newList);
    }

    public OrderResumes confirmIfPaidAndContained(List<Long> orderResumeIds){
        List<OrderResume> filtered = orderResumes.stream()
                .map(or -> or.getStatus() == OrderResumeStatus.PAID
                        && orderResumeIds.contains(or.getId())
                        ? or.updateStatusConfirm() : or)
                .toList();
        return of(filtered);
    }

    public boolean isAllWithStatus(OrderResumeStatus status){
        return orderResumes.stream()
                .allMatch(or -> or.getStatus() == status);
    }

    public OrderResumes confirmPaidOrderResumes(){
        List<OrderResume> filtered = orderResumes.stream()
                .filter(resume -> resume.getStatus() == OrderResumeStatus.PAID)
                .map(OrderResume::updateStatusConfirm)
                .toList();
        return of(filtered);
    }

    public OrderResumes updateStatusCancel(ClockHolder clockHolder){
        List<OrderResume> canceledOrderResumeList = orderResumes.stream()
                .map(or -> or.updateStatusCancel(clockHolder))
                .toList();
        return of(canceledOrderResumeList);
    }

    public OrderResumes cancelIfContainedByIds(List<Long> orderResumeIds, ClockHolder clockHolder){
        List<OrderResume> canceledOrderResumeList = orderResumes.stream()
                .map(or -> orderResumeIds.contains(or.getId())
                        ? or.updateStatusCancel(clockHolder) : or)
                .toList();
        return of(canceledOrderResumeList);
    }

    public BigDecimal calculateAmount(){
        return orderResumes.stream()
                .map(OrderResume::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
