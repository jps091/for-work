package project.forwork.api.domain.resumedecision.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.domain.resumedecision.controller.model.ResumeDecisionResponse;
import project.forwork.api.domain.resumedecision.infrastructure.ResumeDecision;
import project.forwork.api.domain.resumedecision.infrastructure.enums.DecisionStatus;
import project.forwork.api.domain.resumedecision.service.port.ResumeDecisionRepository;
import project.forwork.api.domain.user.controller.model.CurrentUser;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ResumeDecisionService {

    private final ResumeDecisionRepository resumeDecisionRepository;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final ClockHolder clockHolder;

    public ResumeDecisionResponse approve(CurrentUser currentUser, Long resumeId){

        ResumeDecision resumeDecision = create(currentUser, resumeId, DecisionStatus.APPROVE);
        resumeDecision = resumeDecisionRepository.save(resumeDecision);
        return ResumeDecisionResponse.from(resumeDecision);
    }

    public ResumeDecisionResponse deny(CurrentUser currentUser, Long resumeId){

        ResumeDecision resumeDecision = create(currentUser, resumeId, DecisionStatus.DENY);
        resumeDecision = resumeDecisionRepository.save(resumeDecision);
        return ResumeDecisionResponse.from(resumeDecision);
    }

/*    public Page<ResumeDecision> findAllByResumeStatus(
            int offset,
            int limit,
            String sortBy,
            ResumeStatus resumeStatus
    ){
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(sortBy));
        Page<ResumeDecision> resumeDecisions = resumeDecisionRepository.findAllByResumeStatus(pageRequest, resumeStatus);
    }*/


    private ResumeDecision create(CurrentUser currentUser, Long resumeId, DecisionStatus status){

        Long adminId = currentUser.getId();
        User admin = userRepository.findAdminById(adminId, RoleType.ADMIN)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, adminId));

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ApiException(ResumeErrorCode.RESUME_NOT_FOUND, resumeId));


        return ResumeDecision.builder()
                .admin(admin)
                .resume(resume)
                .decisionStatus(status)
                .decidedAt(clockHolder.now())
                .build();
    }
}
