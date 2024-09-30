package project.forwork.api.domain.salespost.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.domain.salespost.infrastructure.enums.FieldCond;
import project.forwork.api.domain.salespost.infrastructure.enums.LevelCond;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
public class SalesPostFilterCond {
    private SalesPostSortType sortType;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private FieldCond field;
    private LevelCond level;

    public static SalesPostFilterCond from(
            SalesPostSortType sortType, BigDecimal minPrice, BigDecimal maxPrice,
            FieldCond field, LevelCond level
    ){
        SalesPostSortType sortCond = Objects.requireNonNullElse(sortType, SalesPostSortType.DEFAULT);
        FieldCond fieldCond = Objects.requireNonNullElse(field, FieldCond.UNSELECTED);
        LevelCond levelCond = Objects.requireNonNullElse(level, LevelCond.UNSELECTED);
        BigDecimal minCond = Objects.requireNonNullElse(minPrice, BigDecimal.ZERO);
        BigDecimal maxCond = Objects.requireNonNullElse(maxPrice, new BigDecimal("100000.00"));

        return SalesPostFilterCond.builder()
                .sortType(sortCond)
                .field(fieldCond)
                .level(levelCond)
                .minPrice(minCond)
                .maxPrice(maxCond)
                .build();
    }
}