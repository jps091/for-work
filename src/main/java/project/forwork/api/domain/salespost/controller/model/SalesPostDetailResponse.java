package project.forwork.api.domain.salespost.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesPostDetailResponse {
    private Long id;
    private String title;
    private BigDecimal price;
    //private String thumbnailUrl; TODO 썸네일
    private String descriptionImageUrl;
    private String description;
    private Integer viewCount;
    private FieldType field;
    private LevelType level;
    private SalesStatus status;
    private LocalDateTime registeredAt;
}
