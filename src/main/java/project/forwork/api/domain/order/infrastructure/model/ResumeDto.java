package project.forwork.api.domain.order.infrastructure.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Getter
public class ResumeDto {
    private Long resumeId;
    private BigDecimal resumePrice;
}
