package project.forwork.api.domain.orderresume.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.orderresume.controller.model.PurchaseResponse;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.ResumeQuantityService;
import project.forwork.api.mock.FakeMailSender;
import project.forwork.api.mock.FakeOrderResumeRepository;
import project.forwork.api.mock.TestClockHolder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
@ExtendWith(MockitoExtension.class)
public class OrderResumeMailServiceTest {

    @Mock
    private OrderResumeRepositoryCustom orderResumeRepositoryCustom;

    @Mock
    private ResumeQuantityService resumeQuantityService;

    private FakeMailSender fakeMailSender;
    private TestClockHolder clockHolder;

    private OrderResumeMailService orderResumeMailService;

    @BeforeEach
    public void init() {
        fakeMailSender = new FakeMailSender();
        FakeOrderResumeRepository fakeOrderResumeRepository = new FakeOrderResumeRepository();
        clockHolder = new TestClockHolder(LocalDateTime.of(2024, 9, 10, 23, 58));
        orderResumeMailService = OrderResumeMailService.builder()
                .orderResumeRepositoryCustom(orderResumeRepositoryCustom)
                .orderResumeRepository(fakeOrderResumeRepository)
                .resumeQuantityService(resumeQuantityService)
                .mailSender(fakeMailSender)
                .clockHolder(clockHolder)
                .build();
    }

    @Test
    public void testSendPurchaseResume() {

        Order order = Order.builder()
                .id(1L)
                .build();
        Resume resume = Resume.builder()
                .id(1L)
                .build();
        OrderResume orderResume = OrderResume.builder()
                .id(1L)
                .order(order)
                .resume(resume)
                .status(OrderResumeStatus.ORDERED)
                .build();
        List<OrderResume> orderResumes = List.of(orderResume);

        PurchaseResponse purchaseResponse = PurchaseResponse.builder()
                .orderId(1L)
                .resumeId(1L)
                .email("user@naver.com")
                .level(LevelType.NEW)
                .field(FieldType.BACKEND)
                .resumeUrl("www.test.com")
                .build();

        List<PurchaseResponse> purchaseResponses = List.of(purchaseResponse);

        when(orderResumeRepositoryCustom.findAllPurchaseResume(orderResumes)).thenReturn(purchaseResponses);

        orderResumeMailService.sendResumeMail(orderResumes);

        assertEquals("user@naver.com", fakeMailSender.email);
        assertEquals("for-work 구매 이력서 : 신입 백엔드 이력서 #1", fakeMailSender.title);
        assertEquals("주문 번호 #1 <URL> : www.test.com", fakeMailSender.content);
    }
}
