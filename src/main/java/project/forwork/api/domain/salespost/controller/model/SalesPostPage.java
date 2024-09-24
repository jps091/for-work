package project.forwork.api.domain.salespost.controller.model;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesPostPage {
    private int offset;
    private int limit;
    private List<SalesPostResponse> contents;

    public static SalesPostPage from(Page<SalesPostResponse> response){
        Pageable pageable = response.getPageable();

        return SalesPostPage.builder()
                .offset((int)pageable.getOffset())
                .limit(pageable.getPageSize())
                .contents(response.getContent())
                .build();
    }
}
