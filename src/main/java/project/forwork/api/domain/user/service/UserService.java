package project.forwork.api.domain.user.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.user.infrastructure.message.AdminInquiryMessage;
import project.forwork.api.common.producer.Producer;
import project.forwork.api.domain.user.infrastructure.message.NoticeMessage;
import project.forwork.api.common.service.port.RedisUtils;
import project.forwork.api.common.service.port.UuidHolder;
import project.forwork.api.domain.resume.service.ResumeService;
import project.forwork.api.domain.token.service.TokenHeaderService;
import project.forwork.api.domain.user.controller.model.*;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserCommandPort;
import project.forwork.api.domain.user.service.port.UserQueryPort;

import java.util.Objects;

@Service
@Builder
@RequiredArgsConstructor
public class UserService {

    public static final String EMAIL_PREFIX = "email:";

    private final UserQueryPort userQueryPort;
    private final UserCommandPort userCommandPort;
    private final TokenHeaderService tokenHeaderService;
    private final ResumeService resumeService;
    private final UuidHolder uuidHolder;
    private final RedisUtils redisUtils;
    private final Producer producer;

    @Transactional
    public User register(UserCreateRequest body){

        if(userQueryPort.existsByEmail(body.getEmail())){
            throw new ApiException(UserErrorCode.EMAIL_DUPLICATION);
        }

        User registeredUser = User.from(body);
        userCommandPort.register(registeredUser);
        produceNoticeMessage(registeredUser);
        return registeredUser;
    }

    @Transactional
    public void updatePassword(
            CurrentUser currentUser, PasswordModifyRequest body
    ){
        User user = userQueryPort.getByIdWithThrow(currentUser.getId());
        user = user.updatePassword(body.getPassword());
        userCommandPort.update(user);
    }

    @Transactional
    public void delete(
            @Current CurrentUser currentUser,
            HttpServletResponse response
    ){
        User user = userQueryPort.getByIdWithThrow(currentUser.getId());
        tokenHeaderService.expiredRefreshTokenAndHeaders(currentUser.getId(), response);
        resumeService.deleteAll(currentUser);
        user = user.delete(uuidHolder);
        userCommandPort.delete(user);
    }

    public void verifyPassword(CurrentUser currentUser, PasswordVerifyRequest body){
        User user = userQueryPort.getByIdWithThrow(currentUser.getId());
        if(user.isPasswordMismatch(body.getPassword())){
            throw new ApiException(UserErrorCode.PASSWORD_NOT_MATCH);
        }
    }

    @Transactional(readOnly = true)
    public User getByIdWithThrow(long id){
        return userQueryPort.getByIdWithThrow(id);
    }

    public void produceVerifyEmail(String email){
        producer.sendAutCodeMail(email);
    }

    public void verifyEmail(EmailVerifyRequest body){
        String targetCode = redisUtils.getData(getKeyByEmail(body.getEmail()));

        if(isCodeMismatch(body.getCode(), targetCode)){
            throw new ApiException(UserErrorCode.EMAIL_VERIFY_FAIL);
        }

        deleteCertificationCode(body.getEmail());
    }

    public void produceInquiryEmail(CurrentUser currentUser, InquiryRequest body){
        AdminInquiryMessage message = AdminInquiryMessage.from(currentUser.getEmail(), body);
        producer.sendAdminInquiry(message);
    }

    private String getKeyByEmail(String email) {
        return redisUtils.createKeyForm(EMAIL_PREFIX, email);
    }

    private static boolean isCodeMismatch(String sourceCode, String targetCode) {
        return !Objects.equals(sourceCode, targetCode);
    }

    private void produceNoticeMessage(User user) {
        NoticeMessage message = NoticeMessage.from(user.getEmail());
        producer.sendNotice(message);
    }

    private void deleteCertificationCode(String email) {
        redisUtils.deleteData(getKeyByEmail(email));
    }
}
