package project.forwork.api.domain.order.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.order.controller.model.ConfirmPaymentRequest;
import project.forwork.api.domain.order.service.CheckoutService;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(SpringExtension.class)

class PgTestControllerTest {

    @Autowired
    CheckoutService checkoutService;
    @Test
    void confirm() {
        CurrentUser currentUser = CurrentUser.builder()
                .id(4L)
                .roleType(RoleType.USER)
                .build();
        ConfirmPaymentRequest body = ConfirmPaymentRequest.builder()
                .paymentKey("test-key")
                .orderId("345636546-a1d658e3-4")
                .amount("99000")
                .build();
        checkoutService.processOrderAndPayment(currentUser, body);
    }
}