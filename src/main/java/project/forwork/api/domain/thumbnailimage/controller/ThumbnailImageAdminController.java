package project.forwork.api.domain.thumbnailimage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.api.Api;

import project.forwork.api.domain.thumbnailimage.service.ThumbnailImageService;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin-api/v1/thumbnail-image")
@Tag(name = "썸네일 이미지 관리자 전용 컨트롤러", description = "썸네일 이미지 저장 컨트롤러")

public class ThumbnailImageAdminController {

    private final ThumbnailImageService thumbnailImageService;

    @Operation(summary = "썸네일 이미지 최초 데이터베이스 등록할때만 사용")
    @PostMapping("/db")
    public Api<String> registerToDatabase(){
        thumbnailImageService.registerToDatabase();
        return Api.OK("썸네일 이미지 데이터베이스 등록 완료");
    }
}
