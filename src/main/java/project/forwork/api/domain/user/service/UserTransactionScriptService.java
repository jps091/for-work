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
import project.forwork.api.common.producer.Producer;
import project.forwork.api.common.service.port.RedisUtils;
import project.forwork.api.common.service.port.UuidHolder;
import project.forwork.api.domain.cart.infrastructure.enums.CartStatus;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.cart.service.port.CartRepository;
import project.forwork.api.domain.resume.service.ResumeService;
import project.forwork.api.domain.token.service.TokenHeaderService;
import project.forwork.api.domain.user.controller.model.*;
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;
import project.forwork.api.domain.user.infrastructure.message.AdminInquiryMessage;
import project.forwork.api.domain.user.infrastructure.message.NoticeMessage;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.Objects;

@Service
@Builder
@RequiredArgsConstructor
public class UserTransactionScriptService {

    public static final String EMAIL_PREFIX = "email:";

    private final UserRepository userRepository;
    private final TokenHeaderService tokenHeaderService;
    private final ResumeService resumeService;
    private final CartRepository cartRepository;
    private final UuidHolder uuidHolder;
    private final RedisUtils redisUtils;
    private final Producer producer;

    @Transactional
    public User register(UserCreateRequest body){

        if(userRepository.existsByEmail(body.getEmail())){
            throw new ApiException(UserErrorCode.EMAIL_DUPLICATION);
        }

        User user = User.builder()
                .email(body.getEmail())
                .name(body.getName())
                .password(body.getPassword())
                .status(UserStatus.USER)
                .build();
        user = userRepository.save(user);

        Cart cart = Cart.builder()
                .user(user)
                .status(CartStatus.ACTIVE)
                .build();
        cartRepository.save(cart);

        produceNoticeMessage(user);
        return user;
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

        user = User.builder()
                .email(uuidHolder.random())
                .status(UserStatus.DELETE)
                .build();
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getByIdWithThrow(long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, id));
    }

    private void produceNoticeMessage(User user) {
        NoticeMessage message = NoticeMessage.from(user.getEmail());
        producer.sendNotice(message);
    }
}
