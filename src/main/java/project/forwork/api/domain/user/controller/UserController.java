package project.forwork.api.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.user.controller.model.PasswordModifyRequest;
import project.forwork.api.domain.user.controller.model.PasswordVerifyRequest;
import project.forwork.api.domain.user.controller.model.UserResponse;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.LoginService;
import project.forwork.api.domain.user.service.UserService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "UserController", description = "유저 서비스 컨트롤러")
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    @Operation(summary = "My Page API", description = "현재 나의 정보 출력")
    @GetMapping("/me")
    public Api<UserResponse> me(
            @Parameter(hidden = true) @Current CurrentUser currentUser
    ){
        User user = userService.getByIdWithThrow(currentUser.getId());
        return Api.OK(UserResponse.from(user));
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃")
    @PostMapping("/logout")
    public Api<String> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        loginService.logout(request, response);
        return Api.OK("로그아웃 성공");
    }

    @Operation(summary = "회원 비밀번호 검증 API", description = "비밀번호 입력")
    @PostMapping("/verify-password")
    public Api<String> verifyPassword(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @Valid @RequestBody PasswordVerifyRequest passwordVerifyRequest
    ){
        userService.verifyPassword(currentUser, passwordVerifyRequest);
        return Api.OK("비밀번호 검증 성공");
    }

    @Operation(summary = "회원 비밀번호 수정 API", description = "비밀번호 검증 성공후 호출, 수정할 비밀번호 입력")
    @PutMapping("/password")
    public Api<String> updatePassword(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @Valid @RequestBody PasswordModifyRequest passwordModifyRequest
    ){
        userService.updatePassword(currentUser, passwordModifyRequest);
        return Api.OK("비밀번호 수정 성공");
    }

    @Operation(summary = "회원 탈퇴 API", description = "비밀번호 검증 성공후 호출, 패스워드 입력")
    @DeleteMapping
    public Api<String> delete(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        userService.delete(currentUser, request, response);
        return Api.OK("회원 탈퇴 성공");
    }
}
