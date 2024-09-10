package project.forwork.api.domain.salepost.infrastructure.query;

import lombok.Data;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;

import java.math.BigDecimal;

@Data
public class SalePostSearchCond {
    private String title;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private FieldType field;
    private LevelType level;
}
