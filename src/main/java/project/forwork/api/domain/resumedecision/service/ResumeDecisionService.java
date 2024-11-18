package project.forwork.api.domain.resumedecision.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.infrastructure.Producer;
import project.forwork.api.common.message.SalesRequestResultMessage;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.domain.resumedecision.model.ResumeDecision;
import project.forwork.api.domain.resumedecision.service.port.ResumeDecisionRepository;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.salespost.service.SalesPostService;
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
    private final SalesPostService salesPostService;
    private final Producer producer;

    public void approve(CurrentUser currentUser, Long resumeId){
        User admin = userRepository.getByIdWithThrow(currentUser.getId());

        Resume resume = resumeRepository.getByIdWithThrow(resumeId);
        resume = resume.updateStatus(ResumeStatus.ACTIVE);
        Resume newResume = resumeRepository.save(resume);

        ResumeDecision resumeDecision = ResumeDecision.approve(admin, resume);
        resumeDecisionRepository.save(resumeDecision);

        salesPostService.registerSalesPost(newResume);
        produceSalesRequestResultMessage(resume, true);
    }

    public void deny(CurrentUser currentUser, Long resumeId){
        User admin = userRepository.getByIdWithThrow(currentUser.getId());

        Resume resume = resumeRepository.getByIdWithThrow(resumeId);
        resume = resume.updateStatus(ResumeStatus.REJECTED);
        resumeRepository.save(resume);

        ResumeDecision resumeDecision = ResumeDecision.deny(admin, resume);
        resumeDecisionRepository.save(resumeDecision);

        produceSalesRequestResultMessage(resume, false);
    }

    private void produceSalesRequestResultMessage(Resume resume, boolean isApprove) {
        SalesRequestResultMessage message =SalesRequestResultMessage.deny(resume.getSellerEmail(), resume.getResumeUrl());
        if(isApprove){
            message = SalesRequestResultMessage.approve(resume.getSellerEmail(), resume.getResumeUrl());
        }
        producer.sendSalesRequestResultMail(message);
    }
}
