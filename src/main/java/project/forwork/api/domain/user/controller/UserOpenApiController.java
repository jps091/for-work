package project.forwork.api.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.ApiErrorCode;
import project.forwork.api.common.api.Api;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.domain.user.controller.model.*;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.LoginService;
import project.forwork.api.domain.user.service.UserService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/v1/users")
@Tag(name = "OpenController", description = "개방된 서비스 컨트롤롤러")
public class UserOpenApiController {

    private final UserService userService;
    private final LoginService loginService;

    @Operation(summary = "회원 등록 API", description = "ID, 패스워드, 이름, 이메일 입력")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiErrorCode(domain = UserErrorCode.class, errorCode = {"EMAIL_DUPLICATION"})
    public Api<UserResponse> register(
            @Valid @RequestBody
            UserCreateRequest createRequest
    ){
        User user = userService.register(createRequest);
        UserErrorCode emailNotFound = UserErrorCode.EMAIL_NOT_FOUND;
        return Api.CREATED(UserResponse.from(user));
    }

    @Operation(summary = "이메일 인증코드 발송 API", description = "이메일 입력")
    @PostMapping("/code/send")
    public Api<String> sendCertificationCode(
            @RequestParam @Email(message = "유효한 이메일 주소를 입력해주세요.")
            String email
    ){
        //userService.sendCode(email);
        userService.produceVerifyEmail(email);
        return Api.OK("인증 코드 이메일 전송 완료");
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Api.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 이메일 인증 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Api.class),
                            examples = {
                                    @ExampleObject(name = "BAD_REQUEST",
                                            value = "{ \"result\": { \"resultCode\": 1402, \"resultMessage\": \"잘못된 요청.\", \"resultDescription\": \"잘못된 요청.\" }, \"body\": null }"),
                                    @ExampleObject(name = "EMAIL_VERIFY_FAIL",
                                            value = "{ \"result\": { \"resultCode\": 1406, \"resultMessage\": \"이메일 인증코드가 일치 하지 않습니다.\", \"resultDescription\": \"이메일 인증코드가 일치 하지 않습니다.\" }, \"body\": null }")
                            })),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Api.class)))
    })
    @Operation(summary = "인증 코드 검증 API", description = "이메일, 받은 검증코드 입력")
    @PostMapping("/code/verify")
    public Api<String> verifyEmail(
            @Valid @RequestBody
            EmailVerifyRequest emailVerifyRequest
    ){
        userService.verifyEmail(emailVerifyRequest);
        return Api.OK("인증코드 검증 성공");
    }

    @Operation(summary = "회원 로그인 API",
            description = "테스트용 관리자 : ID = admin@test.com / PW = admin1234@<br>" +
                    "테스트용 일반 회원 : ID = user@test.com / PW = for1234@")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Api.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 이메일 인증 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Api.class),
                            examples = {
                                    @ExampleObject(name = "BAD_REQUEST",
                                            value = "{ \"result\": { \"resultCode\": 1402, \"resultMessage\": \"잘못된 요청.\", \"resultDescription\": \"잘못된 요청.\" }, \"body\": null }"),
                                    @ExampleObject(name = "EMAIL_VERIFY_FAIL",
                                            value = "{ \"result\": { \"resultCode\": 1406, \"resultMessage\": \"이메일 인증코드가 일치 하지 않습니다.\", \"resultDescription\": \"이메일 인증코드가 일치 하지 않습니다.\" }, \"body\": null }")
                            })),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Api.class)))
    })
    @PostMapping("/login")
    public Api<LoginResponse> login(
            @Valid @RequestBody
            UserLoginRequest userLoginRequest,
            HttpServletResponse response
    ){
        LoginResponse loginResponse = loginService.login(response, userLoginRequest);
        return Api.OK(loginResponse);
    }

    @Operation(summary = "임시 비밀번호 발급 API", description = "계정의 이메일, 성함 입력")
    @PostMapping("/password/issue-temporary")
    public Api<String> initTemporaryPassword(
            @Valid @RequestBody PasswordInitRequest passwordInitRequest
    ){
        loginService.initTemporaryPassword(passwordInitRequest);
        return Api.OK("임시 비밀번호 발급 성공");
    }
}
