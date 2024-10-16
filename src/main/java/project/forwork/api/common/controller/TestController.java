package project.forwork.api.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.order.controller.model.ConfirmOrderRequest;
import project.forwork.api.domain.order.controller.model.ConfirmPaymentRequest;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.CheckoutService;
import project.forwork.api.domain.order.service.OrderService;
import project.forwork.api.domain.order.service.port.OrderRepository;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.OrderResumeService;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;
import project.forwork.api.domain.resume.service.ResumeService;
import project.forwork.api.domain.salespost.controller.model.SalesPostDetailResponse;
import project.forwork.api.domain.salespost.service.SalesPostService;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TestController {
    private final OrderRepository orderRepository;
    private final CheckoutService checkoutService;
    private final OrderResumeService orderResumeService;
    private final OrderResumeRepository orderResumeRepository;

    @GetMapping("/open-api/order")
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

    @GetMapping("/open-api/home")
    public String home() {
        return "home";
    }

    @GetMapping("/open-api/order-requested")
    public String orderRequested() {
        return "order-requested";
    }

    @GetMapping("/open-api/fail")
    public String fail() {
        return "fail";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test/confirm")
    public ResponseEntity<String> confirm(
            @RequestBody ConfirmPaymentRequest body
    ){
        CurrentUser currentUser = CurrentUser.builder()
                        .id(4L)
                        .roleType(RoleType.USER)
                        .build();
        log.info("controller ConfirmRequest={}",body);
        checkoutService.processOrderAndPayment(currentUser, body);
        return new ResponseEntity<>("confirm", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test/pess")
    public ResponseEntity<String> oncePessimistic(
    ){
        orderResumeService.addSalesQuantityWithOnePessimistic2(List.of(3L, 4L));
        return new ResponseEntity<>("confirm", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test/opt")
    public ResponseEntity<String> allPessimistic(
    ){
        orderResumeService.addSalesQuantityWithAllPessimistic2(List.of(149L, 150L));
        return new ResponseEntity<>("confirm", HttpStatus.OK);
    }
}
