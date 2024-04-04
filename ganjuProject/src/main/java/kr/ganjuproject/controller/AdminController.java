package kr.ganjuproject.controller;

import kr.ganjuproject.service.BoardService;
import kr.ganjuproject.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ManagerService managerService;
    private final BoardService boardService;

    @GetMapping("/restaurantList")
    public String restaurantList(){
        return "admin/restaurantList";
    }

    @GetMapping("/reportList")
    public String reportList(){
        return "admin/reportList";
    }

}
