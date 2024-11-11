package project.forwork.api.domain.resumedecision.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.salespost.service.SalesPostService;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.*;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ResumeDecisionServiceTest {

    private ResumeDecisionService resumeDecisionService;
    private  FakeResumeRepository fakeResumeRepository;
    private  FakeUserRepository fakeUserRepository;
    private  FakeResumeDecisionRepository fakeResumeDecisionRepository;
    @Mock
    private SalesPostService salesPostService;

    @BeforeEach
    void init(){
        fakeUserRepository = new FakeUserRepository();
        fakeResumeRepository = new FakeResumeRepository();
        fakeResumeDecisionRepository = new FakeResumeDecisionRepository();
        resumeDecisionService = ResumeDecisionService.builder()
                .resumeRepository(fakeResumeRepository)
                .userRepository(fakeUserRepository)
                .salesPostService(salesPostService)
                .resumeDecisionRepository(fakeResumeDecisionRepository)
                .build();

        User user1 = User.builder()
                .id(1L)
                .email("user1@naver.com")
                .name("user1")
                .password("123")
                .status(UserStatus.USER)
                .build();


        User admin = User.builder()
                .id(3L)
                .email("admin@naver.com")
                .name("admin")
                .password("321")
                .status(UserStatus.ADMIN)
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

        Resume resume2 = Resume.builder()
                .id(2L)
                .seller(user1)
                .field(FieldType.BACKEND)
                .level(LevelType.NEW)
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("98000.00"))
                .description("test resume2")
                .status(ResumeStatus.PENDING)
                .build();

        fakeResumeRepository.save(resume1);
        fakeResumeRepository.save(resume2);
    }

    @Test
    void 일반_유저는_approve_를_할_수_없다(){
        //given(상황환경 세팅)
        CurrentUser user = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        //then(검증)
        assertThatThrownBy(() -> resumeDecisionService.approve(user, 2L))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void 어드민이_approve_를_하면_이력서_상태가_ACTIVE_로_변경_된다(){
        //given(상황환경 세팅)
        CurrentUser admin = CurrentUser.builder()
                .id(3L)
                .build();

        //when(상황발생)
        resumeDecisionService.approve(admin, 2L);

        //then(검증)
        Resume resume = fakeResumeRepository.getByIdWithThrow(2L);
        assertThat(resume.getStatus()).isEqualTo(ResumeStatus.ACTIVE);
    }

    @Test
    void 일반_유저는_deny_를_할_수_없다(){
        //given(상황환경 세팅)
        CurrentUser user = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        //then(검증)
        assertThatThrownBy(() -> resumeDecisionService.deny(user, 1L))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void 어드민이_deny_를_하면_이력서_상태가_REJECTED_로_변경_된다(){
        //given(상황환경 세팅)
        CurrentUser admin = CurrentUser.builder()
                .id(3L)
                .build();

        //when(상황발생)
        resumeDecisionService.deny(admin, 1L);

        //then(검증)
        Resume resume = fakeResumeRepository.getByIdWithThrow(1L);
        assertThat(resume.getStatus()).isEqualTo(ResumeStatus.REJECTED);
    }
}