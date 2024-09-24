package project.forwork.api.domain.salespost.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesPostPage {
    private Long lastId;
    private List<SalesPostResponse> results;
}
