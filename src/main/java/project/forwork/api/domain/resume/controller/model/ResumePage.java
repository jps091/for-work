package project.forwork.api.domain.resume.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.salespost.controller.model.SalesPostSearchResponse;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumePage {
    private List<ResumeAdminResponse> result;
    private boolean isFirstPage;
    private boolean isLastPage;

    public static ResumePage from(List<ResumeAdminResponse> result, boolean isFirstPage, boolean isLastPage){
        return ResumePage.builder()
                .result(result)
                .isFirstPage(isFirstPage)
                .isLastPage(isLastPage)
                .build();
    }
}
