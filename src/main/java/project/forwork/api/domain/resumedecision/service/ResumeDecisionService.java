package project.forwork.api.domain.resumedecision.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Builder
@Transactional
@RequiredArgsConstructor
public class  ResumeDecisionService {

    private final ResumeDecisionRepository resumeDecisionRepository;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public void approve(CurrentUser currentUser, Long resumeId){
        User admin = userRepository.getByIdWithThrow(currentUser.getId());

        Resume resume = resumeRepository.getByIdWithThrow(resumeId);
        resume = resume.updateStatus(ResumeStatus.ACTIVE);
        resumeRepository.save(resume);

        ResumeDecision resumeDecision = ResumeDecision.approve(admin, resume);
        resumeDecisionRepository.save(resumeDecision);
    }

    public void deny(CurrentUser currentUser, Long resumeId){
        User admin = userRepository.getByIdWithThrow(currentUser.getId());

        Resume resume = resumeRepository.getByIdWithThrow(resumeId);
        resume = resume.updateStatus(ResumeStatus.REJECTED);
        resumeRepository.save(resume);

        ResumeDecision resumeDecision = ResumeDecision.deny(admin, resume);
        resumeDecisionRepository.save(resumeDecision);
    }

    public ResumeDecision getByIdWithThrow(Long resumeDecisionId){
        return resumeDecisionRepository.getByIdWithThrow(resumeDecisionId);
    }
}
