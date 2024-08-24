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
import project.forwork.api.domain.user.controller.model.CurrentUser;
import project.forwork.api.domain.user.controller.model.UserResponse;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.service.UserService;

import static project.forwork.api.interceptor.AuthorizationInterceptor.USER_ID;
import static project.forwork.api.domain.token.helper.JwtTokenHelper.ROLE_TYPE;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserCookieResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

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
        UserResponse userResponse = userService.getById(Long.parseLong(userId.toString()));

        return CurrentUser.builder()
                .id(userResponse.getId())
                .name(userResponse.getName())
                .email(userResponse.getEmail())
                .roleType(userResponse.getRoleType())
                .build();
    }
}
