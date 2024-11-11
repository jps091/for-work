package project.forwork.api.domain.salepost.service;

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
import project.forwork.api.domain.salespost.service.SalesPostService;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;
import project.forwork.api.domain.thumbnailimage.service.port.ThumbnailImageRepository;
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.FakeSalesPostRepository;
import project.forwork.api.mock.FakeThumbnailImageRepository;
import project.forwork.api.mock.FakeUserRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SalesPostServiceTest {

    private SalesPostService salesPostService;
    private FakeSalesPostRepository fakeSalesPostRepository;
    private FakeThumbnailImageRepository fakeThumbnailImageRepository;
    FakeUserRepository fakeUserRepository;

    @BeforeEach
    void init(){
        fakeUserRepository = new FakeUserRepository();
        fakeSalesPostRepository = new FakeSalesPostRepository();
        fakeThumbnailImageRepository = new FakeThumbnailImageRepository();
        this.salesPostService = SalesPostService.builder()
                .userRepository(fakeUserRepository)
                .salesPostRepository(fakeSalesPostRepository)
                .thumbnailImageRepository(fakeThumbnailImageRepository)
                .build();

        User user1 = User.builder()
                .id(1L)
                .email("user1@naver.com")
                .name("user1")
                .password("123")
                .status(UserStatus.USER)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("user2@naver.com")
                .name("user2")
                .password("123")
                .status(UserStatus.USER)
                .build();

        fakeUserRepository.save(user1);
        fakeUserRepository.save(user2);

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
                .seller(user2)
                .field(FieldType.BACKEND)
                .level(LevelType.NEW)
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("98000.00"))
                .description("test resume2")
                .status(ResumeStatus.ACTIVE)
                .build();

        Resume resume3 = Resume.builder()
                .id(3L)
                .seller(user2)
                .field(FieldType.BACKEND)
                .level(LevelType.NEW)
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("98000.00"))
                .description("test resume2")
                .status(ResumeStatus.PENDING)
                .build();

        SalesPost salesPost1 = SalesPost.builder()
                .id(1L)
                .resume(resume1)
                .salesStatus(SalesStatus.SELLING)
                .build();

        SalesPost salesPost2 = SalesPost.builder()
                .id(2L)
                .resume(resume2)
                .salesStatus(SalesStatus.CANCELED)
                .build();

        SalesPost salesPost3 = SalesPost.builder()
                .id(3L)
                .resume(resume3)
                .salesStatus(SalesStatus.CANCELED)
                .build();

        fakeSalesPostRepository.save(salesPost1);
        fakeSalesPostRepository.save(salesPost2);
        fakeSalesPostRepository.save(salesPost3);

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
    }

    @Test
    void CANCELED_판매글을_다시_REGISTER_하면_SELLING_으로_상태가_변경_된다(){
        //given(상황환경 세팅)
        User user = User.builder()
                .id(1L)
                .email("test@com")
                .password("123")
                .build();

        Resume resume2 = Resume.builder()
                .id(2L)
                .seller(user)
                .field(FieldType.BACKEND)
                .level(LevelType.NEW)
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("98000.00"))
                .description("test resume2")
                .status(ResumeStatus.ACTIVE)
                .build();

        //when(상황발생)
        salesPostService.registerSalesPost(resume2);
        SalesPost salesPost = fakeSalesPostRepository.getByResumeIdWithThrow(resume2.getId());

        //then(검증)
        assertThat(salesPost.getSalesStatus()).isEqualTo(SalesStatus.SELLING);
    }

    @Test
    void 판매글이_존재_하지_않는_이력서를_가지고REGISTER를_하면_SELLING_상태인_판매글이_자동_생성_된다(){
        //given(상황환경 세팅)
        User user = User.builder()
                .id(1L)
                .email("test@com")
                .password("123")
                .build();

        Resume resume4 = Resume.builder()
                .id(4L)
                .seller(user)
                .field(FieldType.AI)
                .level(LevelType.JUNIOR)
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("10000.00"))
                .description("test resume1")
                .status(ResumeStatus.ACTIVE)
                .build();

        //when(상황발생)
        salesPostService.registerSalesPost(resume4);
        SalesPost salesPost = fakeSalesPostRepository.getByResumeIdWithThrow(resume4.getId());

        //then(검증)
        assertThat(salesPost.getSalesStatus()).isEqualTo(SalesStatus.SELLING);
    }


    @Test
    void 판매_중지_글을_다시_판매중으로_변경할_수_있습니다(){
        //given(상황환경 세팅)
        CurrentUser user1 = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        salesPostService.changeSalesStatus(user1, 1L, SalesStatus.SELLING);
        SalesPost salesPost = fakeSalesPostRepository.getByIdWithThrow(1L);

        //then(검증)
        assertThat(salesPost.getSalesStatus()).isEqualTo(SalesStatus.SELLING);
    }

    @Test
    void 판매_글을__판매_중지로_변경할_수_있습니다(){
        //given(상황환경 세팅)
        CurrentUser user2 = CurrentUser.builder()
                .id(2L)
                .build();

        //when(상황발생)
        salesPostService.changeSalesStatus(user2, 2L, SalesStatus.CANCELED);
        SalesPost salesPost = fakeSalesPostRepository.getByIdWithThrow(2L);

        //then(검증)
        assertThat(salesPost.getSalesStatus()).isEqualTo(SalesStatus.CANCELED);
    }

    @Test
    void 활성_상태인_자신의_이력서에_접근_할_수_있다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        //then(검증)
        assertThatCode(() -> salesPostService.validateSellerAndResumeStatus(currentUser, 1L))
                .doesNotThrowAnyException();
    }

    @Test
    void 자신의_이력서가_아닐_경우_예외가_발생_한다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        //then(검증)
        assertThatThrownBy(() -> salesPostService.validateSellerAndResumeStatus(currentUser, 2L))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void 이력서가_활성_상태가_아닐_경우_예외가_발생_한다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(2L)
                .build();

        //when(상황발생)
        //then(검증)
        assertThatThrownBy(() -> salesPostService.validateSellerAndResumeStatus(currentUser, 3L))
                .isInstanceOf(ApiException.class);
    }
}