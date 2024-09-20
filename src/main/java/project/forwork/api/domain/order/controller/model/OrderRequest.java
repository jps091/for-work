package project.forwork.api.domain.order.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderRequest {

    @NotNull
    private List<Long> cartResumeIds;
}
