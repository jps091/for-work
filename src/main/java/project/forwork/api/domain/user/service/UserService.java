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
import project.forwork.api.common.infrastructure.Producer;
import project.forwork.api.common.infrastructure.message.NoticeMessage;
import project.forwork.api.common.service.port.MailSender;
import project.forwork.api.common.service.port.RedisUtils;
import project.forwork.api.common.service.port.UuidHolder;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.cart.service.port.CartRepository;
import project.forwork.api.domain.resume.service.ResumeService;
import project.forwork.api.domain.token.service.TokenHeaderService;
import project.forwork.api.domain.user.controller.model.*;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.Objects;

@Service
@Builder
@RequiredArgsConstructor
public class UserService {

    public static final String EMAIL_PREFIX = "email:";

    private final UserRepository userRepository;
    private final TokenHeaderService tokenHeaderService;
    private final ResumeService resumeService;
    private final CartRepository cartRepository;
    private final MailSender mailSender;
    private final UuidHolder uuidHolder;
    private final RedisUtils redisUtils;
    private final Producer producer;

    @Transactional
    public User register(UserCreateRequest body){

        if(userRepository.findByEmail(body.getEmail()).isPresent()){
            throw new ApiException(UserErrorCode.EMAIL_DUPLICATION);
        }

        User user = User.from(body);
        user = userRepository.save(user);

        Cart cart = Cart.create(user);
        cartRepository.save(cart);

        produceNoticeMessage(user);
        return user;
    }

    @Transactional
    public void updatePassword(
            CurrentUser currentUser, PasswordModifyRequest body
    ){
        User user = userRepository.getByIdWithThrow(currentUser.getId());
        user = user.updatePassword(body.getPassword());
        userRepository.save(user);
    }

    @Transactional
    public void delete(
            @Current CurrentUser currentUser,
            HttpServletResponse response
    ){
        User user = userRepository.getByIdWithThrow(currentUser.getId());
        tokenHeaderService.expiredRefreshTokenAndHeaders(currentUser.getId(), response);

        resumeService.deleteAll(currentUser);
        cartRepository.delete(user.getId());

        user = user.delete(uuidHolder);
        userRepository.save(user);
    }

    public void verifyPassword(CurrentUser currentUser, PasswordVerifyRequest body){
        User user = userRepository.getByIdWithThrow(currentUser.getId());
        if(user.isPasswordMismatch(body.getPassword())){
            throw new ApiException(UserErrorCode.PASSWORD_NOT_MATCH);
        }
    }

    @Transactional(readOnly = true)
    public User getByIdWithThrow(long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, id));
    }

    public void sendCode(String email){
        String certificationCode = issueCertificationCode(email);
        String title = "for-work 인증 코드 발송";
        String content = "인증코드 : " + certificationCode;
        mailSender.send(email, title, content);
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

    private String issueCertificationCode(String email){
        String certificationCode = uuidHolder.random();
        String key = getKeyByEmail(email);
        redisUtils.setDataWithTimeout(key, certificationCode, 300L);

        return certificationCode;
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
