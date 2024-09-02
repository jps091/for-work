package project.forwork.api.domain.resume.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.forwork.api.domain.resumedecision.controller.model.ResumeDecisionResponse;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumePage {
    private int offset;
    private int limit;
    private List<ResumeAdminResponse> contents;

    public static ResumePage from(Page<ResumeAdminResponse> response){
        Pageable pageable = response.getPageable();

        return ResumePage.builder()
                .offset((int)pageable.getOffset())
                .limit(pageable.getPageSize())
                .contents(response.getContent())
                .build();
    }
}
