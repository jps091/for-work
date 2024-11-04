package project.forwork.api.domain.salespost.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.salespost.infrastructure.model.SalesPostSearchDto;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesPostSearchResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long resumeId;
    private String title;
    private BigDecimal price;
    private String thumbnailImageUrl;

    public static SalesPostSearchResponse from(SalesPostSearchDto dto, String title, String thumbnailImageUrl){
        return SalesPostSearchResponse.builder()
                .resumeId(dto.getResumeId())
                .title(title)
                .price(dto.getPrice())
                .thumbnailImageUrl(thumbnailImageUrl)
                .build();
    }
}
