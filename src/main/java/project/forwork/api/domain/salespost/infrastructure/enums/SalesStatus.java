package project.forwork.api.domain.salespost.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SalesStatus {

    SELLING("판매중"),
    CANCELED("판매중지"),
    ;

    private String description;

    @JsonCreator
    public static SalesStatus from(String s) {
        for (SalesStatus status : SalesStatus.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid SalesStatus: " + s);
    }
}
