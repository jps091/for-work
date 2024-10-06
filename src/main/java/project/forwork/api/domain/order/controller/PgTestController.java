package project.forwork.api.domain.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.order.controller.model.ConfirmRequest;
import project.forwork.api.domain.order.controller.model.PartialCancelRequest;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.CheckoutService;
import project.forwork.api.domain.order.service.OrderService;
import project.forwork.api.domain.order.service.port.OrderRepository;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PgTestController {
    private final OrderRepository orderRepository;
    private final CheckoutService checkoutService;

    @GetMapping("/open-api/order")
    @Transactional
    public String order(
            @RequestParam("orderId") Long orderId,
            Model model
    ) {
        Order order = orderRepository.findById(orderId).get();

        model.addAttribute("requestId", order.getRequestId());
        model.addAttribute("amount", order.getTotalAmount());
        model.addAttribute("customerKey", "customerKey-" + order.getUser().getId());
        return "order";
    }

    @GetMapping("/open-api/order-requested")
    public String orderRequested() {
        return "order-requested";
    }

    @GetMapping("/open-api/fail")
    public String fail() {
        return "fail";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/open-api/confirm")
    public Api<String> confirm(
            @RequestBody ConfirmRequest body
    ){
        CurrentUser currentUser = CurrentUser.builder()
                        .id(4L)
                        .roleType(RoleType.USER)
                        .build();
        log.info("controller ConfirmRequest={}",body);
        checkoutService.processOrderAndPayment(currentUser, body);
        return Api.OK("결제 승인 성공");
    }
}
