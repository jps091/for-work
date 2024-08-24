package project.forwork.api.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.user.controller.model.UserCreateRequest;
import project.forwork.api.domain.user.controller.model.UserResponse;
import project.forwork.api.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/users")
@Tag(name = "OpenController", description = "개방된 서비스 컨트롤롤러")
public class UserController {

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
}
