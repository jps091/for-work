package project.forwork.api.common.config.swageer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.models.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.HandlerMethod;
import project.forwork.api.common.annotation.ApiErrorCode;
import project.forwork.api.common.api.Api;
import project.forwork.api.common.error.ErrorCode;
import project.forwork.api.common.error.ErrorCodeIfs;
import project.forwork.api.common.exception.ApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Configuration
@Slf4j
@RequiredArgsConstructor
@OpenAPIDefinition(
        info = @Info(title = "For-work Service Api 명세서",
                description = "Spring Boot 기반 RESTful Api",
                version = "V1.0.0"),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
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
                .addOperationCustomizer(customizer())
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    @Order(Integer.MIN_VALUE + 1)
    public GroupedOpenApi forWorkLoginApi(){
        String[] paths = {"/api/**"};
        return GroupedOpenApi.builder()
                .group("로그인 사용자를 위한 For-work Service 도메인 API")
                .addOperationCustomizer(customizer())
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    @Order(Integer.MAX_VALUE)
    public GroupedOpenApi forWorkAdminApi(){
        String[] paths = {"/admin-api/**"};
        return GroupedOpenApi.builder()
                .group("관리자를 위한 For-work Service 도메인 API")
                .addOperationCustomizer(customizer())
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public OperationCustomizer customizer(){
        log.info("OperationCustomizer 등록됨");
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiErrorCode apiErrorCode = handlerMethod.getMethodAnnotation(ApiErrorCode.class);
            if(apiErrorCode != null){
                generateErrorCodeResponseExample(operation, apiErrorCode.domain(), apiErrorCode.errorCode());
            }
            return operation;
        };
    }

    private void generateErrorCodeResponseExample(
            Operation operation, Class<? extends ErrorCodeIfs> domain, String[] errorCode
    ){
        ApiResponses responses = operation.getResponses();
        ErrorCodeIfs[] errorCodes = domain.getEnumConstants();

        // 필터링된 errorCode 배열을 기반으로, 실제 매칭되는 ErrorCodeIfs를 찾기
        List<ErrorCodeIfs> filteredErrorCodes = Arrays.stream(errorCodes)
                .filter(errorCodeIfs -> Arrays.asList(errorCode).contains(((Enum<?>) errorCodeIfs).name()))
                .toList();

        // 필터링된 errorCode가 없으면 예외 발생
        if (filteredErrorCodes.isEmpty()) {
            throw new ApiException(ErrorCode.SERVER_ERROR);
        }

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders =
                filteredErrorCodes.stream()
                        .map(
                                errorCodeIfs -> {
                                    try{
                                        return ExampleHolder.builder()
                                                .holder(
                                                    getSwaagerExample(errorCodeIfs)
                                                )
                                                .code(errorCodeIfs.getHttpStatusCode())
                                                .name(errorCodeIfs.getErrorCode().toString())
                                                .build();
                                    }catch (NoSuchFieldError e){
                                        throw new RuntimeException(e);
                                    }
                                }
                        )
                        .collect(groupingBy(ExampleHolder::getCode));
        log.info("generateErrorCodeResponseExample {}", statusWithExampleHolders);
        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    private Example getSwaagerExample(ErrorCodeIfs errorCode){
        //ApiException apiException = new ApiException((errorCode));
        Api<Object> errorResponse = Api.ERROR(errorCode);
        Example example = new Example();
        example.description(errorCode.getDescription());
        example.setValue(errorResponse);
        log.info("getSwaagerExample {}", example);
        return example;
    }

    private void addExamplesToResponses(
            ApiResponses apiResponses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders
    ){
        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();
                    v.forEach(
                            exampleHolder -> mediaType.addExamples(
                                    exampleHolder.getName(), exampleHolder.getHolder()
                            )
                    );
                    content.addMediaType("application/json", mediaType);
                    apiResponse.setContent(content);
                    // 상태코드를 key 값으로 responses 에 추가합니다.
                    apiResponses.addApiResponse(status.toString(), apiResponse);
                }
        );
        log.info("addExamplesToResponses {}", statusWithExampleHolders);
    }
}
