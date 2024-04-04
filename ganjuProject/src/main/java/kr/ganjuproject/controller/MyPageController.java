package kr.ganjuproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/myPage")
@RequiredArgsConstructor
public class MyPageController {
    @GetMapping("main")
    public String myPage() {
        return "/manager/myPage";
    }
}
