package project.forwork.api.domain.cartresume.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.cart.infrastructure.enums.CartStatus;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.cartresume.controller.model.CartResumeDetailResponse;
import project.forwork.api.domain.cartresume.controller.model.CartResumeResponse;
import project.forwork.api.domain.cartresume.controller.model.CartSummary;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CartResumeServiceTest {

    private CartResumeService cartResumeService;

    @BeforeEach
    void init(){
        FakeSalesPostRepository fakeSalesPostRepository = new FakeSalesPostRepository();
        FakeCartResumeRepository fakeCartResumeRepository = new FakeCartResumeRepository();
        FakeCartRepository fakeCartRepository = new FakeCartRepository();
        this.cartResumeService = CartResumeService.builder()
                .cartResumeRepository(fakeCartResumeRepository)
                .salesPostRepository(fakeSalesPostRepository)
                .cartRepository(fakeCartRepository)
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

        User user3 = User.builder()
                .id(3L)
                .email("user3@naver.com")
                .name("user3")
                .password("123")
                .roleType(RoleType.USER)
                .build();

        Resume resume1 = Resume.builder()
                .id(1L)
                .seller(user1)
                .field(FieldType.AI)
                .level(LevelType.JUNIOR)
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("21000.00"))
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
                .price(new BigDecimal("78000.00"))
                .description("test resume2")
                .status(ResumeStatus.ACTIVE)
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

        fakeSalesPostRepository.save(salesPost1);
        fakeSalesPostRepository.save(salesPost2);

        Cart cart1 = Cart.builder()
                .id(1L)
                .user(user1)
                .status(CartStatus.ACTIVE)
                .modifiedAt(LocalDateTime.of(2024, 9, 13, 12, 0, 0))
                .build();

        Cart cart2 = Cart.builder()
                .id(2L)
                .user(user2)
                .status(CartStatus.ACTIVE)
                .modifiedAt(LocalDateTime.of(2024, 9, 13, 12, 0, 0))
                .build();

        fakeCartRepository.save(cart1);
        fakeCartRepository.save(cart2);

        CartResume cartResume1 = CartResume.builder()
                .cart(cart1)
                .resume(resume1)
                .build();

        CartResume cartResume2 = CartResume.builder()
                .cart(cart1)
                .resume(resume2)
                .build();

        fakeCartResumeRepository.save(cartResume1);
        fakeCartResumeRepository.save(cartResume2);
    }

    @Test
    void 로그인한_회원은_장바구니에_이력서를_담을_수_있다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(2L)
                .build();

        //when(상황발생)
        CartResume cartResume = cartResumeService.register(currentUser, 2L);

        //then(검증)
        assertThat(cartResume.getId()).isNotNull();
        assertThat(cartResume.getResume().getId()).isEqualTo(2L);
    }

    @Test
    void 이미_장바구니에_있는_이력서는_담을_수_없다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        //then(검증)
        assertThatThrownBy(() -> cartResumeService.register(currentUser, 1L))
                .isInstanceOf(ApiException.class);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void 장바구니에서_선택한_이력서를_삭제_할_수_있다(long resumeId){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        cartResumeService.deleteBySelected(currentUser, List.of(1L, 2L));

        //then(검증)
        assertThatCode(() -> cartResumeService.register(currentUser, resumeId))
                .doesNotThrowAnyException();
    }

    @Test
    void 장바구니에서_선택한_이력서의_총가격과_갯수를_파악_할_수_있다(){
        //given(상황환경 세팅)
        //when(상황발생)
        CartResumeDetailResponse response1 = cartResumeService.selectCartResumes(List.of(1L, 2L));
        CartResumeDetailResponse response2 = cartResumeService.selectCartResumes(List.of(2L));

        //then(검증)
        assertThat(response1.getTotalPrice()).isEqualTo(new BigDecimal("99000.00"));
        assertThat(response1.getTotalQuantity()).isEqualTo(2);
        assertThat(response2.getTotalPrice()).isEqualTo(new BigDecimal("78000.00"));
        assertThat(response2.getTotalQuantity()).isEqualTo(1);
    }

    @Test
    void 로그인한_회원은_장바구니에_담긴_이력서를_전체_조회_할_수_있다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();
        //when(상황발생)
        CartResumeDetailResponse all = cartResumeService.selectAll(currentUser);

        //then(검증)
        assertThat(all.getTotalQuantity()).isEqualTo(2);
        assertThat(all.getTotalPrice()).isEqualTo(new BigDecimal("99000.00"));
    }
}