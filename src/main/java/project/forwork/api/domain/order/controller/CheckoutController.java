package project.forwork.api.domain.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.order.controller.model.*;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.OrderService;
import project.forwork.api.domain.order.service.CheckoutService;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
@Tag(name = "CheckoutController", description = "Checkout Api 서비스 컨트롤러")
public class CheckoutController {

    private final OrderService orderService;
    private final CheckoutService checkoutService;

    @Operation(summary = "즉시 주문 api", description = "입력한 판매글 ID 에서 즉시 구매")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/now/{salesPostId}")
    public Api<OrderCreateResponse> orderNow(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long salesPostId
    ){
        Order order = orderService.orderNow(currentUser, salesPostId);
        return Api.CREATED(OrderCreateResponse.from(order));
    }

    @Operation(summary = "장바구니 선택 주문 api", description = "장바구니에서 선택한 cart-resume 구매")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/cart")
    public Api<OrderCreateResponse> orderInCart(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @Valid @RequestBody OrderInCartRequest body
    ){
        Order order = orderService.orderInCart(currentUser, body);
        return Api.CREATED(OrderCreateResponse.from(order));
    }

    @Operation(summary = "결제 요청 api", description = "주문에 대한 결제 요청")
    @PostMapping("/confirm")
    public Api<String> confirm(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @RequestBody ConfirmPaymentRequest body
    ){
        checkoutService.processOrderAndPayment(currentUser, body);
        return Api.OK("결제 승인 성공");
    }

    @Operation(summary = "주문 전체 취소 및 전액 환불 api",
            description = "OrderId 에 해당 하는 이력서 주문을 전체 취소하고 전액 환불 합니다.")
    @PostMapping("/cancel/{orderId}")
    public Api<String> cancelOrder(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long orderId
    ){
        checkoutService.cancelPayment(currentUser, orderId);
        return Api.OK("주문 전체 취소 되었습니다.");
    }

    @Operation(summary = "주문 중 부분 취소 및 부분 환불 api",
            description = "OrderId 에 해당 하는 이력서 주문중 선택한 것만 부분 취소하고 해당 금액을 환불 합니다.")
    @PostMapping("/partial-cancel/{orderId}")
    public Api<String> cancelPartialOrder(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long orderId,
            @Valid @RequestBody PartialCancelRequest body
    ){
        checkoutService.cancelPartialPayment(currentUser, orderId, body);
        return Api.OK("선택한 이력서 부분 취소 되었습니다.");
    }
}
