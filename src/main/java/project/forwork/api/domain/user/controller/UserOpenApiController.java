package project.forwork.api.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.user.controller.model.UserCreateRequest;
import project.forwork.api.domain.user.controller.model.UserLoginRequest;
import project.forwork.api.domain.user.controller.model.UserResponse;
import project.forwork.api.domain.user.service.UserService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/users")
@Tag(name = "OpenController", description = "개방된 서비스 컨트롤롤러")
public class UserOpenApiController {

    private final UserService userService;

    @Operation(summary = "회원 등록 API", description = "ID, 패스워드, 이름, 이메일 입력")
    @PostMapping("/create")
    public ResponseEntity<Api<UserResponse>> create(
            @Valid
            @RequestBody Api<UserCreateRequest> createRequest
    ){
        UserResponse userResponse = userService.create(createRequest.getBody());
        return ResponseEntity.status(HttpStatus.CREATED).body(Api.CREATED(userResponse));
    }

    @Operation(summary = "회원 로그인 API", description = "ID, 패스워드 입력")
    @PostMapping("/login")
    public ResponseEntity<Api<UserResponse>> login(
            @Valid
            @RequestBody Api<UserLoginRequest> userLoginRequest,
            HttpServletResponse response
    ){
        UserResponse userResponse = userService.login(response, userLoginRequest.getBody());
        return ResponseEntity.status(HttpStatus.CREATED).body(Api.OK(userResponse));
    }
}
