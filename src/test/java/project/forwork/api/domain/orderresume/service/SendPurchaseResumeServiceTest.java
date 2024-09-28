package project.forwork.api.domain.orderresume.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.forwork.api.domain.orderresume.controller.model.PurchaseResponse;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.mock.FakeMailSender;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
@ExtendWith(MockitoExtension.class)
public class SendPurchaseResumeServiceTest {

    @Mock
    private OrderResumeRepositoryCustom orderResumeRepositoryCustom;

    private FakeMailSender fakeMailSender;

    private SendPurchaseResumeService sendPurchaseResumeService;

    @BeforeEach
    public void init() {
        fakeMailSender = new FakeMailSender();
        sendPurchaseResumeService = new SendPurchaseResumeService(orderResumeRepositoryCustom, fakeMailSender);
    }

    @Test
    public void testSendPurchaseResume() {

        PurchaseResponse purchaseResponse = PurchaseResponse.builder()
                .orderId(1L)
                .resumeId(4L)
                .email("user@naver.com")
                .level(LevelType.NEW)
                .field(FieldType.BACKEND)
                .resumeUrl("www.test.com")
                .build();

        List<PurchaseResponse> purchaseResponses = List.of(purchaseResponse);

        when(orderResumeRepositoryCustom.findAllPurchaseResume()).thenReturn(purchaseResponses);

        sendPurchaseResumeService.sendAllPurchaseResume();

        assertEquals("user@naver.com", fakeMailSender.email);
        assertEquals("for-work 구매 이력서 : 신입 백엔드 이력서 #4", fakeMailSender.title);
        assertEquals("주문 번호 #1 <URL> : www.test.com", fakeMailSender.content);
    }
}
