package project.forwork.api.common.config.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.forwork.api.interceptor.AdminInterceptor;
import project.forwork.api.interceptor.AuthorizationInterceptor;
import project.forwork.api.resolver.UserCookieResolver;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizationInterceptor authorizationInterceptor;
    private final AdminInterceptor adminInterceptor;
    private final UserCookieResolver userCookieResolver;

    private List<String> API = List.of(
            "/api/**"
    );

    private List<String> OPEN_API = List.of(
            "/open-api/**"
    );

    private List<String> ADMIN_API = List.of(
            "/admin-api/**"
    );

    private List<String> DEFAULT_EXCLUDE = List.of(
            "/",
            "/home",
            "/health",
            "/favicon.ico",
            "/css/**",
            "/error"
    );

    private List<String> SWAGGER = List.of(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor)
                .excludePathPatterns(OPEN_API)
                .excludePathPatterns(DEFAULT_EXCLUDE)
                .excludePathPatterns(SWAGGER);

        // AdminInterceptor 등록
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns(ADMIN_API)  // /api/admin/** 경로에만 적용
                .excludePathPatterns(API)    // /api/** 경로는 제외
                .excludePathPatterns(OPEN_API)
                .excludePathPatterns(DEFAULT_EXCLUDE)
                .excludePathPatterns(SWAGGER);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userCookieResolver);
    }
}
