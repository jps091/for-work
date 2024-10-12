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
@RequestMapping("/open-api/v1/thumbnail-image")
@Tag(name = "ThumbnailImage for Admin", description = "썸네일 이미지 데이터베이스 저장 컨트롤러")

public class ThumbnailImageAdminController {

    private final ThumbnailImageService thumbnailImageService;

    @Operation(summary = "썸네일 이미지 최초 데이터베이스 등록할때만 사용",
            description = "데이터베이스가 변경 되거나 썸네일 이미지가 S3에서 수정 되었을때만 사용하세요")
    @PostMapping
    public Api<String> register(
    ){
        thumbnailImageService.register();
        return Api.OK("썸네일 이미지 등록 완료");
    }
}
