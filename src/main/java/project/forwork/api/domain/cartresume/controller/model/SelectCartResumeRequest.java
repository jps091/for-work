package project.forwork.api.domain.cartresume.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SelectCartResumeRequest {
    private List<Long> cartResumeIds;
}
