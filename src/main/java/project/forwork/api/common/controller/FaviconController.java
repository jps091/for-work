package project.forwork.api.common.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaviconController {

    @RequestMapping("favicon.ico")
    public void favicon() {
        // favicon.ico 요청을 무시하고 204 No Content 상태를 반환
    }
}