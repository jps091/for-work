package project.forwork.api.domain.cartresume.model;

import lombok.Getter;
import project.forwork.api.common.error.CartResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.cartresume.controller.model.CartResumeResponse;
import project.forwork.api.domain.cartresume.infrastructure.CartResumeEntity;

import java.util.List;

@Getter
public class CartResumes {
    private final List<CartResume> cartResumeList;

    // 생성자를 private으로 생성하고, static으로 하는 이유
    // 일급 컬렉션의 생성 책임을 일급 컬렉션 관리할 수 있음
    private CartResumes(List<CartResume> cartResumeList){
        this.cartResumeList = cartResumeList;
    }

    public static CartResumes of(List<CartResume> cartResumeList){
        return new CartResumes(cartResumeList);
    }

    public List<CartResumeEntity> toEntityList(){
        return cartResumeList.stream().map(CartResumeEntity::from).toList();
    }

    public List<CartResumeResponse> toResponseList(){
        return cartResumeList.stream()
                .map(CartResumeResponse::from)
                .toList();
    }
}
