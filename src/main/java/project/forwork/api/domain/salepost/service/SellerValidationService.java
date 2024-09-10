package project.forwork.api.domain.salepost.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.error.SalePostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

@RequiredArgsConstructor
@Service
@Builder
@Slf4j
public class SellerValidationService {
    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;

    @Transactional(readOnly = true)
    public Resume validateSellerAndResumeStatus(CurrentUser currentUser, Long resumeId){
        User user = userRepository.getByIdWithThrow(currentUser.getId());
        Resume resume = resumeRepository.getByIdWithThrow(resumeId);

        log.info("userId={}, currentUserId={}",user.getId(), currentUser.getId());
        if(resume.isAuthorMismatch(user.getId())){
            throw new ApiException(SalePostErrorCode.ACCESS_NOT_PERMISSION, user.getId());
        }

        if(resume.isActiveMismatch()){
            throw new ApiException(ResumeErrorCode.STATUS_NOT_ACTIVE, resume.getId());
        }

        return resume;
    }
}
