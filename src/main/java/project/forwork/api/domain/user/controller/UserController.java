package project.forwork.api.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.user.controller.model.CurrentUser;
import project.forwork.api.domain.user.controller.model.UserCreateRequest;
import project.forwork.api.domain.user.controller.model.UserResponse;
import project.forwork.api.domain.user.service.UserService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "UserController", description = "유저 서비스 컨트롤러")
public class UserController {

    private final UserService userService;

    @Operation(summary = "My Page API", description = "현재 나의 정보 출력")
    @GetMapping("/me")
    public Api<UserResponse> me(
            @Parameter(hidden = true) @Current CurrentUser user
    ){
        log.info("currentuser={}",user);
        UserResponse userResponse = userService.getById(user.getId());
        return Api.OK(userResponse);
    }
}
