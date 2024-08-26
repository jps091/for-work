package project.forwork.api.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.CookieService;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.user.controller.model.UserCreateRequest;
import project.forwork.api.domain.user.controller.model.UserLoginRequest;
import project.forwork.api.domain.user.controller.model.UserResponse;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ClockHolder clockHolder;
    private final CookieService cookieService;

    public UserResponse create(UserCreateRequest createRequest){
        User user = User.from(createRequest);
        user = userRepository.save(user);
        return UserResponse.from(user);
    }

    public UserResponse getById(long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

    public UserResponse login(HttpServletResponse response, UserLoginRequest loginUser){

        User user = userRepository.findByEmail(loginUser.getEmail())
                .orElseThrow(() -> new ApiException(UserErrorCode.EMAIL_NOT_FOUND));

        user = user.login(clockHolder, loginUser.getPassword());
        userRepository.save(user);

        cookieService.createCookies(response, user);

        return UserResponse.from(user);
    }

    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response){
        cookieService.extractTokenFromCookies(request, )
    }
}
