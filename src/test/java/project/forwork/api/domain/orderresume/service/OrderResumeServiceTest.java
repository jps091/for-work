package project.forwork.api.domain.orderresume.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.FakeMailSender;
import project.forwork.api.mock.FakeOrderResumeRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(MockitoExtension.class)
class OrderResumeServiceTest {

    @Mock
    private OrderResumeRepositoryCustom orderResumeRepositoryCustom;
    private OrderResumeService orderResumeService;

    @BeforeEach
    void init(){
        FakeOrderResumeRepository fakeOrderResumeRepository = new FakeOrderResumeRepository();
        FakeMailSender fakeMailSender = new FakeMailSender();
        this.orderResumeService = OrderResumeService.builder()
                .orderResumeRepository(fakeOrderResumeRepository)
                .sendPurchaseResumeService(new SendPurchaseResumeService(orderResumeRepositoryCustom, fakeMailSender))
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

        Order order1 = Order.builder().id(1L).build();
        Order order2 = Order.builder().id(2L).build();
        Order order3 = Order.builder().id(3L).build();


        OrderResume orderResume1 = OrderResume.builder()
                .id(1L)
                .order(order1)
                .resume(resume1)
                .status(OrderResumeStatus.ORDERED)
                .build();

        OrderResume orderResume2 = OrderResume.builder()
                .id(2L)
                .order(order1)
                .resume(resume1)
                .status(OrderResumeStatus.ORDERED)
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
                .status(OrderResumeStatus.ORDERED)
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
                .status(OrderResumeStatus.ORDERED)
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
    void CartResume와_Order로_OrderResume을_생성할_수_있다(){
        //given(상황환경 세팅)
        Resume resume = Resume.builder()
                .id(1L)
                .build();
        CartResume cartResume = CartResume.builder()
                .id(1L)
                .resume(resume)
                .build();
        Order order = Order.builder()
                .id(1L)
                .build();

        //when(상황발생)
        //then(검증)
        assertThatCode(() -> orderResumeService.registerByCartResume(order, List.of(cartResume)))
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
        orderResumeService.sendMailForConfirmedOrders(List.of(order1, order2, order3));
        OrderResume orderResume = orderResumeService.getByIdWithThrow(orderResumeId);

        //then(검증)
        assertThat(orderResume.getStatus()).isEqualTo(OrderResumeStatus.CANCEL);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 5L, 7L})
    void 여러건에_주문에_속한_ORDER_상태인_주문이력서_SENT로_변경할_수_있다(long orderResumeId){
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
        orderResumeService.sendMailForConfirmedOrders(List.of(order1, order2, order3));
        OrderResume orderResume = orderResumeService.getByIdWithThrow(orderResumeId);

        //then(검증)
        assertThat(orderResume.getStatus()).isEqualTo(OrderResumeStatus.SENT);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void 주문_1건에_속한_주문이력서_상태를_SENT로_변경할_수_있다(long orderResumeId){
        //given(상황환경 세팅)
        Order order1 = Order.builder()
                .id(1L)
                .build();

        //when(상황발생)
        orderResumeService.sendMailForConfirmedOrder(order1);
        OrderResume orderResume = orderResumeService.getByIdWithThrow(orderResumeId);

        //then(검증)
        assertThat(orderResume.getStatus()).isEqualTo(OrderResumeStatus.SENT);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void 주문취소를_하면_주문에_속한_ORDER_REUME_상태가_전부_CANCEL_변경_된다(long orderResumeId){
        //given(상황환경 세팅)
        Order order1 = Order.builder()
                .id(1L)
                .build();

        //when(상황발생)
        orderResumeService.cancel(order1);
        OrderResume orderResume = orderResumeService.getByIdWithThrow(orderResumeId);

        //then(검증)
        assertThat(orderResume.getStatus()).isEqualTo(OrderResumeStatus.CANCEL);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void 선택된_ORDER_REUME_상태만_CANCEL_변경할_수_있다(long orderResumeId){
        //given(상황환경 세팅)

        //when(상황발생)
        orderResumeService.cancelPartialOrderResumes(List.of(1L, 2L), 1L);
        OrderResume orderResume = orderResumeService.getByIdWithThrow(orderResumeId);

        //then(검증)
        assertThat(orderResume.getStatus()).isEqualTo(OrderResumeStatus.CANCEL);
    }
}