package project.forwork.api.domain.salepost.controller.model;

import lombok.Data;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;

import java.math.BigDecimal;

@Data
public class SalePostResponse {

    private Long id;
    private String title;
    private BigDecimal price;
    private ThumbnailImage thumbnailImage;
}
