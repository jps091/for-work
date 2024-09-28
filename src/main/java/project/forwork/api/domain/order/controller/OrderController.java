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

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "OrderApiController", description = "Order Api 서비스 컨트롤러")
public class OrderController {

    private final OrderService orderService;

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
            @Valid @RequestBody OrderRequest body
    ){
        Order order = orderService.orderInCart(currentUser, body);
        return Api.CREATED(OrderCreateResponse.from(order));
    }

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
            @PathVariable Long orderId
    ){
        orderService.confirmOrderNow(currentUser, orderId);
        return Api.OK("구매 확정이 완료 되었습니다. 이메일을 확인 해주세요");
    }

    @Operation(summary = "주문 전체 취소 api",
            description = "OrderId 에 해당 하는 이력서 주문을 전체 취소 합니다.")
    @PostMapping("/cancel/{orderId}")
    public Api<String> cancelOrder(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long orderId
    ){
        orderService.cancelOrder(currentUser, orderId);
        return Api.OK("주문 전체 취소 되었습니다.");
    }

    @Operation(summary = "주문 중 부분 취소 api",
            description = "OrderId 에 해당 하는 이력서 주문중 선택한 것만 부분 취소 합니다.")
    @PostMapping("/partial-cancel/{orderId}")
    public Api<String> cancelPartialOrder(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long orderId,
            @Valid @RequestBody CancelRequest body
    ){
        orderService.cancelPartialOrder(currentUser, orderId, body);
        return Api.OK("선택한 이력서 부분 취소 되었습니다.");
    }
}
