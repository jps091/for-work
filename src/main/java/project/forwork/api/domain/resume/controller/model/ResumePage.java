package project.forwork.api.domain.resume.controller.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumePage {
    private Long lastId;
    private LocalDateTime lastModifiedAt;
    private List<ResumeAdminResponse> results;
}
