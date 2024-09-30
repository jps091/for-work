package project.forwork.api.domain.orderresume.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PurchaseResponse {

    private Long orderId;
    private Long resumeId;
    private String email;
    private LevelType level;
    private FieldType field;
    private String resumeUrl;

    public String getSalesPostTitle(){
        return level.getDescription() + " " + field.getDescription() + " 이력서 #" + getResumeId();
    }
}
