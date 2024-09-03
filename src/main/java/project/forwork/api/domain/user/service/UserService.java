package project.forwork.api.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.MailSender;
import project.forwork.api.common.service.port.RedisUtils;
import project.forwork.api.common.service.port.UuidHolder;
import project.forwork.api.domain.token.service.TokenCookieService;
import project.forwork.api.domain.user.controller.model.*;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    public static final String EMAIL_PREFIX = "email:";

    private final UserRepository userRepository;
    private final TokenCookieService tokenCookieService;
    private final MailSender mailSender;
    private final UuidHolder uuidHolder;
    private final RedisUtils redisUtils;

    @Transactional
    public UserResponse register(UserCreateRequest createRequest){
        User user = User.from(createRequest);
        user = userRepository.save(user);
        return UserResponse.from(user);
    }
    @Transactional
    public void modifyPassword(CurrentUser currentUser, ModifyPasswordRequest modifyPasswordRequest){
        User user = getUserByCurrentUser(currentUser);
        user = user.modifyPassword(modifyPasswordRequest.getPassword());
        userRepository.save(user);
    }

    @Transactional
    public void delete(
            @Current CurrentUser currentUser,
            String password,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        User user = getUserByCurrentUser(currentUser);
        if(user.isPasswordMismatch(password)){
            throw new ApiException(UserErrorCode.PASSWORD_NOT_MATCH);
        }

        tokenCookieService.expiredCookiesAndRefreshToken(user.getId(), request, response);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getByIdWithThrow(long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, id));
        return UserResponse.from(user);
    }

    public void sendCode(String email){
        String certificationCode = issueCertificationCode(email);
        String title = "for-work 인증 코드 발송";
        String content = "인증코드 : " + certificationCode;
        mailSender.send(email, title, content);
    }

    public void verifyEmail(EmailVerifyRequest emailVerifyRequest){
        String targetCode = redisUtils.getData(getKeyByEmail(emailVerifyRequest.getEmail()));

        if(isCodeMismatch(emailVerifyRequest.getCode(), targetCode)){
            throw new ApiException(UserErrorCode.EMAIL_VERIFY_FAIL);
        }

        deleteCertificationCode(emailVerifyRequest.getEmail());
    }

    private User getUserByCurrentUser(CurrentUser currentUser) {
        return userRepository.getByIdWithThrow(currentUser.getId());
    }

    private String issueCertificationCode(String email){
        String certificationCode = uuidHolder.random();
        String key = getKeyByEmail(email);
        redisUtils.setData(key, certificationCode, 300L);

        return certificationCode;
    }

    private String getKeyByEmail(String email) {
        return redisUtils.createKeyForm(EMAIL_PREFIX, email);
    }

    private static boolean isCodeMismatch(String sourceCode, String targetCode) {
        return !Objects.equals(sourceCode, targetCode);
    }

    private void deleteCertificationCode(String email) {
        redisUtils.deleteData(getKeyByEmail(email));
    }
}
