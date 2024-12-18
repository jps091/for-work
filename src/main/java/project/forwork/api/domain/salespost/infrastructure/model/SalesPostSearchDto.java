package project.forwork.api.domain.salespost.infrastructure.model;

import lombok.*;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesPostSearchDto {
    private Long resumeId;
    private BigDecimal price;
    private FieldType field;
    private LevelType level;
}
