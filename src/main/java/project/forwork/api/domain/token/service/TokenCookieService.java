package project.forwork.api.domain.token.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.forwork.api.common.error.TokenErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.token.model.TokenResponse;
import project.forwork.api.domain.token.service.TokenService;
import project.forwork.api.domain.user.model.User;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenCookieService {

    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    private final TokenService tokenService;

    public void createCookies(HttpServletResponse response, User loginUser) {
        TokenResponse tokenResponse = tokenService.issueTokenResponse(loginUser);
        createCookieByTokenResponse(response, tokenResponse);
    }

    public void addCookies(HttpServletResponse response, TokenResponse tokenResponse) {
        createCookieByTokenResponse(response, tokenResponse);
    }

    public void expiredCookies(HttpServletRequest request, HttpServletResponse response) {
        deleteCookie(request, response, ACCESS_TOKEN);
        deleteCookie(request, response, REFRESH_TOKEN);
    }

    public String extractTokenFromCookies(HttpServletRequest request, String tokenKey) {
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)  // Cookie[] -> Stream<Cookie>
                .filter(cookie -> tokenKey.equals(cookie.getName()))  // tokenKey와 이름이 일치하는 쿠키 필터링
                .map(Cookie::getValue)  // 쿠키의 값을 가져옴
                .findFirst()  // 첫 번째 값을 Optional로 반환
                .orElseThrow(() -> new ApiException(TokenErrorCode.INVALID_TOKEN));  // 값이 없으면 예외 발생
    }


    private void createCookieByTokenResponse(HttpServletResponse response, TokenResponse tokenResponse){
        createCookie(response, tokenResponse.getAccessToken(), ACCESS_TOKEN);
        createCookie(response, tokenResponse.getRefreshToken(), REFRESH_TOKEN);
    }

    private void createCookie(HttpServletResponse response, String tokenValue, String tokenKey){
        Cookie cookie = new Cookie(tokenKey, tokenValue);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
    }

    private void deleteCookie(HttpServletRequest request, HttpServletResponse response, String tokenKey) {
        Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)  // Cookie[] -> Stream<Cookie>
                .filter(cookie -> tokenKey.equals(cookie.getName()))  // tokenKey와 이름이 일치하는 쿠키 필터링
                .forEach(cookie -> {  // 각 쿠키에 대해 아래 작업 수행
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    cookie.setValue("");
                    response.addCookie(cookie);
                });
    }
}
