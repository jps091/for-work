package project.forwork.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import project.forwork.api.common.error.ErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.Objects;

import static project.forwork.api.interceptor.AuthorizationInterceptor.USER_ID;

@Component
@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (HttpMethod.OPTIONS.matches(request.getMethod()) || handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        RequestAttributes requestContext = RequestContextHolder.getRequestAttributes();
        Object userId = requestContext.getAttribute(USER_ID, RequestAttributes.SCOPE_REQUEST);

        Objects.requireNonNull(userId, ()->{throw new ApiException(ErrorCode.NULL_POINT);});

        User user = userRepository.getByIdWithThrow(Long.parseLong(userId.toString()));
        if(user.isAdminMismatch()){
            throw new ApiException(ErrorCode.FORBIDDEN);
        }

        return true;
    }
}
