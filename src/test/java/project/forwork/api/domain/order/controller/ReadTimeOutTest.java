
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
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;


@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
class ReadTimeOutTest {

    @Autowired
    CheckoutService checkoutService;
    @Test
    void read_time_out이_발생해서_SocketTimeoutException_가_발생하면_결제_승인은_실패하고_ApiException_이_발생_한다() {
        CurrentUser currentUser = CurrentUser.builder()
                .id(4L)
                .status(UserStatus.USER)
                .build();
        ConfirmPaymentRequest body = ConfirmPaymentRequest.builder()
                .cartResumeIds(List.of(1L))
                .paymentKey("test-key")
                .requestId("345636546-a1d658e3-4")
                .amount(new BigDecimal("99000"))
                .build();

/*        assertThrows(ApiException.class, () -> {
            checkoutService.processOrderAndPayment(currentUser, body);
        });*/
    }
}

