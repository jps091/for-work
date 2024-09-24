package project.forwork.api.domain.resume.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.controller.model.*;
import project.forwork.api.domain.resume.infrastructure.ResumeQueryDlsRepository;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.domain.resume.infrastructure.ResumeSearchCond;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.resumedecision.service.port.ResumeDecisionRepository;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeDecisionRepository resumeDecisionRepository;
    private final ResumeQueryDlsRepository resumeQueryDlsRepository;
    private final UserRepository userRepository;

    public Resume register(CurrentUser currentUser, ResumeRegisterRequest body){
        User user = userRepository.getByIdWithThrow(currentUser.getId());
        Resume resume = Resume.from(user, body);
        resume =  resumeRepository.save(resume);
        return resume;
    }

    public void modifyIfPending(
            Long resumeId,
            CurrentUser currentUser,
            ResumeModifyRequest body
    ){
        Resume resume = resumeRepository.getByIdWithThrow(resumeId);
        validateAuthor(currentUser, resume);

        resume = resume.modifyIfPending(body);
        resumeRepository.save(resume);
    }

    public void updatePending(Long resumeId, CurrentUser currentUser){
        Resume resume = resumeRepository.getByIdWithThrow(resumeId);
        validateAuthor(currentUser, resume);

        resume = resume.updateStatus(ResumeStatus.PENDING);
        resumeRepository.save(resume);
    }

    public void delete(Long resumeId, CurrentUser currentUser) {
        Resume resume = resumeRepository.getByIdWithThrow(resumeId);
        validateAuthor(currentUser, resume);

        resumeRepository.delete(resume);
    }

    public Resume getByIdWithThrow(CurrentUser currentUser, Long resumeId){
        Resume resume = resumeRepository.getByIdWithThrow(resumeId);

        validateAuthorOrAdmin(currentUser, resume);
        return resume;
    }

    public Resume getByIdWithThrow(Long resumeId){
        return resumeRepository.getByIdWithThrow(resumeId);
    }

    public List<ResumeResponse> findAll(){
        return resumeRepository.findAll()
                .stream()
                .map(ResumeResponse::from)
                .toList();
    }

    public ResumePage getResumesByCondition(
            int offset,
            int limit,
            String sortBy,
            boolean ascending,
            ResumeSearchCond cond
    ){
        Sort sort = Sort.by(ascending ? Sort.Order.asc(sortBy) : Sort.Order.desc(sortBy));
        PageRequest pageRequest = PageRequest.of(offset, limit, sort);
        Page<ResumeResponse> result = resumeQueryDlsRepository.search(cond, pageRequest);
        return ResumePage.from(result);
    }

    public List<ResumeResponse> findResumesBySeller(CurrentUser currentUser){
        User user = userRepository.getByIdWithThrow(currentUser.getId());

        return resumeRepository.findAllBySeller(user)
                .stream()
                .map(ResumeResponse::from)
                .toList();
    }

    private static void validateAuthor(CurrentUser currentUser, Resume resume) {
        if(resume.isAuthorMismatch(currentUser.getId())){
            throw new ApiException(ResumeErrorCode.ACCESS_NOT_PERMISSION);
        }
    }

    private static void validateAuthorOrAdmin(CurrentUser currentUser, Resume resume) {
        if(currentUser.isAdminMismatch() && resume.isAuthorMismatch(currentUser.getId())){
            throw new ApiException(ResumeErrorCode.ACCESS_NOT_PERMISSION);
        }
    }
}
