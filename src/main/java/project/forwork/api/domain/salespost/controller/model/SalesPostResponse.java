package project.forwork.api.domain.salespost.controller.model;

import lombok.*;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salespost.model.SalesPost;

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
    private Integer quantity;
    private Integer viewCount;
    private FieldType field;
    private LevelType level;
    private SalesStatus salesStatus;
    private LocalDateTime registerAt;

    public static SalesPostResponse from(SalesPost salesPost){
        return SalesPostResponse.builder()
                .id(salesPost.getId())
                .title(salesPost.getTitle())
                .price(salesPost.getResume().getPrice()) // TODO
                //.thumbnailImage(salesPost.getThumbnailImage()) TODO 썸네일
                .build();
    }
}
