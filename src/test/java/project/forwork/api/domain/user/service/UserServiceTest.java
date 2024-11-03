package project.forwork.api.domain.user.service;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.RedisUtils;
import project.forwork.api.domain.cart.infrastructure.enums.CartStatus;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.resume.service.ResumeService;
import project.forwork.api.domain.token.service.TokenHeaderService;
import project.forwork.api.domain.user.controller.model.EmailVerifyRequest;
import project.forwork.api.domain.user.controller.model.PasswordModifyRequest;
import project.forwork.api.domain.user.controller.model.PasswordVerifyRequest;
import project.forwork.api.domain.user.controller.model.UserCreateRequest;
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.*;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;
    private FakeUserRepository fakeUserRepository;
    private FakeCartRepository fakeCartRepository;
    private TestUuidHolder testUuidHolder;
    private FakeMailSender fakeMailSender;
    @Mock
    private ResumeService resumeService;
    @Mock
    private TokenHeaderService tokenHeaderService;
    @Mock
    private RedisUtils redisUtils;
    @BeforeEach
    void init(){
        fakeUserRepository = new FakeUserRepository();
        testUuidHolder = new TestUuidHolder("aaaaa-111111-eeeee");
        fakeMailSender = new FakeMailSender();
        fakeCartRepository = new FakeCartRepository();
        userService = UserService.builder()
                .mailSender(fakeMailSender)
                .userRepository(fakeUserRepository)
                .cartRepository(fakeCartRepository)
                .resumeService(resumeService)
                .redisUtils(redisUtils)
                .cartRepository(fakeCartRepository)
                .uuidHolder(testUuidHolder)
                .tokenHeaderService(tokenHeaderService)
                .build();

        User user = User.builder()
                .id(1L)
                .email("user@naver.com")
                .name("user")
                .password("123")
                .status(UserStatus.USER)
                .build();
        fakeUserRepository.save(user);

        Cart cart = Cart.builder()
                .id(1L)
                .user(user)
                .status(CartStatus.ACTIVE)
                .build();
        fakeCartRepository.save(cart);
    }
    
    @Test
    void UserCreateRequest_로_User_와_Cart_를_생성할_수_있다(){
        //given(상황환경 세팅)
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .name("test")
                .email("test@naver.com")
                .password("123")
                .build();
        
        //when(상황발생)
        User user = userService.register(userCreateRequest);
        Cart cart = fakeCartRepository.getByUserIdWithThrow(user.getId());

        //then(검증)
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@naver.com");
        assertThat(user.getName()).isEqualTo("test");
        assertThat(user.getPassword()).isEqualTo("123");
        assertThat(cart.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void 이미_존재하는_이메일로_User_를_생성할_수_없다(){
        //given(상황환경 세팅)
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .name("test")
                .email("user@naver.com")
                .password("123")
                .build();

        //when(상황발생)
        //then(검증)
        assertThatThrownBy(() -> userService.register(userCreateRequest))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void PasswordVerifyRequest_와_CurrentUser_로_비밀번호_검증을_할_수_있다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        PasswordVerifyRequest body = PasswordVerifyRequest.builder()
                .password("123")
                .build();

        //when(상황발생)
        //then(검증)
        assertThatCode(() -> userService.verifyPassword(currentUser, body))
                .doesNotThrowAnyException();
    }

    @Test
    void PasswordVerifyRequest_비밀번호가_일치_하지_않으면_예외가_발생_한다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        PasswordVerifyRequest body = PasswordVerifyRequest.builder()
                .password("455")
                .build();

        //when(상황발생)
        //then(검증)
        assertThatThrownBy(() -> userService.verifyPassword(currentUser, body))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void CurrentUser_와_ModifyPasswordRequest_로_비밀번호를_수정할_수_있다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        PasswordModifyRequest passwordModifyRequest = PasswordModifyRequest.builder()
                .password("567")
                .build();

        //when(상황발생)
        userService.updatePassword(currentUser, passwordModifyRequest);
        User user = userService.getByIdWithThrow(1L);

        //then(검증)
        assertThat(user.getPassword()).isEqualTo("567");
    }

    @Test
    void 회원탈퇴를_하면_쿠키와_리프레쉬_토큰은_만료_된다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        HttpServletResponse response = mock(HttpServletResponse.class);
        userService.delete(currentUser, response);

        //then(검증)
        verify(tokenHeaderService).expiredRefreshTokenAndHeaders(eq(currentUser.getId()), eq(response));
    }

    @Test
    void 회원탈퇴를_하면_상태가_DELETE로_변경_된다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        HttpServletResponse response = mock(HttpServletResponse.class);
        userService.delete(currentUser, response);

        //then(검증)
        User user = fakeUserRepository.getByIdWithThrow(1L);
        assertThat(user.getStatus()).isEqualTo(UserStatus.DELETE);
    }

    @Test
    void 회원탈퇴를_하면_장바구니는_삭제_된다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        HttpServletResponse response = mock(HttpServletResponse.class);
        userService.delete(currentUser, response);

        //then(검증)
        assertThatThrownBy(() -> fakeCartRepository.getByUserIdWithThrow(1L))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void 해당_이메일에_인증코드를_전송할_수_있다(){
        //given(상황환경 세팅)
        String email = "test@naver.com";
        String expectedCertificationCode = "aaaaa-111111-eeeee";
        String expectedKey = "email:test@naver.com";
        String emailPrefix = "email:";

        //when(상황발생)
        Mockito.when(redisUtils.createKeyForm(anyString(), anyString())).thenReturn(expectedKey);
        userService.sendCode(email);

        //then(검증)
        verify(redisUtils).setDataWithTimeout(eq(expectedKey), eq(expectedCertificationCode), eq(300L));
        verify(redisUtils).createKeyForm(eq(emailPrefix), eq(email));

        assertThat(fakeMailSender.getEmail()).isEqualTo(email);
        assertThat(fakeMailSender.getTitle()).isEqualTo("for-work 인증 코드 발송");
        assertThat(fakeMailSender.getContent()).contains("인증코드 : " + expectedCertificationCode);
    }

    @Test
    void 발송한_인증코드와_EmailVerifyRequest_을_통해_입려한_인증코드가_다르면_에러가_발생_한다() {
        //given(상황환경 세팅)
        EmailVerifyRequest emailVerifyRequest = EmailVerifyRequest.builder()
                .email("test@naver.com")
                .code("22222-111111-eeeee")
                .build();

        //when(상황발생)
        //then(검증)
        assertThatThrownBy(()-> userService.verifyEmail(emailVerifyRequest))
                .isInstanceOf(ApiException.class);
    }

        @Test
    void EmailVerifyRequest_에_이메일과_인증코드를_검증할_수_있다(){
        //given(상황환경 세팅)
        EmailVerifyRequest emailVerifyRequest = EmailVerifyRequest.builder()
                .email("test@naver.com")
                .code("aaaaa-111111-eeeee")
                .build();

        String expectedKey = "email:test@naver.com";
        String code = "aaaaa-111111-eeeee";

        //when(상황발생)
        Mockito.when(redisUtils.createKeyForm(anyString(), anyString())).thenReturn(expectedKey);
        Mockito.when(redisUtils.getData(anyString())).thenReturn(code);
        userService.verifyEmail(emailVerifyRequest);
        
        //then(검증)
        verify(redisUtils).getData(eq(expectedKey)); // 결국 최종적으로 redisUtils에 들어가는 파라미터로 검증을 해야한다.
        verify(redisUtils).deleteData(eq(expectedKey));
    }
}