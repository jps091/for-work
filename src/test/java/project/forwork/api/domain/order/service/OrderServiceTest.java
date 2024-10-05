package project.forwork.api.domain.order.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.cart.infrastructure.enums.CartStatus;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.order.controller.model.PartialCancelRequest;
import project.forwork.api.domain.order.controller.model.OrderDetailResponse;
import project.forwork.api.domain.order.controller.model.OrderInCartRequest;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;
import project.forwork.api.domain.orderresume.infrastructure.OrderResumeRepositoryCustomImpl;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.OrderResumeService;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    private FakeOrderRepository fakeOrderRepository;

    @Mock
    private OrderResumeService orderResumeService;

    @Mock
    private OrderResumeRepositoryCustomImpl orderResumeQueryDslRepository;

    @BeforeEach
    void init(){
        fakeOrderRepository = new FakeOrderRepository();
        FakeCartResumeRepository fakeCartResumeRepository = new FakeCartResumeRepository();
        FakeOrderResumeRepository fakeOrderResumeRepository = new FakeOrderResumeRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeSalesPostRepository fakeSalesPostRepository = new FakeSalesPostRepository();
        TestClockHolder testClockHolder = new TestClockHolder(LocalDateTime.of(2024, 9, 16, 12, 0, 0));

        orderService = OrderService.builder()
                .orderResumeService(orderResumeService)
                .orderRepository(fakeOrderRepository)
                .orderResumeRepository(fakeOrderResumeRepository)
                .cartResumeRepository(fakeCartResumeRepository)
                .userRepository(fakeUserRepository)
                .salesPostRepository(fakeSalesPostRepository)
                .clockHolder(testClockHolder)
                .orderResumeRepositoryCustom(orderResumeQueryDslRepository)
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
                .seller(user2)
                .field(FieldType.AI)
                .level(LevelType.JUNIOR)
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("40000.00"))
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
                .price(new BigDecimal("22000.00"))
                .description("test resume2")
                .status(ResumeStatus.ACTIVE)
                .build();

        Resume resume3 = Resume.builder()
                .id(3L)
                .seller(user1)
                .field(FieldType.BACKEND)
                .level(LevelType.SENIOR)
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("34000.00"))
                .description("test resume3")
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

        SalesPost salesPost3 = SalesPost.builder()
                .id(2L)
                .resume(resume3)
                .title(resume3.createSalesPostTitle())
                .salesStatus(SalesStatus.CANCELED)
                .salesQuantity(30)
                .viewCount(0)
                .build();

        fakeSalesPostRepository.save(salesPost1);
        fakeSalesPostRepository.save(salesPost2);
        fakeSalesPostRepository.save(salesPost3);

        Cart cart1 = Cart.builder()
                .id(1L)
                .user(user1)
                .status(CartStatus.ACTIVE)
                .build();


        CartResume cartResume1 = CartResume.builder()
                .id(1L)
                .cart(cart1)
                .resume(resume1)
                .build();

        CartResume cartResume2 = CartResume.builder()
                .id(2L)
                .cart(cart1)
                .resume(resume2)
                .build();

        fakeCartResumeRepository.save(cartResume1);
        fakeCartResumeRepository.save(cartResume2);

        // resume 1,2,3 구매
        Order order1 = Order.builder()
                .id(1L)
                .user(user1)
                .totalAmount(new BigDecimal("96000.00"))
                .status(OrderStatus.ORDERED)
                .orderedAt(testClockHolder.now())
                .build();

        // 1 구매
        // 3 부분취소
        Order order2 = Order.builder()
                .id(2L)
                .user(user1)
                .totalAmount(new BigDecimal("40000.00"))
                .status(OrderStatus.PARTIAL_WAIT)
                .orderedAt(testClockHolder.now())
                .build();

        // 1 구매
        Order order3 = Order.builder()
                .id(3L)
                .user(user1)
                .totalAmount(new BigDecimal("40000.00"))
                .status(OrderStatus.WAIT)
                .orderedAt(testClockHolder.now())
                .build();

        // 1,3 구매
        // 2 부분취소
        Order order4 = Order.builder()
                .id(4L)
                .user(user2)
                .totalAmount(new BigDecimal("74000.00"))
                .status(OrderStatus.PARTIAL_CANCEL)
                .orderedAt(testClockHolder.now())
                .build();

        // 1번 취소
        Order order5 = Order.builder()
                .id(5L)
                .user(user2)
                .totalAmount(BigDecimal.ZERO)
                .status(OrderStatus.CANCEL)
                .orderedAt(testClockHolder.now())
                .build();

        fakeOrderRepository.save(order1);
        fakeOrderRepository.save(order2);
        fakeOrderRepository.save(order3);
        fakeOrderRepository.save(order4);
        fakeOrderRepository.save(order5);
    }

    @Test
    void 로그인한_유저는_즉시_Order를_생성_할_수_있다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        Order order = orderService.orderNow(currentUser, 1L);

        //then(검증)
        assertThat(order.getId()).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDERED);
        assertThat(order.getOrderedAt()).isEqualTo(LocalDateTime.of(2024, 9, 16, 12, 0, 0));
        assertThat(order.getTotalAmount()).isEqualTo(new BigDecimal("40000.00"));
    }

    @Test
    void 장바구니에_있는_여러개의_이력서로_Order를_생성_할_수_있다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        OrderInCartRequest orderInCartRequest = OrderInCartRequest.builder()
                .cartResumeIds(List.of(1L, 2L)) // 선택한 이력서장바구니
                .build();

        //when(상황발생)
        Order order = orderService.orderInCart(currentUser, orderInCartRequest);

        //then(검증)
        assertThat(order.getId()).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDERED);
        assertThat(order.getOrderedAt()).isEqualTo(LocalDateTime.of(2024, 9, 16, 12, 0, 0));
        assertThat(order.getTotalAmount()).isEqualTo(new BigDecimal("62000.00"));
    }
    
    @Test
    void 주문의_상태가_ORDER_주문을_WAIT_상태로_변경할_수_있다(){
        //given(상황환경 세팅)

        //when(상황발생)
        orderService.markAsWaiting();
        Order order = fakeOrderRepository.getByIdWithThrow(1L);

        //then(검증)
        assertThat(order.getStatus()).isEqualTo(OrderStatus.WAIT);
    }

    @Test
    void 주문의_상태가_PARTIAL_CANCEL_주문을_PARTIAL_WAIT_상태로_변경할_수_있다(){
        //given(상황환경 세팅)

        //when(상황발생)
        orderService.markPartialAsWaiting();
        Order order = fakeOrderRepository.getByIdWithThrow(4L);

        //then(검증)
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PARTIAL_WAIT);
    }

    @Test
    void 주문의_상태가_WAIT_주문을_CONFIRM_상태로_변경할_수_있다(){
        //given(상황환경 세팅)

        //when(상황발생)
        orderService.markAsConfirm();
        Order order = fakeOrderRepository.getByIdWithThrow(3L);

        //then(검증)
        assertThat(order.getStatus()).isEqualTo(OrderStatus.SEND);
    }
    @Test
    void 주문의_상태가_PARTIAL_WAIT_주문을_PARTIAL_CONFIRM_상태로_변경할_수_있다(){
        //given(상황환경 세팅)

        //when(상황발생)
        orderService.markPartialAsConfirm();
        Order order = fakeOrderRepository.getByIdWithThrow(2L);

        //then(검증)
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PARTIAL_SEND);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void 로그인한_유저는_자신의_주문만_즉시_주문_확정을_할_수_있다(long orderId){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        orderService.orderConfirmNow(currentUser, orderId);
        Order order = fakeOrderRepository.getByIdWithThrow(orderId);

        //then(검증)
        assertThat(order.getStatus()).isEqualTo(OrderStatus.SEND);
    }

    @ParameterizedTest
    @ValueSource(longs = {4L, 5L})
    void 자신의_주문이_아니면_주문_확정을_할_수_없다(long orderId){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        //then(검증)
        assertThatThrownBy(() -> orderService.orderConfirmNow(currentUser, orderId))
                .isInstanceOf(ApiException.class);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void 자신의_주문을_취소_할_수_있다(long orderId){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        orderService.cancelOrder(currentUser, orderId);
        Order order = fakeOrderRepository.getByIdWithThrow(orderId);

        //then(검증)
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @ParameterizedTest
    @ValueSource(longs = {4L, 5L})
    void 자신의_주문이_아니면_주문_취소를_할_수_없다(long orderId){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        //when(상황발생)
        //then(검증)
        assertThatThrownBy(() -> orderService.cancelOrder(currentUser, orderId))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void 자신의_주문에_일부_이력서_주문을_취소_할_수_있다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        Resume resume1 = Resume.builder()
                .price(new BigDecimal("40000.00"))
                .build();

        Resume resume2 = Resume.builder()
                .price(new BigDecimal("22000.00"))
                .build();

        List<OrderResume> fakeOrderResumes = List.of(
                OrderResume.builder()
                        .id(1L)
                        .resume(resume1)
                        .status(OrderResumeStatus.ORDERED)
                        .build(),
                OrderResume.builder()
                        .id(2L)
                        .resume(resume2)
                        .status(OrderResumeStatus.ORDERED)
                        .build()
        );

        PartialCancelRequest partialCancelRequest = PartialCancelRequest.builder()
                .orderResumeIds(List.of(1L, 2L)) // 선택한 이력서장바구니
                .build();

        // Mock 설정: cancelPartialOrderResumes 호출 시 fakeOrderResumes 반환
        when(orderResumeService.cancelPartialOrderResumes(partialCancelRequest.getOrderResumeIds()))
                .thenReturn(fakeOrderResumes);

        // when
        orderService.cancelPartialOrder(currentUser, 1L, partialCancelRequest);
        Order order = fakeOrderRepository.getByIdWithThrow(1L);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PARTIAL_CANCEL);
        assertThat(order.getTotalAmount()).isEqualTo(new BigDecimal("34000.00"));  // 예상 금액
    }

    @Test
    void 자신의_주문의_ID와_CurrentUser를_통해_OrderDetailResponse를_생성_할_수_있다(){
        //given(상황환경 세팅)
        // user1(1L)이 주문한 order3(3L)의 이력서1(1L)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        List<OrderResumeResponse> orderResumeResponses = List.of(
                OrderResumeResponse.builder()
                        .orderId(1L)
                        .title("JUNIOR AI 이력서 #1")
                        .status(OrderResumeStatus.CONFIRM)
                        .price(new BigDecimal("40000.00"))
                        .orderedAt(LocalDateTime.of(2024, 9, 16, 12, 0, 0))
                        .build()
        );

        //when(상황발생)
        when(orderResumeQueryDslRepository.findByOrderId(3L)).thenReturn(orderResumeResponses);
        OrderDetailResponse orderDetail = orderService.getOrderDetail(currentUser, 3L);

        //then(검증)
        assertThat(orderDetail.getEmail()).isEqualTo("user1@naver.com");
        assertThat(orderDetail.getTotalPrice()).isEqualTo(new BigDecimal("40000.00"));
        assertThat(orderDetail.getOrderedAt()).isEqualTo(LocalDateTime.of(2024, 9, 16, 12, 0, 0));
        assertThat(orderDetail.getOrderId()).isEqualTo(3L);
        assertThat(orderDetail.getOrderResumeResponses()).isEqualTo(orderResumeResponses);
    }
}