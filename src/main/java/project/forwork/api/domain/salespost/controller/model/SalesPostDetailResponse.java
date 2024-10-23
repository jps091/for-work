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
public class SalesPostDetailResponse {
    private Long resumeId;
    private String title;
    private BigDecimal price;
    private String thumbnailImageUrl;
    private String descriptionImageUrl;
    private String description;
    private Integer salesQuantity;
    private FieldType field;
    private LevelType level;
    private SalesStatus status;
    private LocalDateTime registeredAt;
}
