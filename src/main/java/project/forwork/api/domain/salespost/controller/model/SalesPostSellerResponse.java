package project.forwork.api.domain.salespost.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesPostSellerResponse {

    private Long resumeId;
    private String title;
    private Integer salesQuantity;
    private SalesStatus status;
    private LocalDateTime registeredAt;
}
