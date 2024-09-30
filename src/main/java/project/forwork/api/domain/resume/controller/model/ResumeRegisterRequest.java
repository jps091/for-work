package project.forwork.api.domain.resume.controller.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeRegisterRequest {
    @NotNull(message = "관련 분야 선택은 필수 입니다.")
    private FieldType field;

    @NotNull(message = "년차 선택은 필수 입니다.")
    private LevelType level;

    //@NotBlank(message = "이력서 공유 URL을 입력 해주세요.")
    private String resumeUrl;

    //@NotBlank(message = "이력서 공유 URL을 입력 해주세요.")
    private String descriptionImageUrl;

    @NotNull(message = "희망 판매 가격을 입력 해주세요.")
    @DecimalMin(value = "10000.0", inclusive = false, message = "최소 판매 가격은 10,000원 이상입니다.")
    @DecimalMax(value = "100000.0", message = "최대 판매 가격은 100,000원 이상입니다.")
    private BigDecimal price;

    @NotBlank(message = "이력서 설명을 입력 해주세요.")
    private String description;
}
