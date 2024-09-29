package project.forwork.api.domain.token.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.forwork.api.common.error.TokenErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.token.model.TokenResponse;
import project.forwork.api.domain.user.model.User;

@Service
@RequiredArgsConstructor
public class TokenHeaderService {
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    private final TokenService tokenService;

    public TokenResponse addTokenToHeaders(HttpServletResponse response, User loginUser) {
        TokenResponse tokenResponse = tokenService.issueTokenResponse(loginUser);
        setHeaderByTokenResponse(response, tokenResponse);

        return tokenResponse;
    }

    public void expireTokensInHeaders(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(ACCESS_TOKEN_HEADER, "");
        response.setHeader(REFRESH_TOKEN_HEADER, "");
    }

    public void expiredRefreshTokenAndHeaders(
            Long userId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        tokenService.deleteRefreshToken(userId);
        expireTokensInHeaders(request, response);
    }

    public void reissueRefreshTokenAndHeaders(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractTokenFromHeader(request, REFRESH_TOKEN_HEADER);
        TokenResponse tokenResponse = tokenService.reissueTokenResponse(refreshToken);
        setHeaderByTokenResponse(response, tokenResponse);
    }

    public String extractTokenFromHeader(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ApiException(TokenErrorCode.TOKEN_NOT_FOUND);
        }
        return token.substring(7); // "Bearer " 이후의 토큰 값만 반환
    }

    private void setHeaderByTokenResponse(HttpServletResponse response, TokenResponse tokenResponse) {
        response.setHeader(ACCESS_TOKEN_HEADER, "Bearer " + tokenResponse.getAccessToken());
        response.setHeader(REFRESH_TOKEN_HEADER, "Bearer " + tokenResponse.getRefreshToken());
    }
}
