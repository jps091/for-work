package project.forwork.api.domain.salespost.infrastructure.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesPostSearchDto {
    private Long resumeId;
    private BigDecimal price;
    private String title;
}
