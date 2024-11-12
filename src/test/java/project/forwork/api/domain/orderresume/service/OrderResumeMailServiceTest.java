package project.forwork.api.domain.orderresume.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.forwork.api.domain.maillog.service.MailLogService;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeMailMessage;
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

import static org.assertj.core.api.Assertions.assertThat;
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
    @Mock
    private MailLogService mailLogService;
    private FakeMailSender fakeMailSender;
    private TestClockHolder clockHolder;
    private OrderResumeMailService orderResumeMailService;
    private FakeOrderResumeRepository fakeOrderResumeRepository;

    @BeforeEach
    public void init() {
        fakeMailSender = new FakeMailSender();
        fakeOrderResumeRepository = new FakeOrderResumeRepository();
        clockHolder = new TestClockHolder(LocalDateTime.of(2024, 9, 10, 23, 58));
        orderResumeMailService = OrderResumeMailService.builder()
                .orderResumeRepositoryCustom(orderResumeRepositoryCustom)
                .orderResumeRepository(fakeOrderResumeRepository)
                .resumeQuantityService(resumeQuantityService)
                .mailSender(fakeMailSender)
                .clockHolder(clockHolder)
                .mailLogService(mailLogService)
                .build();
    }

    @Test
    void setupConfirmedResumesAndSendEmail를_통해_OrderResume_의_상태가_SENT_로_변경_되고_SENT_AT_도_업데이트_된다(){
        //given(상황환경 세팅)
        Resume resume1 = Resume.builder()
                .id(1L)
                .build();
        Resume resume2 = Resume.builder()
                .id(2L)
                .build();
        OrderResume orderResume1 = OrderResume.builder()
                .id(1L)
                .resume(resume1)
                .status(OrderResumeStatus.ORDERED)
                .build();
        OrderResume orderResume2 = OrderResume.builder()
                .id(2L)
                .resume(resume2)
                .status(OrderResumeStatus.ORDERED)
                .build();
        List<OrderResume> orderResumes = List.of(orderResume1, orderResume2);

        //when(상황발생)
        orderResumeMailService.setupConfirmedResumesAndSendEmail(orderResumes);
        List<OrderResume> newOrderResumes = List.of(fakeOrderResumeRepository.getByIdWithThrow(1L), fakeOrderResumeRepository.getByIdWithThrow(2L));

        //then(검증)
        assertThat(newOrderResumes).allMatch(orderResume -> orderResume.getStatus().equals(OrderResumeStatus.SENT));
        assertThat(newOrderResumes).allMatch(orderResume -> orderResume.getSentAt().equals(LocalDateTime.of(2024, 9, 10, 23, 58)));
    }

    @Test
    public void sendEmail() {

        OrderResumeMailMessage orderResumeMailMessage = OrderResumeMailMessage.builder()
                .orderId(1L)
                .resumeId(1L)
                .email("user@naver.com")
                .level(LevelType.NEW)
                .field(FieldType.BACKEND)
                .resumeUrl("www.test.com")
                .build();

        orderResumeMailService.sendEmail(orderResumeMailMessage);

        assertEquals("user@naver.com", fakeMailSender.email);
        assertEquals("for-work 구매 이력서 : 신입 백엔드 이력서 #1", fakeMailSender.title);
        assertEquals("주문 번호 #1 <URL> : www.test.com", fakeMailSender.content);

        verify(mailLogService).registerSuccessLog(orderResumeMailMessage);
    }
}
