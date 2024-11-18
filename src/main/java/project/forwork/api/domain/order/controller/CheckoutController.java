package project.forwork.api.domain.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.order.controller.model.*;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.CheckoutService;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
@Tag(name = "CheckoutController", description = "Checkout Api 서비스 컨트롤러")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Operation(summary = "결제 요청 api", description = "결제 요청시 주문도 같이 생성 됩니다.")
    @PostMapping("/confirm")
    public Api<ConfirmResponse> confirm(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @Valid @RequestBody ConfirmPaymentRequest body
    ){
        ConfirmResponse confirmResponse = checkoutService.processOrderAndPayment(currentUser, body);
        return Api.OK(confirmResponse);
    }

    @Operation(summary = "주문 전체 취소 및 전액 환불 api",
            description = "OrderId 에 해당 하는 이력서 주문을 전체 취소하고 전액 환불 합니다.")
    @PostMapping("/cancel/{orderId}")
    public Api<CancelResponse> cancelOrder(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long orderId
    ){
        CancelResponse cancelResponse = checkoutService.cancelPayment(currentUser, orderId);
        return Api.OK(cancelResponse);
    }

    @Operation(summary = "주문 중 부분 취소 및 부분 환불 api",
            description = "OrderId 에 해당 하는 이력서 주문중 선택한 것만 부분 취소하고 해당 금액을 환불 합니다.")
    @PostMapping("/partial-cancel/{orderId}")
    public Api<CancelResponse> cancelPartialOrder(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long orderId,
            @Valid @RequestBody PartialCancelRequest body
    ){
        CancelResponse cancelResponse = checkoutService.cancelPartialPayment(currentUser, orderId, body);
        return Api.OK(cancelResponse);
    }
}