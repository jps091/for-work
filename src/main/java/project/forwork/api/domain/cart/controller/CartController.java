package project.forwork.api.domain.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.cart.service.CartService;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "CartApiController", description = "Cart Api 서비스 컨트롤러")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니 생성", description = "장바구니 생성")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public Api<String> register(
            @Parameter(hidden = true) @Current CurrentUser currentUser
    ){
        cartService.register(currentUser);
        return Api.CREATED("장바구니 생성 성공");
    }
}
