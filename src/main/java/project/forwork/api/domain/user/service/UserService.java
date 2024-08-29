package project.forwork.api.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.user.controller.model.UserCreateRequest;
import project.forwork.api.domain.user.controller.model.UserResponse;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse create(UserCreateRequest createRequest){
        User user = User.from(createRequest);
        user = userRepository.save(user);
        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getById(long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }
}
