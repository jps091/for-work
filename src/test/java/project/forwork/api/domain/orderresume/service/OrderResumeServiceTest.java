package project.forwork.api.domain.orderresume.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(MockitoExtension.class)
class OrderResumeServiceTest {

    @Mock
    private FakeOrderResumeRepository fakeOrderResumeRepository;
    private OrderResumeService orderResumeService;
    private FakeCartResumeRepository fakeCartResumeRepository;
    @Mock
    private OrderResumeProducer orderResumeProducer;

    @BeforeEach
    void init(){
        TestClockHolder testClockHolder = new TestClockHolder(LocalDateTime.of(2024, 8, 8, 12, 0, 0));
        fakeOrderResumeRepository = new FakeOrderResumeRepository();
        fakeCartResumeRepository = new FakeCartResumeRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.orderResumeService = OrderResumeService.builder()
                .orderResumeRepository(fakeOrderResumeRepository)
                .clockHolder(testClockHolder)
                .cartResumeRepository(fakeCartResumeRepository)
                .orderResumeProducer(orderResumeProducer)
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
                .status(ResumeStatus.PENDING)
                .build();

        Resume resume3 = Resume.builder()
                .id(3L)
                .seller(user1)
                .field(FieldType.BACKEND)
                .level(LevelType.SENIOR)
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("98000.00"))
                .description("test resume3")
                .status(ResumeStatus.PENDING)
                .build();

        Order order1 = Order.builder().id(1L).user(user1).build();
        Order order2 = Order.builder().id(2L).user(user1).build();
        Order order3 = Order.builder().id(3L).user(user1).build();


        OrderResume orderResume1 = OrderResume.builder()
                .id(1L)
                .order(order1)
                .resume(resume1)
                .status(OrderResumeStatus.PAID)
                .build();

        OrderResume orderResume2 = OrderResume.builder()
                .id(2L)
                .order(order1)
                .resume(resume1)
                .status(OrderResumeStatus.PAID)
                .build();

        OrderResume orderResume3 = OrderResume.builder()
                .id(3L)
                .order(order2)
                .resume(resume2)
                .status(OrderResumeStatus.CANCEL)
                .build();

        OrderResume orderResume4 = OrderResume.builder()
                .id(4L)
                .order(order2)
                .resume(resume1)
                .status(OrderResumeStatus.CANCEL)
                .build();

        OrderResume orderResume5 = OrderResume.builder()
                .id(5L)
                .order(order3)
                .resume(resume1)
                .status(OrderResumeStatus.PAID)
                .build();

        OrderResume orderResume6 = OrderResume.builder()
                .id(6L)
                .order(order3)
                .resume(resume2)
                .status(OrderResumeStatus.CANCEL)
                .build();

        OrderResume orderResume7 = OrderResume.builder()
                .id(7L)
                .order(order3)
                .resume(resume3)
                .status(OrderResumeStatus.PAID)
                .build();

