package project.forwork.api.domain.resumedecision.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.*;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ResumeDecisionServiceTest {

    private ResumeDecisionService resumeDecisionService;
    private FakeSalesPostRepository fakeSalesPostRepository;
    private FakeResumeDecisionRepository fakeResumeDecisionRepository;
    private  FakeResumeRepository fakeResumeRepository;
    private FakeThumbnailImageRepository fakeThumbnailImageRepository;

    @BeforeEach
    void init(){
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        fakeResumeRepository = new FakeResumeRepository();
        fakeResumeDecisionRepository = new FakeResumeDecisionRepository();
        fakeSalesPostRepository = new FakeSalesPostRepository();
        fakeThumbnailImageRepository = new FakeThumbnailImageRepository();
        resumeDecisionService = ResumeDecisionService.builder()
                .resumeRepository(fakeResumeRepository)
                .userRepository(fakeUserRepository)
                .resumeDecisionRepository(fakeResumeDecisionRepository)
                .salesPostRepository(fakeSalesPostRepository)
                .thumbnailImageRepository(fakeThumbnailImageRepository)
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

        ThumbnailImage thumbnailImage1 = ThumbnailImage.builder()
                .id(1L)
                .url("www.test1.com")
                .fieldType(FieldType.AI)
                .build();
        ThumbnailImage thumbnailImage2 = ThumbnailImage.builder()
                .id(2L)
                .url("www.test2.com")
                .fieldType(FieldType.BACKEND)
                .build();
        fakeThumbnailImageRepository.saveAll(List.of(thumbnailImage1, thumbnailImage2));


        SalesPost salesPost = SalesPost.builder()
                .id(1L)
                .resume(resume1)
                .title(resume1.createSalesPostTitle())
                .salesStatus(SalesStatus.CANCELED)
                .salesQuantity(30)
                .thumbnailImage(thumbnailImage1)
                .viewCount(0)
                .build();

        fakeSalesPostRepository.save(salesPost);
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
    void CANCELED_상태인_판매글을_다시_APPROVE_하면_SELLING_으로_상태가_변경_된다(){
        //given(상황환경 세팅)
        Resume resume = fakeResumeRepository.getByIdWithThrow(1L);
        ThumbnailImage thumbnailImage1 = ThumbnailImage.builder()
                .id(1L)
                .url("www.test1.com")
                .fieldType(FieldType.AI)
                .build();

        //when(상황발생)
        resumeDecisionService.registerSalesPost(resume, thumbnailImage1);
        SalesPost salesPost = fakeSalesPostRepository.getByResumeWithThrow(resume);

        //then(검증)
        assertThat(salesPost.getSalesStatus()).isEqualTo(SalesStatus.SELLING);
    }

    @Test
    void 이력서를_APPROVE_했는데_판매글이_존재_하지_않는다면_SELLING_상태인_판매글이_자동_생성_된다(){
        //given(상황환경 세팅)
        Resume resume = fakeResumeRepository.getByIdWithThrow(2L);
        ThumbnailImage thumbnailImage2 = ThumbnailImage.builder()
                .id(1L)
                .url("www.test1.com")
                .fieldType(FieldType.BACKEND)
                .build();

        //when(상황발생)
        resumeDecisionService.registerSalesPost(resume, thumbnailImage2);
        SalesPost salesPost = fakeSalesPostRepository.getByResumeWithThrow(resume);

        //then(검증)
        assertThat(salesPost.getSalesStatus()).isEqualTo(SalesStatus.SELLING);
        assertThat(salesPost.getThumbnailImage()).isEqualTo(thumbnailImage2);
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