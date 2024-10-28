package project.forwork.api.domain.orderresume.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StopWatch;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.orderresume.controller.model.PurchaseResponse;
import project.forwork.api.domain.token.helper.JwtTokenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SendMailPerformanceTest {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private OrderResumeMailService orderResumeMailService;

    @Test
    public void testSendEmailPerformance() throws Exception {
        int testCount = 2;
        StopWatch stopWatch = new StopWatch();


        stopWatch.start();

        for (int i = 0; i < testCount; i++) {
            PurchaseResponse purchaseResponse = PurchaseResponse.builder()
                    .orderId(5L)
                    .resumeId(1L)
                    .email("seokin23@naver.com")
                    .level(LevelType.NEW)
                    .field(FieldType.BACKEND)
                    .resumeUrl("www")
                    .build();

            orderResumeMailService.sendEmail(purchaseResponse);
        }


        stopWatch.stop();
        System.out.println("Total execution time with current thread settings: " + stopWatch.getTotalTimeMillis() + " ms");
    }



    @Test
    public void testEmailSendTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("seokin23@naver.com");
        message.setSubject("Test Email");
        message.setText("This is a test email.");

        mailSender.send(message);

        stopWatch.stop();
        System.out.println("Email Send Time: " + stopWatch.getTotalTimeMillis() + " ms");
    }
}*/
