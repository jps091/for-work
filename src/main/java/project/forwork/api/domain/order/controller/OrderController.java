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
import project.forwork.api.domain.order.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "OrderApiController", description = "Order Api 서비스 컨트롤러")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 내역 전체 api",
            description = "사용자가 주문한 내역을 전부 조회 할 수 있습니다.")
    @GetMapping
    public Api<List<OrderResponse>> findAll(
            @Parameter(hidden = true) @Current CurrentUser currentUser
    ){
        List<OrderResponse> orderResponses = orderService.findAll(currentUser);
        return Api.OK(orderResponses);
    }

    @Operation(summary = "주문 상세 api",
            description = "주문에 포함된 이력서 주문 내역을 전부 조회 할 수 있습니다.")
    @GetMapping("/{orderId}")
    public Api<OrderDetailResponse> getOrderDetail(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long orderId
    ){
        OrderDetailResponse orderDetail = orderService.getOrderDetail(currentUser, orderId);
        return Api.OK(orderDetail);
    }

    @Operation(summary = "즉시 구매 확정 api",
            description = "OrderId 에 해당 하는 주문 상태를 Confirm 으로 변경하고 구매 이력서 메일을 전송 합니다.")
    @PostMapping("/confirm/{orderId}")
    public Api<String> confirmOrderNow(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long orderId,
            @Valid @RequestBody ConfirmOrderRequest body
    ){
        orderService.orderConfirmNow(currentUser, orderId, body);
        return Api.OK("구매 확정이 완료 되었습니다. 이메일을 확인 해주세요");
    }
}
