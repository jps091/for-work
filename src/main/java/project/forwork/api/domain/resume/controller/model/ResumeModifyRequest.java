package project.forwork.api.domain.resume.controller.model;

import lombok.Data;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;

import java.math.BigDecimal;
@Data
public class ResumeModifyRequest {
    private FieldType field;
    private LevelType level;
    private String resumeUrl;
    private String architectureImageUrl;
    private BigDecimal price;
    private String description;
}
