package project.forwork.api.domain.salespost.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesPostPage {
    private List<SalesPostSearchResponse> result;
    private boolean isFirstPage;
    private boolean isLastPage;

    public static SalesPostPage from(List<SalesPostSearchResponse> result, boolean isFirstPage, boolean isLastPage){
        return SalesPostPage.builder()
                .result(result)
                .isFirstPage(isFirstPage)
                .isLastPage(isLastPage)
                .build();
    }
}
