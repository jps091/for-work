package project.forwork.api.domain.resume.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.forwork.api.common.controller.port.S3Service;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.cartresume.service.port.CartResumeRepository;
import project.forwork.api.domain.resume.controller.model.*;
import project.forwork.api.common.infrastructure.enums.PageStep;
import project.forwork.api.domain.resume.infrastructure.enums.PeriodCond;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salespost.service.port.SalesPostRepository;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumePageService resumePageService;
    private final UserRepository userRepository;
    private final SalesPostRepository salesPostRepository;
    private final CartResumeRepository cartResumeRepository;
    private final S3Service s3Service;

    @Transactional
    public Resume register(CurrentUser currentUser, ResumeRegisterRequest body, MultipartFile file){
        User user = userRepository.getByIdWithThrow(currentUser.getId());
        String descriptionUrl = s3Service.saveFile(file);

        Resume resume = Resume.from(user, body, descriptionUrl);
        resume =  resumeRepository.save(resume);
        return resume;
    }
    @Transactional
    public void modify(
            Long resumeId, CurrentUser currentUser,
            ResumeModifyRequest body, MultipartFile file
    ){
        Resume resume = resumeRepository.getByIdWithThrow(resumeId);
        validateAuthor(currentUser, resume);

        String descriptionUrl = null;
        if(file != null && !file.isEmpty()){
            descriptionUrl = s3Service.saveFile(file);
        }

        resume = resume.modify(body, descriptionUrl);
        resumeRepository.save(resume);


        // 만약 이미 승인을 받아서 판매글이 있다면 판매글 상태 변경
        salesPostRepository.findByResumeId(resumeId).ifPresent(salesPost -> {
            salesPost = salesPost.changeStatus(SalesStatus.CANCELED);
            salesPostRepository.save(salesPost);
        });
    }
    @Transactional
    public void delete(Long resumeId, CurrentUser currentUser) {
        Resume resume = resumeRepository.getByIdWithThrow(resumeId);
        validateAuthor(currentUser, resume);

        s3Service.deleteFile(resume.getDescriptionImageUrl());
        resume = resume.delete();
        resumeRepository.save(resume);

        salesPostRepository.deleteByResumeId(resumeId);
        cartResumeRepository.deleteAllByResumeId(resumeId);
    }
    @Transactional
    public void deleteAll(CurrentUser currentUser) {
        List<ResumeStatus> statusList = List.of(ResumeStatus.ACTIVE, ResumeStatus.PENDING, ResumeStatus.REJECTED);

        List<Resume> resumeList = resumeRepository.findAllBySeller(currentUser.getId(), statusList);
        if(resumeList.isEmpty()){
            return;
        }

        List<Long> resumeIds = resumeList.stream()
                .map(Resume::delete)
                .map(resumeRepository::save)
                .map(Resume::getId)
                .toList();

        resumeIds.forEach(resumeId -> {
            salesPostRepository.deleteByResumeId(resumeId);
            cartResumeRepository.deleteAllByResumeId(resumeId);
        });
    }

    @Transactional(readOnly = true)
    public Resume getByIdWithThrow(CurrentUser currentUser, Long resumeId){
        Resume resume = resumeRepository.getByIdWithThrow(resumeId);

        validateAuthorOrAdmin(currentUser, resume);
        return resume;
    }
    @Transactional(readOnly = true)
    public List<ResumeSellerResponse> findResumesBySeller(CurrentUser currentUser){
        List<ResumeStatus> statusList = List.of(ResumeStatus.ACTIVE, ResumeStatus.PENDING, ResumeStatus.REJECTED);
        List<ResumeSellerResponse> resumeAdminResponses = resumeRepository.findAllBySeller(currentUser.getId(), statusList)
                .stream()
                .map(ResumeSellerResponse::from)
                .toList();

        if(resumeAdminResponses.isEmpty()){
            throw new ApiException(ResumeErrorCode.RESUME_NO_CONTENT);
        }

        return resumeAdminResponses;
    }

    public List<ResumeAdminResponse> getFilteredAndPagedResults(
            PeriodCond periodCond, ResumeStatus status,
            PageStep pageStep, Long lastId, int limit
    ){
        return switch(pageStep){
            case FIRST -> resumePageService.findFirstPage(periodCond, status, limit);
            case LAST -> resumePageService.findLastPage(periodCond, status, limit);
            case NEXT -> resumePageService.findNextPage(periodCond, status, lastId, limit);
            case PREVIOUS -> resumePageService.findPreviousPage(periodCond, status, lastId, limit);
        };
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
