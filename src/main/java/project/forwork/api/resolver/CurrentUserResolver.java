package project.forwork.api.resolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import static project.forwork.api.interceptor.AuthorizationInterceptor.USER_ID;

@Component
@Slf4j
@RequiredArgsConstructor
public class CurrentUserResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean annotation = parameter.hasParameterAnnotation(Current.class);
        boolean parameterType = parameter.getParameterType().equals(CurrentUser.class);

        return (annotation && parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        RequestAttributes requestContext = RequestContextHolder.getRequestAttributes();
        Object userId = requestContext.getAttribute(USER_ID, RequestAttributes.SCOPE_REQUEST);

        User user = userRepository.getByIdWithThrow(Long.parseLong(userId.toString()));

        return CurrentUser.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roleType(user.getRoleType())
                .build();
    }
}
