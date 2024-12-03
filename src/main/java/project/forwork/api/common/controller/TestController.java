package project.forwork.api.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.api.Api;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.producer.Producer;
import project.forwork.api.domain.order.controller.model.ConfirmPaymentRequest;
import project.forwork.api.domain.order.service.CheckoutService;
import project.forwork.api.domain.orderresume.infrastructure.message.BuyerMessage;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.domain.salespost.infrastructure.model.SalesPostSearchDto;
import project.forwork.api.domain.salespost.service.SalesPostService;
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TestController {
    private final CheckoutService checkoutService;
    private final ResumeRepository resumeRepository;
    private final Producer producer;
    private final SalesPostService salesPostService;

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
                        .id(1L)
                        .status(UserStatus.ADMIN)
                        .build();
        log.info("controller ConfirmRequest={}",body);
        checkoutService.processOrderAndPayment(currentUser, body);
        return new ResponseEntity<>("confirm#@", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test/pess3")
    @Transactional
    public ResponseEntity<String> oncePessimistic1(
    ){
        List<Long> resumeIds = List.of(3L);
        for (Long resumeId : resumeIds) {
            Resume resume = resumeRepository.getByIdWithPessimisticLock(resumeId);
            resume = resume.increaseSalesQuantity();
            resumeRepository.save(resume);
        }
        return new ResponseEntity<>("confirm", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test/pess4")
    @Transactional
    public ResponseEntity<String> oncePessimistic2(
    ){
        List<Long> resumeIds = List.of(4L);
        for (Long resumeId : resumeIds) {
            Resume resume = resumeRepository.getByIdWithPessimisticLock(resumeId);
            resume = resume.increaseSalesQuantity();
            resumeRepository.save(resume);
        }
        return new ResponseEntity<>("confirm", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test/pro/{resumeId}")
    public ResponseEntity<String> produceMsg(
            @PathVariable Long resumeId
    ){
        BuyerMessage message = new BuyerMessage("seokin23@naver.com", "title", "content", 1L, resumeId);
        producer.sendBuyerMail(message);
        return new ResponseEntity<>("testBuyerMail", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test/like")
    public Api<List<SalesPostSearchDto>> searchByTextWithLike(
            @RequestParam(required = false) String text,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        List<SalesPostSearchDto> salesPostSearchDtos = salesPostService.searchByTextWithLike(text, pageNumber, pageSize);
        return Api.OK(salesPostSearchDtos);
    }
}
