package project.forwork.api.domain.order.service

import org.slf4j.Logger
import project.forwork.api.common.domain.CurrentUser
import project.forwork.api.common.error.TransactionErrorCode
import project.forwork.api.common.exception.ApiException
import project.forwork.api.domain.cartresume.service.CartResumeService
import project.forwork.api.domain.order.controller.model.ConfirmPaymentRequest
import project.forwork.api.domain.order.infrastructure.model.ConfirmPaymentDto
import project.forwork.api.domain.order.model.Order
import project.forwork.api.domain.retrylog.infrastructure.enums.RetryType
import project.forwork.api.domain.retrylog.service.RetryLogService
import project.forwork.api.domain.transaction.infrastructure.enums.TransactionType
import project.forwork.api.domain.transaction.model.Transaction
import project.forwork.api.domain.transaction.service.port.TransactionRepository
import project.forwork.api.domain.user.infrastructure.enums.UserStatus
import spock.lang.Specification

class CheckoutServiceTest extends Specification {

    CheckoutService checkoutService;
    PaymentGatewayService pgService = Mock()
    TransactionRepository transactionRepository = Mock()
    OrderService orderService = Mock()
    RetryLogService retryLogService = Mock()
    CartResumeService cartResumeService = Mock()

    CurrentUser currentUser;

    def setup() {
        checkoutService = CheckoutService.builder()
                .pgService(pgService)
                .transactionRepository(transactionRepository)
                .orderService(orderService)
                .retryLogService(retryLogService)
                .cartResumeService(cartResumeService)
                .build();

        currentUser = CurrentUser.builder()
                .id(1L)
                .name("test")
                .email("test@naver.com")
                .status(UserStatus.USER)
                .build()
    }

    def "ConfirmPaymentRequest 로 주문 생성 및 결제 요청을 할 수 있다."() {
        given:
        var body = ConfirmPaymentRequest.builder()
                .resumeIds(List.of(1L))
                .paymentKey("paymentKey")
                .requestId("requestId")
                .amount(new BigDecimal(10_000))
                .build();

        var order = Order.builder()
                .totalAmount(body.getAmount())
                .id(1L)
                .build()

        var dto = ConfirmPaymentDto.builder()
                .paymentKey(body.getPaymentKey())
                .orderId(body.getRequestId())
                .amount(body.getAmount().toString())
                .build()

        var tx = Transaction.builder()
                .userEmail(currentUser.getEmail())
                .requestId(body.getRequestId())
                .paymentKey(body.getPaymentKey())
                .amount(body.getAmount())
                .transactionType(TransactionType.PAYMENT)
                .build();

        ConfirmPaymentDto.from(body) >> dto
        pgService.confirm(dto) >> null
        orderService.create(currentUser, body) >> order
        cartResumeService.deleteByPaidResumeIds(currentUser, body.getResumeIds()) >> null
        Transaction.create(currentUser, body.getRequestId(), body.getPaymentKey(), body.getAmount(), TransactionType.PAYMENT) >> tx

        when:
        var response = checkoutService.processOrderAndPayment(currentUser, body)

        then:
        response.orderId == order.getId()
        response.totalAmount == order.getTotalAmount()
        response.orderCount == body.getResumeIds().size()
    }

    def "ConfirmPaymentRequest, CurrentUser 로 주문 생성을 할 수 있다."() {
        given:
        var body = ConfirmPaymentRequest.builder()
                .resumeIds(List.of(1L))
                .paymentKey("paymentKey")
                .requestId("requestId")
                .amount(new BigDecimal(10_000))
                .build();

        var order = Order.builder()
                .totalAmount(body.getAmount())
                .id(1L)
                .build()

        orderService.create(currentUser, body) >> order

        when:
        var response = checkoutService.processOrderAndPayment(currentUser, body)

        then:
        response.orderId == order.getId()
        response.totalAmount == order.getTotalAmount()
        response.orderCount == body.getResumeIds().size()
    }

