package project.forwork.api.domain.orderresume.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import project.forwork.api.domain.orderresume.model.PurchaseInfo;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.mock.FakeMailSender;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SendPurchaseResumeServiceTest {

    @Mock
    private OrderResumeRepository orderResumeRepository;

    private FakeMailSender fakeMailSender;

    private SendPurchaseResumeService sendPurchaseResumeService;

    @BeforeEach
    public void init() {
        fakeMailSender = new FakeMailSender();
        sendPurchaseResumeService = new SendPurchaseResumeService(orderResumeRepository, fakeMailSender);
    }

    @Test
    public void testSendPurchaseResume() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        PurchaseInfo purchaseInfo = PurchaseInfo.builder()
                .orderId(1L)
                .resumeId(2L)
                .email("user@naver.com")
                .level(LevelType.NEW)
                .field(FieldType.BACKEND)
                .resumeUrl("www.test.com")
                .build();
        Page<PurchaseInfo> purchaseInfoPage = new PageImpl<>(List.of(purchaseInfo), pageRequest, 1);

        when(orderResumeRepository.getPurchaseResume()).thenReturn(purchaseInfoPage);

        sendPurchaseResumeService.sendPurchaseResume();

        assertEquals("user@naver.com", fakeMailSender.email);
        assertEquals("for-work 구매 이력서 : 신입 백엔드 이력서 #2", fakeMailSender.title);
        assertEquals("주문 번호 #1 <URL> : www.test.com", fakeMailSender.content);
    }
}
