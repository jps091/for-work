package project.forwork.api.domain.resume.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.controller.model.ResumeModifyRequest;
import project.forwork.api.domain.resume.controller.model.ResumeRegisterRequest;
import project.forwork.api.domain.resume.controller.model.ResumeResponse;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.domain.user.controller.model.CurrentUser;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public ResumeResponse register(CurrentUser currentUser, ResumeRegisterRequest resumeRegisterRequest){

        User user = userRepository.getById(currentUser.getId());

        Resume resume = Resume.from(user, resumeRegisterRequest);
        resume =  resumeRepository.save(resume);
        return ResumeResponse.from(resume);
    }

    public void modify(
            Long resumeId,
            CurrentUser currentUser,
            ResumeModifyRequest resumeModifyRequest
    ){
        Resume resume = getResumeWithThrow(resumeId);
        validateAuthor(currentUser, resume);

        resume = resume.modify(resumeModifyRequest);
        resumeRepository.save(resume);
    }

    public void delete(CurrentUser currentUser, Long resumeId) {
        Resume resume = getResumeWithThrow(resumeId);
        validateAuthor(currentUser, resume);

        resumeRepository.delete(resume);
    }

    public Resume getResumeWithThrow(Long resumeId) {
        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ApiException(ResumeErrorCode.RESUME_NOT_FOUND, resumeId));
    }

    public List<ResumeResponse> getResumes(){
        return resumeRepository.findAll()
                .stream()
                .map(ResumeResponse::from)
                .toList();
    }

    public List<ResumeResponse> findResumesBySeller(CurrentUser currentUser){
        User user = userRepository.getById(currentUser.getId());

        return resumeRepository.findAllBySeller(user)
                .stream()
                .map(ResumeResponse::from)
                .toList();
    }

    private static void validateAuthor(CurrentUser currentUser, Resume resume) {
        if(resume.isAuthorMismatch(currentUser.getId())){
            throw new ApiException(ResumeErrorCode.AUTHOR_MISMATCH);
        }
    }
}
