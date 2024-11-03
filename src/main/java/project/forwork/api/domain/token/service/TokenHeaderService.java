package project.forwork.api.domain.token.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import project.forwork.api.common.error.TokenErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.token.controller.model.AccessTokenResponse;
import project.forwork.api.domain.token.model.TokenResponse;
import project.forwork.api.domain.user.model.User;

@Service
@RequiredArgsConstructor
public class TokenHeaderService {
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    private final TokenService tokenService;

    public TokenResponse addTokenToHeaders(HttpServletResponse response, User loginUser) {
        TokenResponse tokenResponse = tokenService.issueTokenResponse(loginUser.getId());
        setHeaderByTokenResponse(response, tokenResponse);
        return tokenResponse;
    }

    public AccessTokenResponse reissueRefreshTokenAndHeaders(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromHeader(request);
        TokenResponse tokenResponse = tokenService.reissueTokenResponse(refreshToken);
        setHeaderByTokenResponse(response, tokenResponse);
        return new AccessTokenResponse(tokenResponse.getAccessToken());
    }

    public void expiredRefreshTokenAndHeaders(
            Long userId,
            HttpServletResponse response
    ){
        tokenService.deleteRefreshToken(userId);
        expireTokensInHeaders(response);
    }

    public void expireTokensInHeaders(HttpServletResponse response) {
        response.setHeader(ACCESS_TOKEN_HEADER, "");
        response.setHeader(REFRESH_TOKEN_HEADER, "");
    }

    public String extractAccessTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(ACCESS_TOKEN_HEADER);
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ApiException(TokenErrorCode.TOKEN_NOT_FOUND);
        }
        return token.substring(7); // "Bearer " 이후의 토큰 값만 반환
    }

    private String extractRefreshTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(REFRESH_TOKEN_HEADER);
        if (token == null) {
            throw new ApiException(TokenErrorCode.TOKEN_NOT_FOUND);
        }
        return token;
    }

    private void setHeaderByTokenResponse(HttpServletResponse response, TokenResponse tokenResponse) {
        response.setHeader(REFRESH_TOKEN_HEADER, tokenResponse.getRefreshToken());
    }
}