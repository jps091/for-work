package project.forwork.api.domain.salespost.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.salespost.infrastructure.model.SalesPostSearchDto;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesPostSearchResponse {
    private Long resumeId;
    private String title;
    private BigDecimal price;
    private String thumbnailImageUrl;

    public static SalesPostSearchResponse from(SalesPostSearchDto dto){
        return SalesPostSearchResponse.builder()
                .resumeId(dto.getResumeId())
                .title(dto.getTitle())
                .price(dto.getPrice())
                .thumbnailImageUrl(dto.getUrl())
                .build();
    }
}
