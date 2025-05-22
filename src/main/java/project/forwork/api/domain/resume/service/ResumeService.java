package project.forwork.api.domain.resume.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.forwork.api.common.controller.port.S3Service;
import project.forwork.api.common.producer.Producer;
import project.forwork.api.domain.resume.controller.model.*;
import project.forwork.api.common.infrastructure.enums.PageStep;
import project.forwork.api.domain.resume.infrastructure.enums.PeriodCond;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeCommandPort;
import project.forwork.api.domain.resume.service.port.ResumeQueryPort;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.resumedecision.infrastructure.message.SalesRequestResultMessage;
import project.forwork.api.domain.salespost.service.SalesPostService;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeQueryService resumeQueryService;
    private final S3Service s3Service;

    private final ResumeCommandPort resumeCommandPort;
    private final ResumeQueryPort resumeQueryPort;
    private final SalesPostService salesPostService;
    private final Producer producer;

    @Transactional
    public Resume register(CurrentUser currentUser, ResumeRegisterRequest body, MultipartFile file){
        String descriptionUrl = s3Service.saveFile(file);
        Resume resume = Resume.from(currentUser, body, descriptionUrl);
        return resumeCommandPort.save(resume);
    }

    @Transactional
    public void approve(CurrentUser currentUser, Long resumeId){
        Resume updatedResume = resumeQueryPort.getByIdWithThrow(resumeId);
        updatedResume = resumeCommandPort.approve(updatedResume, currentUser.getId());
        salesPostService.registerSalesPost(updatedResume);
        produceSalesRequestResultMessage(updatedResume, true);
    }

    @Transactional
    public void deny(CurrentUser currentUser, Long resumeId){
        Resume updatedResume = resumeQueryPort.getByIdWithThrow(resumeId);
        updatedResume = resumeCommandPort.deny(updatedResume, currentUser.getId());
        produceSalesRequestResultMessage(updatedResume, false);
    }

    @Transactional
    public ResumeRegisterResponse registerByPresignedUrl(CurrentUser currentUser, ResumeRegisterRequest body, String fileName){
        String presignedUrl = s3Service.generatePresignedUrl(fileName);
        Resume savedResume = Resume.from(currentUser, body);
        savedResume = resumeCommandPort.save(savedResume);
        return new ResumeRegisterResponse(savedResume.getId(), presignedUrl);
    }

    @Transactional
    public void resumeCallback(String filePath, Long resumeId){
        Resume resume = resumeQueryPort.getByIdWithThrow(resumeId);
        resume = resume.callbackDescriptionImageUrl(filePath);
        resumeCommandPort.save(resume);
    }

    @Transactional
    public void modify(
            Long resumeId, CurrentUser currentUser,
            ResumeModifyRequest body, MultipartFile file
    ){
        Resume resume = resumeQueryPort.getByIdWithThrow(resumeId);

        String descriptionUrl = null;
        if(file != null && !file.isEmpty()){
            descriptionUrl = s3Service.saveFile(file);
        }

        resume = resume.modify(currentUser.getId(), body, descriptionUrl);
        resumeCommandPort.save(resume);

        // 만약 이미 승인을 받아서 판매글이 있다면 판매글 상태 변경
        salesPostService.cancelSalesPostIfExistsByResumeId(resumeId);
    }
    @Transactional
    public void delete(Long resumeId, CurrentUser currentUser) {
        Resume resume = resumeQueryPort.getByIdWithThrow(resumeId);
        s3Service.deleteFile(resume.getDescriptionImageUrl());
        resumeCommandPort.delete(resume, currentUser.getId());
    }

    @Transactional
    public void deleteAll(CurrentUser currentUser) {
        List<ResumeStatus> statusList = List.of(ResumeStatus.ACTIVE, ResumeStatus.PENDING, ResumeStatus.REJECTED);
        List<Resume> resumeList = resumeQueryPort.findAllBySeller(currentUser.getId(), statusList);
        if(resumeList.isEmpty()){
            return;
        }
        resumeCommandPort.deleteAll(resumeList, currentUser.getId());
    }

    public ResumePage getFilteredAndPagedResults(
            PeriodCond periodCond, ResumeStatus status,
            PageStep pageStep, Long lastId, int limit
    ){
        return switch(pageStep){
            case FIRST -> resumeQueryService.findFirstPage(periodCond, status, limit);
            case LAST -> resumeQueryService.findLastPage(periodCond, status, limit);
            case NEXT -> resumeQueryService.findNextPage(periodCond, status, lastId, limit);
            case PREVIOUS -> resumeQueryService.findPreviousPage(periodCond, status, lastId, limit);
        };
    }

    private void produceSalesRequestResultMessage(Resume resume, boolean isApprove) {
        SalesRequestResultMessage message = SalesRequestResultMessage.deny(resume.getSellerEmail(), resume.getResumeUrl());
        if(isApprove){
            message = SalesRequestResultMessage.approve(resume.getSellerEmail(), resume.getResumeUrl());
        }
        producer.sendSalesRequestResultMail(message);
    }
}