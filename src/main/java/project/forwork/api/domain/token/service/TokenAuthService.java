package project.forwork.api.domain.token.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.forwork.api.common.error.TokenErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.token.model.TokenResponse;
import project.forwork.api.domain.user.model.User;

import java.util.Arrays;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenAuthService {

    private final TokenService tokenService;
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN = "refreshToken";

    public String getRefreshTokenAndIssueToken(HttpServletResponse response, User loginUser) {
        TokenResponse tokenResponse = tokenService.issueTokenResponse(loginUser);
        registerAccessTokenWithHeader(response, tokenResponse);
        registerRefreshTokenWithCookie(response, tokenResponse);
        return tokenResponse.getAccessToken();
    }

    public String reissueAccessToken(HttpServletRequest request, HttpServletResponse response){
        String refreshToken = extractRefreshTokenFromCookies(request);
        log.info("refreshToken={}",refreshToken);
        TokenResponse tokenResponse = tokenService.reissueTokenResponse(refreshToken);
        registerRefreshTokenWithCookie(response, tokenResponse);
        return tokenResponse.getAccessToken();
    }

    public String extractTokenFromHeader(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ApiException(TokenErrorCode.TOKEN_NOT_FOUND);
        }
        return token.substring(7); // "Bearer " 이후의 토큰 값만 반환
    }

    public void expireTokens(HttpServletRequest request, HttpServletResponse response) {
        deleteAccessTokensInHeaders(response);
        deleteRefreshTokenInCookies(request, response);
    }

    public void deleteTokensWithUserDelete(
            Long userId,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        tokenService.deleteRefreshToken(userId);
        deleteAccessTokensInHeaders(response);
        deleteRefreshTokenInCookies(request, response);
    }

    private void registerAccessTokenWithHeader(HttpServletResponse response, TokenResponse tokenResponse) {
        response.setHeader(ACCESS_TOKEN_HEADER, "Bearer " + tokenResponse.getAccessToken());
    }

    private void registerRefreshTokenWithCookie(HttpServletResponse response, TokenResponse tokenResponse){
        Cookie cookie = new Cookie(REFRESH_TOKEN, tokenResponse.getRefreshToken());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        // 3. SameSite=None 속성을 포함한 Set-Cookie 헤더 추가
        String cookieHeader = String.format("%s=%s; Path=/; HttpOnly; Secure; SameSite=None",
                REFRESH_TOKEN, tokenResponse.getRefreshToken());
        response.addHeader("Set-Cookie", cookieHeader);
    }

    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)  // Cookie[] -> Stream<Cookie>
                .filter(cookie -> REFRESH_TOKEN.equals(cookie.getName()))  // tokenKey와 이름이 일치하는 쿠키 필터링
                .map(Cookie::getValue)  // 쿠키의 값을 가져옴
                .findFirst()  // 첫 번째 값을 Optional로 반환
                .orElseThrow(() -> new ApiException(TokenErrorCode.TOKEN_NOT_FOUND));  // 값이 없으면 예외 발생
    }

    private void deleteAccessTokensInHeaders(HttpServletResponse response) {
        response.setHeader(ACCESS_TOKEN_HEADER, "");
    }

    private void deleteRefreshTokenInCookies(HttpServletRequest request, HttpServletResponse response) {
        Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)  // Cookie[] -> Stream<Cookie>
                .filter(cookie -> REFRESH_TOKEN.equals(cookie.getName()))  // tokenKey와 이름이 일치하는 쿠키 필터링
                .forEach(cookie -> {  // 각 쿠키에 대해 아래 작업 수행
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    cookie.setValue("");
                    response.addCookie(cookie);
                });
    }
}