    def "ConfirmPaymentRequest 로 결제 요청을 할 수 있다."() {
        given:
        var body = ConfirmPaymentRequest.builder()
                .resumeIds(List.of(1L))
                .paymentKey("paymentKey")
                .requestId("requestId")
                .amount(new BigDecimal(10_000))
                .build();

        var dto = ConfirmPaymentDto.builder()
                .paymentKey(body.getPaymentKey())
                .orderId(body.getRequestId())
                .amount(body.getAmount().toString())
                .build()

        var order = Order.builder()
                .totalAmount(body.getAmount())
                .id(1L)
                .build()


        ConfirmPaymentDto.from(body) >> dto
        orderService.create(currentUser, body) >> order


        when:
        checkoutService.processOrderAndPayment(currentUser, body)

        then:
        1 * pgService.confirm({ confirmDto ->
            confirmDto.paymentKey == dto.getPaymentKey() &&
                    confirmDto.orderId == dto.getOrderId() &&
                    confirmDto.amount == dto.getAmount()
        })
    }

    def "이력서 결제를 성공하면 해당 이력서는 장바구니에서 삭제 된다."() {
        given:
        var body = ConfirmPaymentRequest.builder()
                .resumeIds(List.of(1L))
                .paymentKey("paymentKey")
                .requestId("requestId")
                .amount(new BigDecimal(10_000))
                .build();

        var order = Order.builder()
                .totalAmount(body.getAmount())
                .id(1L)
                .build()

        orderService.create(currentUser, body) >> order

        when:
        checkoutService.processOrderAndPayment(currentUser, body)

        then:
        1 * cartResumeService.deleteByPaidResumeIds(currentUser, body.getResumeIds())
    }

    def "이력서 결제를 성공하면 결제 내역이 Transaction 테이블에 저장 된다."() {
        given:
        var body = ConfirmPaymentRequest.builder()
                .resumeIds(List.of(1L))
                .paymentKey("paymentKey")
                .requestId("requestId")
                .amount(new BigDecimal(10_000))
                .build();

        var order = Order.builder()
                .totalAmount(body.getAmount())
                .id(1L)
                .build()

        var tx = Transaction.builder()
                .userEmail(currentUser.getEmail())
                .requestId(body.getRequestId())
                .paymentKey(body.getPaymentKey())
                .amount(body.getAmount())
                .transactionType(TransactionType.PAYMENT)
                .build();

        orderService.create(currentUser, body) >> order
        //Transaction.create(currentUser, body.getRequestId(), body.getPaymentKey(), body.getAmount(), TransactionType.PAYMENT) >> tx;
        Transaction.create(_, _, _, _, _) >> tx;

        when:
        checkoutService.processOrderAndPayment(currentUser, body)

        then:
        1 * transactionRepository.save({ savedTx ->
            savedTx.userEmail == tx.userEmail &&
                    savedTx.requestId == tx.requestId &&
                    savedTx.paymentKey == tx.paymentKey &&
                    savedTx.amount == tx.amount &&
                    savedTx.transactionType == tx.transactionType
        });
    }

    def "주문생성 결제 요청에서 결제 실패시 예외가 발생 된다"() {
        given:
        var body = ConfirmPaymentRequest.builder()
                .resumeIds(List.of(1L))
                .paymentKey("paymentKey")
                .requestId("requestId")
                .amount(new BigDecimal(10_000))
                .build();

        var order = Order.builder()
                .totalAmount(body.getAmount())
                .id(1L)
                .build()

        var ex = new ApiException(TransactionErrorCode.SERVER_ERROR)

        orderService.create(currentUser, body) >> order
        pgService.confirm(_  as ConfirmPaymentDto) >> {throw ex}

        when:
        checkoutService.processOrderAndPayment(currentUser, body)

        then:
        def thrownException = thrown(ApiException);
        thrownException.message == "시스템 오류입니다. 잠시후 다시 시도 해주세요."
    }
}