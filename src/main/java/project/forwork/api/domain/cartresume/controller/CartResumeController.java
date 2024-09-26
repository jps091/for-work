package project.forwork.api.domain.cartresume.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.cartresume.controller.model.CartResumeResponse;
import project.forwork.api.domain.cartresume.controller.model.CartSummary;
import project.forwork.api.domain.cartresume.service.CartResumeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order/cart")
@RequiredArgsConstructor
@Tag(name = "CartResumeApiController", description = "CartResume Api 서비스 컨트롤러")
public class CartResumeController {

    private final CartResumeService cartResumeService;

    @Operation(summary = "장바구니 생성", description = "장바구니 생성")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{resumeId}/register")
    public Api<String> register(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long resumeId
    ){
        cartResumeService.register(currentUser, resumeId);
        return Api.CREATED("이력서 장바구니 추가 성공");
    }

    @Operation(summary = "장바구니 비우기", description = "선택한 이력서를 장바구니에서 삭제")
    @DeleteMapping
    public Api<String> deleteBySelected(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @RequestParam List<Long> cartResumeIds
    ){
        cartResumeService.deleteBySelected(currentUser, cartResumeIds);
        return Api.OK("선택한 장바구니에서 삭제 성공");
    }

    @Operation(summary = "장바구니서 선택한 이력서 총가격, 총개수 조회하기", description = "장바구니서 선택한 이력서 총가격, 총개수 조회하기")
    @GetMapping("/summary")
    public Api<CartSummary> getCartSummary(
            @RequestParam List<Long> cartResumeIds
    ){
        CartSummary summary = cartResumeService.calculateSummary(cartResumeIds);
        return Api.OK(summary);
    }

    @Operation(summary = "장바구니에 있는 이력서 전체 조회", description = "로그인한 회원의 장바구니 이력서 전체 조회")
    @GetMapping
    public Api<List<CartResumeResponse>> findAll(
            @Parameter(hidden = true) @Current CurrentUser currentUser
    ){
        List<CartResumeResponse> cartResumeResponses = cartResumeService.findAll(currentUser);
        return Api.OK(cartResumeResponses);
    }
}
