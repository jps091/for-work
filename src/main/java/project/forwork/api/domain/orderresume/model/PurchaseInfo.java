package project.forwork.api.domain.orderresume.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;

@Getter
@AllArgsConstructor
@Builder
public class PurchaseInfo {

    private final Long orderId;
    private final Long resumeId;
    private final String email;
    private final LevelType level;
    private final FieldType field;
    private final String resumeUrl;

    public String getSalePostTitle(){
        return level.getDescription() + " : " + field.getDescription() + " 이력서 #" + getResumeId();
    }
}
