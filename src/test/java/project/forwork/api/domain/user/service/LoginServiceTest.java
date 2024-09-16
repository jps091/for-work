package project.forwork.api.domain.user.service;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.RedisUtils;
import project.forwork.api.domain.token.service.TokenCookieService;
import project.forwork.api.domain.user.controller.model.UserLoginRequest;
import project.forwork.api.domain.user.controller.model.UserResponse;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.FakeUserRepository;
import project.forwork.api.mock.TestClockHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    private LoginService loginService;
    @Mock
    private TokenCookieService tokenCookieService;
    @Mock
    private RedisUtils redisUtils;
    @Mock
    private PasswordInitializationService passwordInitializationService;

    private FakeUserRepository fakeUserRepository;
    TestClockHolder testClockHolder;

    @BeforeEach
    void init(){
        fakeUserRepository = new FakeUserRepository();
        testClockHolder = new TestClockHolder(123L);
        loginService = LoginService.builder()
                .tokenCookieService(tokenCookieService)
                .redisUtils(redisUtils)
                .clockHolder(testClockHolder)
                .userRepository(fakeUserRepository)
                .passwordInitializationService(passwordInitializationService)
                .build();

        User user = User.builder()
                .id(1L)
                .email("user@naver.com")
                .name("user")
                .password("123")
                .roleType(RoleType.USER)
                .build();

        fakeUserRepository.save(user);
    }
    @Test
    void UserLoginRequest_으로_로그인을_할_수_있다(){
        //given(상황환경 세팅)
        UserLoginRequest loginUser = UserLoginRequest.builder()
                .email("user@naver.com")
                .password("123")
                .build();

        String key = "loginAttempt:userId:" + 1L;
        when(redisUtils.createKeyForm(anyString(), anyLong())).thenReturn(key);
        when(redisUtils.incrementDataInitTimeOut(eq(key), anyLong())).thenReturn(1L);

        //when(상황발생)
        HttpServletResponse response = mock(HttpServletResponse.class);
        UserResponse result = loginService.login(response, loginUser);
        User user = fakeUserRepository.getByIdWithThrow(1L);

        //then(검증)
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getRoleType()).isEqualTo(user.getRoleType());
        verify(tokenCookieService).createCookies(eq(response), eq(user));
        verify(redisUtils).deleteData(anyString());  // 로그인 시도 횟수 초기화
    }

    @Test
    void 존재하지_않는_이메일로_로그인_시도시_예외발생(){
        //given(상황환경 세팅)
        UserLoginRequest loginUser = UserLoginRequest.builder()
                .email("null@naver.com")
                .password("123")
                .build();

        //when(상황발생)
        //then(검증)
        HttpServletResponse response = mock(HttpServletResponse.class);
        assertThatThrownBy(() -> loginService.login(response, loginUser))
                .isInstanceOf(ApiException.class);
    }
    
    @Test
    void 비밀번호가_일치하지_않으면_예외발생(){
        //given(상황환경 세팅)
        UserLoginRequest loginUser = UserLoginRequest.builder()
                .email("user@naver.com")
                .password("xxx")
                .build();

        //when(상황발생)
        //then(검증)
        HttpServletResponse response = mock(HttpServletResponse.class);
        assertThatThrownBy(() -> loginService.login(response, loginUser))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void 비밀번호_불일치로_인한_로그인_실패가_최대치가_넘으면_임시비밀번호로_초기화_및_이메일전송(){
        //given(상황환경 세팅)
        UserLoginRequest loginUser = UserLoginRequest.builder()
                .email("user@naver.com")
                .password("xxx")
                .build();

        User user = fakeUserRepository.getByIdWithThrow(1L);

        String loginAttemptKey = "loginAttempt:userId:" + user.getId();
        when(redisUtils.createKeyForm(anyString(), anyLong())).thenReturn(loginAttemptKey);
        when(redisUtils.incrementDataInitTimeOut(anyString(), anyLong())).thenReturn(6L);

        //when(상황발생)
        //then(검증)
        HttpServletResponse response = mock(HttpServletResponse.class);
        assertThatThrownBy(() -> loginService.login(response, loginUser))
                .isInstanceOf(ApiException.class);

        verify(passwordInitializationService).issueTemporaryPassword(eq(user));
        verify(redisUtils).deleteData(eq(loginAttemptKey));
    }
}