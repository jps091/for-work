package project.forwork.api.domain.user.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InquiryType {

    ORDER("주문 문의"),
    REFUND("환불 문의"),
    SALES("판매 문의"),
    ETC("기타 문의")
    ;

    private String description;

    @JsonCreator
    public static InquiryType from(String s) {
        for (InquiryType status : InquiryType.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid InquiryType: " + s);
    }
}
