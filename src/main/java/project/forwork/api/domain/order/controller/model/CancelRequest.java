package project.forwork.api.domain.order.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CancelRequest {

    @NotNull
    private List<Long> orderResumeIds;
}
