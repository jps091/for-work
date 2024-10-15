package project.forwork.api.domain.salespost.controller.model;

import lombok.*;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SalesPostQueryDto {
    private Long salesPostId;
    private Long resumeId;
    private BigDecimal price;
    private Integer salesQuantity;
}
