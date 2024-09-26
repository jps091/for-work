package project.forwork.api.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.api.Api;
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
    public Api<UserResponse> register(
            @Valid @RequestBody
            UserCreateRequest createRequest
    ){
        User user = userService.register(createRequest);
        return Api.CREATED(UserResponse.from(user));
    }

    @Operation(summary = "이메일 인증코드 발송 API", description = "이메일 입력")
    @PostMapping("/code/send")
    public Api<String> sendCertificationCode(
            @RequestParam String email
    ){
        userService.sendCode(email);
        return Api.OK("인증 코드 이메일 전송 완료");
    }

    @Operation(summary = "인증 코드 검증 API", description = "이메일, 받은 검증코드 입력")
    @PostMapping("/code/verify")
    public Api<String> verifyEmail(
            @Valid @RequestBody
            EmailVerifyRequest emailVerifyRequest
    ){
        userService.verifyEmail(emailVerifyRequest);
        return Api.OK("인증코드 검증 성공");
    }

    @Operation(summary = "회원 로그인 API", description = "ID, 패스워드 입력")
    @PostMapping("/login")
    public Api<UserResponse> login(
            @Valid @RequestBody
            UserLoginRequest userLoginRequest,
            HttpServletResponse response
    ){
        UserResponse userResponse = loginService.login(response, userLoginRequest);
        return Api.OK(userResponse);
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
