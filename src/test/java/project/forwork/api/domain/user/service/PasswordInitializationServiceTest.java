package project.forwork.api.domain.user.service;

import org.junit.jupiter.api.Test;
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.FakeMailSender;
import project.forwork.api.mock.FakeUserRepository;
import project.forwork.api.mock.TestUuidHolder;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordInitializationServiceTest {
    @Test
    void 비밀번호를_임시_비밀번호로_초기화_하고_메일을_발송한다(){
        //given(상황환경 세팅)
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        TestUuidHolder testUuidHolder = new TestUuidHolder("gggggg-gggggg-qqqqqq");
        PasswordInitializationService passwordInitializationService =
                new PasswordInitializationService(fakeUserRepository, testUuidHolder, fakeMailSender);

        User user = User.builder()
                .id(1L)
                .email("user1@naver.com")
                .name("user1")
                .password("123")
                .status(UserStatus.USER)
                .build();
        fakeUserRepository.save(user);

        //when(상황발생)
        passwordInitializationService.issueTemporaryPassword(user);
        User updateUser = fakeUserRepository.getByIdWithThrow(1L);

        //then(검증)
        assertThat(updateUser.getPassword()).isEqualTo("gggggg-gggggg-qqqqqq");
        assertThat(fakeMailSender.email).isEqualTo("user1@naver.com");
        assertThat(fakeMailSender.title).isEqualTo("for-work 임시 비밀번호 발급");
        assertThat(fakeMailSender.content).isEqualTo("임시 비밀번호 : gggggg-gggggg-qqqqqq");
    }
}