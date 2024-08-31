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
import project.forwork.api.domain.user.controller.model.CurrentUser;
import project.forwork.api.domain.user.controller.model.ModifyPasswordRequest;
import project.forwork.api.domain.user.controller.model.UserResponse;
import project.forwork.api.domain.user.service.LoginService;
import project.forwork.api.domain.user.service.UserService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "UserController", description = "유저 서비스 컨트롤러")
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    @Operation(summary = "My Page API", description = "현재 나의 정보 출력")
    @GetMapping("/mypage")
    public Api<UserResponse> me(
            @Parameter(hidden = true) @Current CurrentUser user
    ){
        UserResponse userResponse = userService.getById(user.getId());
        return Api.OK(userResponse);
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

    @Operation(summary = "회원 Email 수정 API", description = "수정할 Email 입력")
    @PutMapping
    public Api<String> modifyPassword(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @Valid @RequestBody ModifyPasswordRequest modifyPasswordRequest
    ){
        userService.modifyPassword(currentUser, modifyPasswordRequest);
        return Api.OK("비밀번호 수정 성공");
    }

    @Operation(summary = "회원 탈퇴 API", description = "패스워드 입력")
    @DeleteMapping
    public Api<String> deleteUser(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        userService.delete(currentUser, password, request, response);
        return Api.OK("회원 탈퇴 성공");
    }
}
