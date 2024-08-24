package project.forwork.api.common.service;

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

@Service
@RequiredArgsConstructor
public class CookieService {

    public static final String ACCESS_TOKEN = "accessToken";
    public static final String CSRF_TOKEN = "csrfToken";
    private final TokenService tokenService;

    public void createCookies(HttpServletResponse response, User loginUser) {
        TokenResponse token = tokenService.issueToken(loginUser);
        createCookie(response, token.getAccessToken(), ACCESS_TOKEN, true);
        createCookie(response, token.getCsrfToken(), CSRF_TOKEN, false);
    }

    public void addCookies(HttpServletResponse response, TokenResponse tokenResponse) {
        createCookie(response, tokenResponse.getAccessToken(), ACCESS_TOKEN, true);
        createCookie(response, tokenResponse.getCsrfToken(), CSRF_TOKEN, false);
    }

    private void createCookie(HttpServletResponse response, String tokenValue, String tokenKey, boolean httpOnly) {
        Cookie cookie = new Cookie(tokenKey, tokenValue);
        cookie.setPath("/");
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
    }

    public void expiredCookies(HttpServletRequest request, HttpServletResponse response) {
        deleteCookie(request, response, ACCESS_TOKEN);
        deleteCookie(request, response, CSRF_TOKEN);
    }

    private void deleteCookie(HttpServletRequest request, HttpServletResponse response, String tokenKey) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tokenKey.equals(cookie.getName())) {
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    cookie.setValue("");
                    response.addCookie(cookie);
                }
            }
        }
    }

    public String extractTokenFromCookies(HttpServletRequest request, String tokenKey) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tokenKey.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new ApiException(TokenErrorCode.INVALID_TOKEN);
    }
}
