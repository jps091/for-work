package project.forwork.api.domain.salespost.controller.model;

import lombok.*;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesPostResponse {

    private Long id;
    private String title;
    private BigDecimal price;
    //private String thumbnailUrl; TODO 썸네일
    private Integer salesQuantity;
    private Integer viewCount;
    private FieldType field;
    private LevelType level;
    private SalesStatus status;
    private LocalDateTime registeredAt;
}
