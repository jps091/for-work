package project.forwork.api.domain.salepost.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.salepost.model.SalePost;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalePostResponse {

    private Long id;
    private String title;
    private BigDecimal price;
    private String thumbnailUrl;
    private Integer quantity;
    private Integer viewCount;
    private FieldType field;
    private LevelType level;
    private LocalDateTime registerAt;

    public static SalePostResponse from(SalePost salePost){
        return SalePostResponse.builder()
                .id(salePost.getId())
                .title(salePost.getTitle())
                .price(salePost.getResume().getPrice()) // TODO
                //.thumbnailImage(salePost.getThumbnailImage()) TODO 썸네일
                .build();
    }
}
