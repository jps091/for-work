package project.forwork.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.CookieService;
import project.forwork.api.domain.token.service.TokenService;

import java.util.Objects;


@Component
@Slf4j
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    public static final String USER_ID = "userId";
    private final TokenService tokenService;
    private final CookieService cookieService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        RequestAttributes requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());

        try {
            String accessToken = cookieService.extractTokenFromCookies(request, CookieService.ACCESS_TOKEN);
            Long userId = tokenService.validationToken(accessToken);
            requestContext.setAttribute(USER_ID, userId, RequestAttributes.SCOPE_REQUEST);
            log.info("interceptor={}",userId);
            return true;
        } catch (ApiException e) {
            log.error("AuthorizationInterceptor Token Error : ", e);
            throw e;
        }
    }
}
