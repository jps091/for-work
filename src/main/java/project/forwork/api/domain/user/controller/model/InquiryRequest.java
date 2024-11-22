package project.forwork.api.domain.user.controller.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.user.infrastructure.enums.InquiryType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryRequest {

    @NotBlank(message = "제목을 입력 해주세요.")
    private String title;

    @NotNull(message = "문의 유형을 선택 해주세요.")
    private InquiryType inquiryType;

    @NotBlank(message = "문의 내용을 입력 해주세요.")
    @Size(min = 10, max = 500, message = "문의 내용은 10자 이상 500자 이하여야 합니다.")
    private String content;
}
