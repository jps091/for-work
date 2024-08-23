package project.forwork.api.domain.saleresume.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SaleResumeStatus {

    PENDING("대기"),
    ACTIVE("활성"),
    REJECTED("거절")
    ;

    private String description;

    @JsonCreator
    public static SaleResumeStatus from(String s) {
        for (SaleResumeStatus status : SaleResumeStatus.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid SaleResumeStatus: " + s);
    }

}
