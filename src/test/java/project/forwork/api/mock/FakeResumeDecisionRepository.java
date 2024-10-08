package project.forwork.api.mock;


import project.forwork.api.domain.resumedecision.model.ResumeDecision;
import project.forwork.api.domain.resumedecision.service.port.ResumeDecisionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeResumeDecisionRepository implements ResumeDecisionRepository {

    private final AtomicLong id = new AtomicLong(0);
    private final List<ResumeDecision> data = new ArrayList<>();


    @Override
    public ResumeDecision save(ResumeDecision resumeDecision) {
        if (resumeDecision.getId() == null || resumeDecision.getId() == 0) {
            ResumeDecision newDecision = ResumeDecision.builder()
                    .id(id.incrementAndGet())
                    .admin(resumeDecision.getAdmin())
                    .resume(resumeDecision.getResume())
                    .decisionStatus(resumeDecision.getDecisionStatus())
                    .registeredAt(resumeDecision.getRegisteredAt())
                    .build();
            data.add(newDecision);
            return newDecision;
        } else {
            data.removeIf(r -> Objects.equals(r.getId(), resumeDecision.getId()));
            data.add(resumeDecision);
            return resumeDecision;
        }
    }

    @Override
    public void delete(ResumeDecision resumeDecision) {
        data.removeIf(r -> Objects.equals(r.getId(), resumeDecision.getId()));
    }

    @Override
    public ResumeDecision getByIdWithThrow(Long id) {
        return null;
    }

    @Override
    public Optional<ResumeDecision> findById(Long id) {
        return data.stream().filter(r -> Objects.equals(r.getId(), id)).findAny();
    }
}