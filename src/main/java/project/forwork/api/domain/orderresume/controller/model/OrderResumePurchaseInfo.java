package project.forwork.api.domain.orderresume.controller.model;

import lombok.*;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;

@Getter
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class OrderResumePurchaseInfo {
    private Long orderId;
    private Long resumeId;
    private String buyerEmail;
    private LevelType level;
    private FieldType field;
    private String resumeUrl;
    private String sellerEmail;
}
