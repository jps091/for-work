package project.forwork.api.common.config.swageer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@RequiredArgsConstructor
@OpenAPIDefinition(
        info = @Info(title = "For-work Service Api 명세서",
                description = "Spring Boot 기반 RESTful Api",
                version = "v1.0.0")
)
public class SwaggerConfig {
    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper){
        return new ModelResolver(objectMapper);
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public GroupedOpenApi forWorkOpenApi(){
        String[] paths = {"/open-api/**"};
        return GroupedOpenApi.builder()
                .group("전체 사용자를 위한 For-work Service 도메인 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    @Order(Integer.MIN_VALUE + 1)
    public GroupedOpenApi forWorkLoginApi(){
        String[] paths = {"/api/**"};
        return GroupedOpenApi.builder()
                .group("로그인 사용자를 위한 For-work Service 도메인 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    @Order(Integer.MAX_VALUE)
    public GroupedOpenApi forWorkAdminApi(){
        String[] paths = {"/admin-api/**"};
        return GroupedOpenApi.builder()
                .group("관리자를 위한 For-work Service 도메인 API")
                .pathsToMatch(paths)
                .build();
    }
}
