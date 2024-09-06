package project.forwork.api.domain.resumedecision.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.domain.resumedecision.controller.model.ResumeDecisionResponse;
import project.forwork.api.domain.resumedecision.infrastructure.ResumeDecision;
import project.forwork.api.domain.resumedecision.service.port.ResumeDecisionRepository;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class  ResumeDecisionService {

    private final ResumeDecisionRepository resumeDecisionRepository;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public ResumeDecisionResponse approve(CurrentUser currentUser, Long resumeId){
        User admin = userRepository.getByIdWithThrow(currentUser.getId());

        Resume resume = resumeRepository.getByIdWithThrow(resumeId);
        resume = resume.updateStatus(ResumeStatus.ACTIVE);
        resumeRepository.save(resume);

        ResumeDecision resumeDecision = ResumeDecision.approve(admin, resume);
        resumeDecision = resumeDecisionRepository.save(resumeDecision);

        return ResumeDecisionResponse.from(resumeDecision);
    }

    public ResumeDecisionResponse deny(CurrentUser currentUser, Long resumeId){
        User admin = userRepository.getByIdWithThrow(currentUser.getId());

        Resume resume = resumeRepository.getByIdWithThrow(resumeId);
        resume = resume.updateStatus(ResumeStatus.REJECTED);
        resumeRepository.save(resume);

        ResumeDecision resumeDecision = ResumeDecision.deny(admin, resume);
        resumeDecision = resumeDecisionRepository.save(resumeDecision);

        return ResumeDecisionResponse.from(resumeDecision);
    }


}
