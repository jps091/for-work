package project.forwork.api.domain.resumedecision.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.FakeResumeDecisionRepository;
import project.forwork.api.mock.FakeResumeRepository;
import project.forwork.api.mock.FakeUserRepository;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class ResumeDecisionServiceTest {

    private ResumeDecisionService resumeDecisionService;


    @BeforeEach
    void init(){
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeResumeRepository fakeResumeRepository = new FakeResumeRepository();
        FakeResumeDecisionRepository fakeResumeDecisionRepository = new FakeResumeDecisionRepository();
        this.resumeDecisionService = ResumeDecisionService.builder()
                .resumeRepository(fakeResumeRepository)
                .userRepository(fakeUserRepository)
                .resumeDecisionRepository(fakeResumeDecisionRepository)
                .build();

        User user1 = User.builder()
                .id(1L)
                .email("user1@naver.com")
                .name("user1")
                .password("123")
                .roleType(RoleType.USER)
                .build();


        User admin = User.builder()
                .id(3L)
                .email("admin@naver.com")
                .name("admin")
                .password("321")
                .roleType(RoleType.ADMIN)
                .build();
        fakeUserRepository.save(user1);
        fakeUserRepository.save(admin);

        Resume resume1 = Resume.builder()
                .id(1L)
                .seller(user1)
                .field(FieldType.AI)
                .level(LevelType.JUNIOR)
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("10000.00"))
                .description("test resume1")
                .status(ResumeStatus.ACTIVE)
                .build();

        fakeResumeRepository.save(resume1);
    }

    @Test
    void 승인을_할_수_있다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        //then(검증)
        assertThatCode(() -> resumeDecisionService.approve(currentUser, 1L))
                .doesNotThrowAnyException();
    }

    @Test
    void 거절을_할_수_있다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        //then(검증)
        assertThatCode(() -> resumeDecisionService.deny(currentUser, 1L))
                .doesNotThrowAnyException();
    }
}