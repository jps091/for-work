package project.forwork.api.domain.user.model;

import org.junit.jupiter.api.Test;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.user.controller.model.UserCreateRequest;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.mock.TestClockHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class UserTest {
    @Test
    void UserCreateRequest_객체로_생성할_수_있다(){
        //given(상황환경 세팅)
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .email("test@naver.com")
                .name("kim")
                .password("123")
                .build();

        //when(상황발생)
        User user = User.from(userCreateRequest);

        //then(검증)
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("test@naver.com");
        assertThat(user.getPassword()).isEqualTo("123");
        assertThat(user.getRoleType()).isEqualTo(RoleType.USER);
    }

    @Test
    void 비밀번호가_일치하면_로그인을_할_수_있고_최근_로그인_시간이_갱신된다(){
        //given(상황환경 세팅)
        User user = User.builder()
                .email("test@naver.com")
                .name("kim")
                .password("123")
                .build();

        //when(상황발생)
        user = user.login(new TestClockHolder(1678530673958L), "123");

        //then(검증)
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
    }

    @Test
    void 비밀번호가_일치하지_않으면_예외가_발생_된다(){
        //given(상황환경 세팅)
        User user = User.builder()
                .email("test@naver.com")
                .name("kim")
                .password("123")
                .build();
        //when(상황발생)
        //then(검증)
        assertThatThrownBy(() -> user.login(new TestClockHolder(1678530673958L), "321"))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void 비밀번호를_수정할_수_있다(){
        //given(상황환경 세팅)
        User user = User.builder()
                .email("test@naver.com")
                .name("kim")
                .password("123")
                .build();

        //when(상황발생)
        user = user.modifyPassword("345");

        //then(검증)
        assertThat(user.getPassword()).isEqualTo("345");
    }

    @Test
    void 비밀번호를_임시_비밀번호로_초기화_할_수_있다(){
        //given(상황환경 세팅)
        User user = User.builder()
                .email("test@naver.com")
                .name("kim")
                .password("123")
                .build();

        //when(상황발생)
        user = user.initTemporaryPassword("345");

        //then(검증)
        assertThat(user.getPassword()).isEqualTo("345");
    }

    @Test
    void 비밀번호가_일치하지_않으면_참을_반환한다(){
        //given(상황환경 세팅)
        User user = User.builder()
                .email("test@naver.com")
                .name("kim")
                .password("123")
                .build();

        //when(상황발생)
        boolean result = user.isNotMatchPassword("321");

        //then(검증)
        assertThat(result).isTrue();
    }
}