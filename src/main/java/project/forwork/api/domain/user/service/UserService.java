package project.forwork.api.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.MailSender;
import project.forwork.api.common.service.port.RedisUtils;
import project.forwork.api.common.service.port.UuidHolder;
import project.forwork.api.domain.user.controller.model.UserCreateRequest;
import project.forwork.api.domain.user.controller.model.UserResponse;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    public static final String EMAIL_PREFIX = "email:";

    private final UserRepository userRepository;
    private final MailSender mailSender;
    private final UuidHolder uuidHolder;
    private final RedisUtils redisUtils;

    @Transactional
    public UserResponse create(UserCreateRequest createRequest){
        User user = User.from(createRequest);
        user = userRepository.save(user);
        return UserResponse.from(user);
    }

    public void sendCode(String email){
        String certificationCode = issueCertificationCode(email);
        String title = "for-work 인증 코드 발송";
        String content = "인증코드 : " + certificationCode;
        mailSender.send(email, title, content);
    }

    public void verifyEmail(String email, String sourceCode){
        String targetCode = redisUtils.getData(getKeyByEmail(email));

        if(isCodeMismatch(sourceCode, targetCode)){
            throw new ApiException(UserErrorCode.EMAIL_VERIFY_FAIL);
        }

        deleteCertificationCode(email);
    }

    @Transactional(readOnly = true)
    public UserResponse getById(long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
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
