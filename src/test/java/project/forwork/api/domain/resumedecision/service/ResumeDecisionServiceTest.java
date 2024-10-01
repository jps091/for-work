package project.forwork.api.domain.resumedecision.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.FakeResumeDecisionRepository;
import project.forwork.api.mock.FakeResumeRepository;
import project.forwork.api.mock.FakeSalesPostRepository;
import project.forwork.api.mock.FakeUserRepository;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class ResumeDecisionServiceTest {

    private ResumeDecisionService resumeDecisionService;
    private FakeSalesPostRepository fakeSalesPostRepository;
    private FakeResumeDecisionRepository fakeResumeDecisionRepository;
    private  FakeResumeRepository fakeResumeRepository;

    @BeforeEach
    void init(){
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        fakeResumeRepository = new FakeResumeRepository();
        fakeResumeDecisionRepository = new FakeResumeDecisionRepository();
        fakeSalesPostRepository = new FakeSalesPostRepository();
        this.resumeDecisionService = ResumeDecisionService.builder()
                .resumeRepository(fakeResumeRepository)
                .userRepository(fakeUserRepository)
                .resumeDecisionRepository(fakeResumeDecisionRepository)
                .salesPostRepository(fakeSalesPostRepository)
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


        SalesPost salesPost = SalesPost.builder()
                .id(2L)
                .resume(resume2)
                .title(resume2.createSalesPostTitle())
                .salesStatus(SalesStatus.CANCELED)
                .salesQuantity(30)
                .viewCount(0)
                .build();

        fakeSalesPostRepository.save(salesPost);
    }

    @Test
    void 승인을_할_수_있다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        Resume resume = fakeResumeRepository.getByIdWithThrow(1L);

        //when(상황발생)
        //then(검증)
        assertThatCode(() -> resumeDecisionService.approve(currentUser, resume.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    void 승인을_했는데_판매글이_존재하지_않다면_판매글이_자동_생성_된다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        Resume resume = fakeResumeRepository.getByIdWithThrow(2L);

        //when(상황발생)
        resumeDecisionService.approve(currentUser, resume.getId());

        //then(검증)
        assertThat(fakeSalesPostRepository.getByResumeWithThrow(resume).getId()).isNotNull();
    }

    @Test
    void 승인을_했는데_이미_판매글이_존재한다면_판매글의_상태가_SELLING_으로_변경_된다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        Resume resume = fakeResumeRepository.getByIdWithThrow(2L);

        //when(상황발생)
        resumeDecisionService.approve(currentUser, resume.getId());
        SalesPost salesPost = fakeSalesPostRepository.getByResumeWithThrow(resume);

        //then(검증)
        assertThat(salesPost.getSalesStatus()).isEqualTo(SalesStatus.SELLING);
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