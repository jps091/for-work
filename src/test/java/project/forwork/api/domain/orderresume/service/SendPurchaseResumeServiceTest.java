package project.forwork.api.domain.orderresume.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import project.forwork.api.domain.orderresume.infrastructure.OrderResumeQueryDslRepository;
import project.forwork.api.domain.orderresume.controller.model.PurchaseResponse;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.mock.FakeMailSender;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
@ExtendWith(MockitoExtension.class)
public class SendPurchaseResumeServiceTest {

    @Mock
    private OrderResumeQueryDslRepository orderResumeQueryDslRepository;

    private FakeMailSender fakeMailSender;

    private SendPurchaseResumeService sendPurchaseResumeService;

    @BeforeEach
    public void init() {
        fakeMailSender = new FakeMailSender();
        sendPurchaseResumeService = new SendPurchaseResumeService(orderResumeQueryDslRepository, fakeMailSender);
    }

    @Test
    public void testSendPurchaseResume() {

        PageRequest pageRequest = PageRequest.of(0, 20);
        PurchaseResponse purchaseResponse = PurchaseResponse.builder()
                .orderId(1L)
                .resumeId(4L)
                .email("user@naver.com")
                .level(LevelType.NEW)
                .field(FieldType.BACKEND)
                .resumeUrl("www.test.com")
                .build();
        Page<PurchaseResponse> purchaseInfoPage = new PageImpl<>(List.of(purchaseResponse), pageRequest, 1);

        when(orderResumeQueryDslRepository.findPurchaseResume()).thenReturn(purchaseInfoPage);

        sendPurchaseResumeService.sendPurchaseResume();

        assertEquals("user@naver.com", fakeMailSender.email);
        assertEquals("for-work 구매 이력서 : 신입 백엔드 이력서 #4", fakeMailSender.title);
        assertEquals("주문 번호 #1 <URL> : www.test.com", fakeMailSender.content);
    }
}