package project.forwork.api.domain.order.service

import project.forwork.api.common.domain.CurrentUser
import project.forwork.api.common.exception.ApiException
import project.forwork.api.common.service.port.ClockHolder
import project.forwork.api.common.service.port.UuidHolder
import project.forwork.api.domain.order.controller.model.ConfirmOrderRequest
import project.forwork.api.domain.order.controller.model.ConfirmPaymentRequest
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus
import project.forwork.api.domain.order.model.Order
import project.forwork.api.domain.order.service.port.OrderRepository
import project.forwork.api.domain.orderresume.controller.model.OrderTitleResponse
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus
import project.forwork.api.domain.orderresume.model.OrderResume
import project.forwork.api.domain.orderresume.service.OrderResumeService
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom
import project.forwork.api.domain.resume.model.Resume
import project.forwork.api.domain.resume.service.port.ResumeRepository
import project.forwork.api.domain.user.infrastructure.enums.UserStatus
import project.forwork.api.domain.user.model.User
import project.forwork.api.domain.user.service.port.UserRepository
import project.forwork.api.mock.TestClockHolder
import project.forwork.api.mock.TestUuidHolder
import spock.lang.Specification

import java.time.LocalDateTime

class OrderServiceTest extends Specification {
    OrderService orderService
    OrderResumeRepositoryCustom orderResumeRepositoryCustom = Mock()
    UserRepository userRepository = Mock()
    ResumeRepository resumeRepository = Mock()
    OrderResumeService orderResumeService = Mock()
    OrderRepository orderRepository = Mock()
    ClockHolder clockHolder
    UuidHolder uuidHolder

    CurrentUser currentUser;

    def setup(){
        clockHolder = new TestClockHolder(LocalDateTime.of(2024, 8, 8, 12, 0, 0))
        uuidHolder = new TestUuidHolder("gggggg-gggggg-qqqqqq")
        orderService = OrderService.builder()
            .uuidHolder(uuidHolder)
            .clockHolder(clockHolder)
            .orderRepository(orderRepository)
            .orderResumeRepositoryCustom(orderResumeRepositoryCustom)
            .userRepository(userRepository)
            .resumeRepository(resumeRepository)
            .orderResumeService(orderResumeService)
            .build();

        currentUser = CurrentUser.builder()
                .id(1L)
                .name("test")
                .email("test@naver.com")
                .status(UserStatus.USER)
                .build()
    }

    def "resume_id 로 주문 생성 요청시 order-resume 이 생성되고 주문이 생성된다."(){
        given:
        var request = new ConfirmPaymentRequest(List.of(1L, 2L), "paymentKey", "requestId", new BigDecimal(1_0000));
        var user = User.builder().id(currentUser.getId()).build()
        var resumes = List.of(Resume.builder().id(1L).build(),
                Resume.builder().id(2L).build())
        var saveOrder = Order.builder()
                .id(1L)
                .totalAmount(request.getAmount())
                .requestId(request.getRequestId())
                .user(user)
                .build();

        userRepository.getByIdWithThrow(_) >> user
        resumeRepository.findByIds(request.getResumeIds()) >> resumes
        orderRepository.save(_) >> saveOrder

        when:
        var result = orderService.create(currentUser, request)

        then:
        result.id == 1L
        result.user == user
        result.totalAmount == request.getAmount()
        result.requestId == request.getRequestId()
    }

    def "구매자가 PAID 상태인 주문Id, ConfirmOrderRequest 를 가지고 즉시 주문 확정을 할 수 있다."(){
        given:
        var body = new ConfirmOrderRequest(List.of(1L, 2L))
        var user = User.builder()
                .id(1L)
                .build()
        var order = Order.builder()
                .id(1L)
                .status(OrderStatus.PAID)
                .user(user)
                .build();
        var updateOrder = Order.builder()
                .id(1L)
                .status(OrderStatus.CONFIRM)
                .user(user)
                .build();
        orderRepository.getByIdWithThrow(_) >>  order
        orderResumeService.sendMailForNowConfirmedOrder(_, _, _) >> updateOrder

        when:
        orderService.orderConfirmNow(currentUser, 1L, body)

        then:
        1 * orderResumeService.sendMailForNowConfirmedOrder(currentUser.getId(), order, body.getOrderResumeIds()) >> updateOrder
        assert updateOrder.status == OrderStatus.CONFIRM
    }

    def "자신의 주문이 아닌 주문에 대해 구매확정을 하면 ApiException 이 발생한다."() {
        given:
        var body = new ConfirmOrderRequest(List.of(1L, 2L))
        var user = User.builder()
                .id(2L)
                .build()
        var order = Order.builder()
                .id(1L)
                .status(OrderStatus.PAID)
                .user(user)
                .build()
        orderRepository.getByIdWithThrow(_) >> order

        when:
        orderService.orderConfirmNow(currentUser, 1L, body)

        then:
        def e = thrown(ApiException.class)
    }

