package project.forwork.api.domain.salepost.infrastructure;

import lombok.Data;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;

import java.math.BigDecimal;

@Data // TODO 검색 조건 제목 제거 해야함
public class SalePostSearchCond {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private FieldType field;
    private LevelType level;
}
