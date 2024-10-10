package project.forwork.api.domain.resume.controller.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;

import java.math.BigDecimal;

@Getter
public class ResumeRegisterImageRequest {
    @Schema(type = "string", format = "binary")
    @NotBlank(message = "이력서 관련 이미지를 첨부 해주세요.")
    private MultipartFile descriptionImage;
}
