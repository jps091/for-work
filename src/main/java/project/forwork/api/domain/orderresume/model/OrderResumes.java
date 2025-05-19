package project.forwork.api.domain.orderresume.model;

import lombok.Getter;

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
}
