package project.forwork.api.domain.orderresume.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
