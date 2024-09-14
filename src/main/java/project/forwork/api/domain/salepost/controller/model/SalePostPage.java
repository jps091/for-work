package project.forwork.api.domain.salepost.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.forwork.api.domain.resume.controller.model.ResumeResponse;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalePostPage {
    private int offset;
    private int limit;
    private List<SalePostResponse> contents;

    public static SalePostPage from(Page<SalePostResponse> response){
        Pageable pageable = response.getPageable();

        return SalePostPage.builder()
                .offset((int)pageable.getOffset())
                .limit(pageable.getPageSize())
                .contents(response.getContent())
                .build();
    }
}