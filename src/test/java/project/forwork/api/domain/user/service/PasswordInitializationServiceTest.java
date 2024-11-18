package project.forwork.api.domain.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.forwork.api.common.infrastructure.Producer;
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.FakeUserRepository;
import project.forwork.api.mock.TestUuidHolder;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PasswordInitializationServiceTest {
    @Mock
    private Producer producer;

    @Test
    void 비밀번호를_임시_비밀번호로_초기화_하고_메일을_발송한다(){
        //given(상황환경 세팅)
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        TestUuidHolder testUuidHolder = new TestUuidHolder("gggggg-gggggg-qqqqqq");
        PasswordInitializationService passwordInitializationService =
                new PasswordInitializationService(fakeUserRepository, testUuidHolder, producer);

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
    }
}