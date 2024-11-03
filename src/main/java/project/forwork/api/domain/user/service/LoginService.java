package project.forwork.api.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.common.service.port.RedisUtils;
import project.forwork.api.domain.token.model.TokenResponse;
import project.forwork.api.domain.token.service.TokenHeaderService;
import project.forwork.api.domain.user.controller.model.LoginResponse;
import project.forwork.api.domain.user.controller.model.PasswordInitRequest;
import project.forwork.api.domain.user.controller.model.UserLoginRequest;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

@Slf4j
@Builder
@Service
@RequiredArgsConstructor
public class LoginService {
    private static final String LOGIN_ATTEMPT_KEY_PREFIX = "loginAttempt:userId:";
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private final UserRepository userRepository;
    private final TokenHeaderService tokenHeaderService;
    private final ClockHolder clockHolder;
    private final RedisUtils redisUtils;
    private final PasswordInitializationService passwordInitializationService;

    @Transactional
    public LoginResponse login(HttpServletResponse response, UserLoginRequest loginUser){

        User user = userRepository.findByEmail(loginUser.getEmail())
                .orElseThrow(() -> new ApiException(UserErrorCode.EMAIL_NOT_FOUND));

        loginAttempt(user);

        user = user.login(clockHolder, loginUser.getPassword());
        userRepository.save(user);

        TokenResponse tokenResponse = tokenHeaderService.addTokenToHeaders(response, user);

        String key = getKeyByLoginAttempt(user);
        initLoginAttemptCount(key);

        return LoginResponse.from(user.getId(), tokenResponse);
    }

    public void logout(HttpServletResponse response){
        tokenHeaderService.expireTokensInHeaders(response);
    }

    @Transactional
    public void initTemporaryPassword(PasswordInitRequest body){
        User user = userRepository.findByEmail(body.getEmail())
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        if(user.isNameMismatch(body.getName())){
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        }

        passwordInitializationService.issueTemporaryPassword(user);
    }

    private void loginAttempt(User user){
        String key = getKeyByLoginAttempt(user);
        Long loginCount = redisUtils.incrementDataInitTimeOut(key, 600);
        log.info("login count={}", loginCount);

        if(isExceedMaxCount(loginCount)){
            passwordInitializationService.issueTemporaryPassword(user);
            initLoginAttemptCount(key);
            throw new ApiException(UserErrorCode.PASSWORD_ISSUE);
        }
    }

    private void initLoginAttemptCount(String key) {
        redisUtils.deleteData(key);
    }

    private static boolean isExceedMaxCount(Long loginCount) {
        return loginCount > MAX_LOGIN_ATTEMPTS;
    }

    private String getKeyByLoginAttempt(User user) {
        return redisUtils.createKeyForm(LOGIN_ATTEMPT_KEY_PREFIX, user.getId());
    }
}
