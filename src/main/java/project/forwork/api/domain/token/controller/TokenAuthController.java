package project.forwork.api.domain.token.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.token.service.TokenHeaderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
@Tag(name = "TokenCookiesController")
public class TokenAuthController {

    private final TokenHeaderService tokenHeaderService;

    @Operation(summary = "Access Token 재발급 API", description = "Refresh Token을 이용해 새로운 Access Token을 발급")
    @PostMapping("/reissue")
    public Api<String> reissueAccessToken(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        String accessToken = tokenHeaderService.reissueRefreshTokenAndHeaders(request, response);
        return Api.OK(accessToken);
    }
}