    def "주문 ID를 가지고 주문 취소를 할 수 있다."(){
        given:
        var user = User.builder()
                .id(1L)
                .build()
        var order = Order.builder()
                .id(1L)
                .user(user)
                .status(OrderStatus.PAID)
                .build()
        var canceledOrder = Order.builder()
                .id(1L)
                .user(user)
                .status(OrderStatus.CANCEL)
                .build()

        order.cancelOrderWithThrow(_) >> canceledOrder

        when:
        var result = orderService.cancelOrder(currentUser, order)

        then:
        1 * orderResumeService.cancelByOrder({ cancelOrder ->
            cancelOrder.id == 1L &&
                    cancelOrder.status == OrderStatus.CANCEL
        })

        // 저장 호출 검증
        1 * orderRepository.save({ savedOrder ->
            savedOrder.id == 1L &&
                    savedOrder.status == OrderStatus.CANCEL
        })
    }

    def "여러 주문을 동시에 원하는 상태로 변경할 수 있다."(){
        given:
        var order1 = Order.builder()
                .id(1L)
                .status(OrderStatus.PAID)
                .build()
        var order2 = Order.builder()
                .id(2L)
                .status(OrderStatus.PAID)
                .build()
        var updatedOrder1 = Order.builder().id(1L).status(OrderStatus.CONFIRM).build()
        var updatedOrder2 = Order.builder().id(2L).status(OrderStatus.CONFIRM).build()
        var orders = [order1, order2]
        var updatedOrders = [updatedOrder1, updatedOrder2]

        orderRepository.saveAll(_) >> updatedOrders

        when:
        var result = orderService.updateOrdersByStatus(orders, OrderStatus.CONFIRM)

        then:
        result.size() == 2
        result.every{it.status == OrderStatus.CONFIRM}
    }

    def "같은 유저가 주문 요청을 5초 이내로 반복 하면 예외를 발생 시킨다."(){
        given:
        var firstRequestId = "3900_1-12345"
        var secondRequestId = "4900_1-54321"
        var order = Order.builder()
                .id(1L)
                .requestId(firstRequestId)
                .build()

        orderRepository.findByRequestId(secondRequestId) >> Optional.of(order)

        when:
        orderService.validRequestId(secondRequestId)

        then:
        var e = thrown(ApiException.class)
    }

    def "같은 유저가 주문 요청을 5초 이후로 반복 하면 예외를 발생 시키지 않는다."(){
        given:
        var firstRequestId = "3900_1-12345"
        var secondRequestId = "5100_1-54321"
        var order = Order.builder()
                .id(1L)
                .requestId(firstRequestId)
                .build()

        orderRepository.findByRequestId(secondRequestId) >> Optional.of(order)

        when:
        orderService.validRequestId(secondRequestId)

        then:
        noExceptionThrown()
    }

    def "다른 유저가 주문 요청을 5초 이내로 반복 하면 예외를 발생 시키지 않는다."(){
        given:
        var firstRequestId = "3900_1-12345"
        var secondRequestId = "4900_2-54321"
        var order = Order.builder()
                .id(1L)
                .requestId(firstRequestId)
                .build()

        orderRepository.findByRequestId(secondRequestId) >> Optional.of(order)

        when:
        orderService.validRequestId(secondRequestId)

        then:
        noExceptionThrown()
    }

    def "CurrentUser 로 자신의 주문 전체 조회를 할 수 있다."(){
        given:
        var user = User.builder()
                .id(1L)
                .build()
        var order1 = Order.builder()
                .id(1L)
                .user(user)
                .status(OrderStatus.CONFIRM)
                .build()
        var order2 = Order.builder()
                .id(2L)
                .user(user)
                .status(OrderStatus.PARTIAL_CONFIRM)
                .build()
        var titles1 = [new OrderTitleResponse("order1 Title")]
        var titles2 = [new OrderTitleResponse("order2 Title"), new OrderTitleResponse("order3 Title")]

        orderRepository.findByUserId(currentUser.getId()) >> [order1, order2]
        orderResumeRepositoryCustom.findOrderTitleByOrderId(1L) >> titles1
        orderResumeRepositoryCustom.findOrderTitleByOrderId(2L) >> titles2

        when:
        var orders = orderService.findAll(currentUser)

        then:
        orders.size() == 2
        orders.get(0).orderTitle == "order1 Title"
        orders.get(1).orderTitle == "order2 Title 외 1건"
    }

    def "주문 내역이 없는 유저가 주문조회를 할 경우 예외가 발생한다."(){
        given:
        orderRepository.findByUserId(currentUser.getId()) >> []

        when:
        orderService.findAll(currentUser)

        then:
        thrown(ApiException.class)
    }



    def "자신의 주문의 ID로 상세 내역을 확인 할 수 있다."(){
        given:
        var user = User.builder()
                .id(1L)
                .email("test@mail.com")
                .build()
        var order = Order.builder()
                .id(1L)
                .user(user)
                .status(OrderStatus.CONFIRM)
                .build()
        var orderResume1 = OrderResume.builder()
                .id(1L)
                .status(OrderResumeStatus.CONFIRM)
                .build()
        var orderResume2 = OrderResume.builder()
                .id(1L)
                .status(OrderResumeStatus.CONFIRM)
                .build()

        orderRepository.getOrderWithThrow(currentUser.getId(), 1L) >> order
        orderResumeRepositoryCustom.findByOrderId(1L) >> [orderResume1, orderResume2]

        when:
        var result = orderService.getOrderDetail(currentUser, 1L)

        then:
        result.getEmail() == "test@mail.com"
        result.getOrderId() == 1L
        result.getOrderResumeResponses().size() == 2
    }
}
