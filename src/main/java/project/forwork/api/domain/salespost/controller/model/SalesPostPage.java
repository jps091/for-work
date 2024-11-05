package project.forwork.api.domain.salespost.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesPostPage implements Serializable {
    @Serial
    private static final long serialVersionUID = 3201894841286387742L;
    private Long lastId;
    private List<SalesPostSearchResponse> results;

    public static SalesPostPage from(List<SalesPostSearchResponse> results, SalesPostSearchResponse record){
        return SalesPostPage.builder()
                .results(results)
                .lastId(record.getResumeId())
                .build();
    }
}
