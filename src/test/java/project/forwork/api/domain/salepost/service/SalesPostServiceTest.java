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
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.FakeResumeRepository;
import project.forwork.api.mock.FakeSalesPostRepository;
import project.forwork.api.mock.FakeUserRepository;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SalesPostServiceTest {

    private SalesPostService salesPostService;
    private FakeSalesPostRepository fakeSalesPostRepository;
    FakeUserRepository fakeUserRepository;

    @BeforeEach
    void init(){
        fakeUserRepository = new FakeUserRepository();
        fakeSalesPostRepository = new FakeSalesPostRepository();
        this.salesPostService = SalesPostService.builder()
                .userRepository(fakeUserRepository)
                .salesPostRepository(fakeSalesPostRepository)
                .build();

        User user1 = User.builder()
                .id(1L)
                .email("user1@naver.com")
                .name("user1")
                .password("123")
                .roleType(RoleType.USER)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("user2@naver.com")
                .name("user2")
                .password("123")
                .roleType(RoleType.USER)
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
                .title(resume1.createSalesPostTitle())
                .salesStatus(SalesStatus.SELLING)
                .salesQuantity(30)
                .viewCount(0)
                .build();

        SalesPost salesPost2 = SalesPost.builder()
                .id(2L)
                .resume(resume2)
                .title(resume2.createSalesPostTitle())
                .salesStatus(SalesStatus.CANCELED)
                .salesQuantity(30)
                .viewCount(0)
                .build();

        SalesPost salesPost3 = SalesPost.builder()
                .id(3L)
                .resume(resume3)
                .title(resume3.createSalesPostTitle())
                .salesStatus(SalesStatus.CANCELED)
                .salesQuantity(30)
                .viewCount(0)
                .build();

        fakeSalesPostRepository.save(salesPost1);
        fakeSalesPostRepository.save(salesPost2);
        fakeSalesPostRepository.save(salesPost3);
    }

    @Test
    void 판매_중지_글을_다시_판매중으로_변경할_수_있습니다(){
        //given(상황환경 세팅)
        CurrentUser user1 = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        salesPostService.startSelling(user1, 1L);
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
        salesPostService.cancelSelling(user2, 2L);
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

    @Test
    void getSellingPostWithThrow() { // TODO 동시성공부후
    }

}