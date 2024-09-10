package project.forwork.api.domain.salepost.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salepost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salepost.model.SalePost;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.FakeResumeRepository;
import project.forwork.api.mock.FakeSalePostRepository;
import project.forwork.api.mock.FakeUserRepository;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SalePostServiceTest {

    private SalePostService salePostService;

    @BeforeEach
    void init(){
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeResumeRepository fakeResumeRepository = new FakeResumeRepository();
        FakeSalePostRepository fakeSalePostRepository = new FakeSalePostRepository();
        this.salePostService = SalePostService.builder()
                .sellerValidationService(new SellerValidationService(fakeUserRepository, fakeResumeRepository))
                .salePostRepository(fakeSalePostRepository)
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
                .architectureImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
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
                .architectureImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("98000.00"))
                .description("test resume2")
                .status(ResumeStatus.ACTIVE)
                .build();

        fakeResumeRepository.save(resume1);
        fakeResumeRepository.save(resume2);

        SalePost salePost1 = SalePost.builder()
                .id(1L)
                .resume(resume1)
                .title(resume1.createSalePostTitle())
                .salesStatus(SalesStatus.SELLING)
                .quantity(30)
                .viewCount(0)
                .build();

        SalePost salePost2 = SalePost.builder()
                .id(2L)
                .resume(resume2)
                .title(resume2.createSalePostTitle())
                .salesStatus(SalesStatus.CANCELED)
                .quantity(30)
                .viewCount(0)
                .build();

        fakeSalePostRepository.save(salePost1);
        fakeSalePostRepository.save(salePost2);
    }

    @Test
    void CurrentUser와_이력서ID를_가지고_판매글을_등록할_수_있습니다() {
        // given
        CurrentUser user1 = CurrentUser.builder()
                .id(1L)
                .build();

        // when
        SalePost salePost = salePostService.register(user1, 1L);

        //then
        assertThat(salePost.getId()).isNotNull();
        assertThat(salePost.getSalesStatus()).isEqualTo(SalesStatus.SELLING);
        assertThat(salePost.getQuantity()).isEqualTo(30);
    }

    @Test
    void 판매_중지_글을_다시_판매중으로_변경할_수_있습니다(){
        //given(상황환경 세팅)
        CurrentUser user1 = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        salePostService.startSelling(user1, 1L);
        SalePost salePost = salePostService.getSalePostWithThrow(1L);

        //then(검증)
        assertThat(salePost.getSalesStatus()).isEqualTo(SalesStatus.SELLING);
    }

    @Test
    void 판매_글을__판매_중지로_변경할_수_있습니다(){
        //given(상황환경 세팅)
        CurrentUser user2 = CurrentUser.builder()
                .id(2L)
                .build();

        //when(상황발생)
        salePostService.cancelSelling(user2, 2L);
        SalePost salePost = salePostService.getSalePostWithThrow(2L);

        //then(검증)
        assertThat(salePost.getSalesStatus()).isEqualTo(SalesStatus.CANCELED);
    }

    @Test
    void getSellingPostWithThrow() { // TODO 동시성공부후
    }

}