        fakeOrderResumeRepository.save(orderResume1);
        fakeOrderResumeRepository.save(orderResume2);
        fakeOrderResumeRepository.save(orderResume3);
        fakeOrderResumeRepository.save(orderResume4);
        fakeOrderResumeRepository.save(orderResume5);
        fakeOrderResumeRepository.save(orderResume6);
        fakeOrderResumeRepository.save(orderResume7);
    }

    @Test
    void Resume와_Order로_OrderResume을_생성할_수_있다(){
        //given(상황환경 세팅)
        Resume resume = Resume.builder()
                .id(1L)
                .build();
        Order order = Order.builder()
                .id(1L)
                .build();

        //when(상황발생)
        //then(검증)
        assertThatCode(() -> orderResumeService.createByResumes(order, List.of(resume)))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(longs = {3L, 4L, 6L})
    void CANCEL_상태인_ORDER_RESUME은_SENT_상태로_변경할_수_없다(long orderResumeId){
        //given(상황환경 세팅)
        Order order1 = Order.builder()
                .id(1L)
                .build();
        Order order2 = Order.builder()
                .id(2L)
                .build();
        Order order3 = Order.builder()
                .id(3L)
                .build();

        //when(상황발생)
        orderResumeService.sendMailForAutoConfirmedOrder(List.of(order1, order2, order3));
        OrderResume orderResume = fakeOrderResumeRepository.getByIdWithThrow(orderResumeId);

        //then(검증)
        assertThat(orderResume.getStatus()).isEqualTo(OrderResumeStatus.CANCEL);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 5L, 7L})
    void 여러건에_주문에_속한_ORDER_상태인_주문이력서_CONFIRM로_변경할_수_있다(long orderResumeId){
        //given(상황환경 세팅)
        Order order1 = Order.builder()
                .id(1L)
                .build();
        Order order2 = Order.builder()
                .id(2L)
                .build();
        Order order3 = Order.builder()
                .id(3L)
                .build();

        //when(상황발생)
        orderResumeService.sendMailForAutoConfirmedOrder(List.of(order1, order2, order3));
        OrderResume orderResume = fakeOrderResumeRepository.getByIdWithThrow(orderResumeId);

        //then(검증)
        assertThat(orderResume.getStatus()).isEqualTo(OrderResumeStatus.CONFIRM);
    }

    @Test
    void 주문에_속해_있는_orderResume_을_전부_구매확정_하면_주문은_CONFRIM_으로_변경_된다(){
        //given(상황환경 세팅)
        User user1 = User.builder()
                .id(1L)
                .build();

        Order order1 = Order.builder()
                .id(1L)
                .user(user1)
                .status(OrderStatus.PAID)
                .build();
        List<Long> orderResumeIds = List.of(1L, 2L); // order1 에속한 order-resume-id

        //when(상황발생)
        Order newOrder = orderResumeService.sendMailForNowConfirmedOrder(1L, order1, orderResumeIds);

        //then(검증)
        assertThat(newOrder.getStatus()).isEqualTo(OrderStatus.CONFIRM);
    }
    @Test
    void 주문에_속해_있는_orderResume_을_부분_구매확정_하면_주문은_PARTIAL_CONFRIM_으로_변경_된다(){
        //given(상황환경 세팅)
        User user1 = User.builder()
                .id(1L)
                .build();
        Order order1 = Order.builder()
                .id(1L)
                .user(user1)
                .status(OrderStatus.PAID)
                .build();
        List<Long> orderResumeIds = List.of(1L); // order1 에속한 order-resume-id

        //when(상황발생)
        Order newOrder = orderResumeService.sendMailForNowConfirmedOrder(1L, order1, orderResumeIds);

        //then(검증)
        assertThat(newOrder.getStatus()).isEqualTo(OrderStatus.PARTIAL_CONFIRM);
    }
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void 주문취소를_하면_주문에_속한_ORDER_REUME_상태가_전부_CANCEL_변경_된다(long orderResumeId){
        //given(상황환경 세팅)
        Order order1 = Order.builder()
                .id(1L)
                .build();

        //when(상황발생)
        orderResumeService.cancelByOrder(order1);
        OrderResume orderResume = fakeOrderResumeRepository.getByIdWithThrow(orderResumeId);

        //then(검증)
        assertThat(orderResume.getStatus()).isEqualTo(OrderResumeStatus.CANCEL);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void 선택된_ORDER_REUME_상태만_CANCEL_변경할_수_있다(long orderResumeId){
        //given(상황환경 세팅)
        OrderResume orderResume1 = fakeOrderResumeRepository.getByIdWithThrow(1);
        OrderResume orderResume2 = fakeOrderResumeRepository.getByIdWithThrow(2);
        List<OrderResume> orderResumes = List.of(orderResume1, orderResume2);

        //when(상황발생)
        orderResumeService.cancelByOrderResumes(orderResumes);

        //then(검증)
        OrderResume newOrderResume = fakeOrderResumeRepository.getByIdWithThrow(orderResumeId);
        assertThat(newOrderResume.getStatus()).isEqualTo(OrderResumeStatus.CANCEL);
    }
}