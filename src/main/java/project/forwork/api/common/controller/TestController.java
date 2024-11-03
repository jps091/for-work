package project.forwork.api.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.service.port.MailSender;
import project.forwork.api.domain.order.controller.model.ConfirmPaymentRequest;
import project.forwork.api.domain.order.service.CheckoutService;
import project.forwork.api.domain.orderresume.service.OrderResumeMailService;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.ResumeQuantityService;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TestController {
    private final CheckoutService checkoutService;
    private final ResumeRepository resumeRepository;
    private final ResumeQuantityService resumeQuantityService;
    private final OrderResumeMailService orderResumeMailService;
    private final MailSender mailSender;

    @GetMapping("/open-api/order")
    @Transactional
    public String order(
            @RequestParam("resumeId") Long resumeId,
            Model model
    ) {
        Resume resume = resumeRepository.findById(resumeId).get();

        model.addAttribute("resumeId", resumeId);
        model.addAttribute("orderId", UUID.randomUUID());
        model.addAttribute("amount", resume.getPrice());
        model.addAttribute("customerKey", "customerKey-" + resume.getId());
        return "order";
    }

    @GetMapping("/open-api/home")
    public String home() {
        return "home";
    }

    @GetMapping("/open-api/order-requested")
    public String orderRequested(@RequestParam("resumeId") String resumeId,
                                 Model model) {
        model.addAttribute("resumeId", resumeId);
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
                        .status(UserStatus.USER)
                        .build();
        log.info("controller ConfirmRequest={}",body);
        checkoutService.processOrderAndPayment(currentUser, body);
        return new ResponseEntity<>("confirm#@", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test/pess")
    public ResponseEntity<String> oncePessimistic(
    ){
        resumeQuantityService.addSalesQuantityWithOnePessimistic(List.of(3L, 4L));
        return new ResponseEntity<>("confirm", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test/opt")
    public ResponseEntity<String> allPessimistic(
    ){
        resumeQuantityService.addSalesQuantityWithAllPessimistic(List.of(149L, 150L));
        return new ResponseEntity<>("confirm", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test/async-mail")
    public ResponseEntity<Integer> asyncMail(
    ){
        int count = Runtime.getRuntime().availableProcessors();
        mailSender.send("seokin23@naver.com", "e","e");
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/test/sql")
    @Transactional
    public ResponseEntity<String> sqlOnce(
    ){
        Resume resume = resumeRepository.getByIdWithThrow(2L);
        return new ResponseEntity<>(resume.getLevel().toString(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/test/loop")
    public ResponseEntity<String> loop(
    ){
        int count = 30;
        for(int i = 0; i < count; i++){
            if(i == count -1)
                System.out.println("i = " + i);
        }
        return new ResponseEntity<>("loop end: "+ count, HttpStatus.OK);
    }
}
