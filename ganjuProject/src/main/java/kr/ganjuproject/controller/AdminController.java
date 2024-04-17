package kr.ganjuproject.controller;

import kr.ganjuproject.entity.Board;
import kr.ganjuproject.entity.Restaurant;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.service.BoardService;
import kr.ganjuproject.service.ManagerService;
import kr.ganjuproject.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ManagerService managerService;
    private final BoardService boardService;

    @GetMapping("")
    public String home(){
        return "redirect:/admin/restaurantList";
    }

    @GetMapping("/restaurantList")
    public String restaurantList(Model model){
        List<Users> list = managerService.getManagerList();
        model.addAttribute("list", list);
        return "admin/restaurantList";
    }
    @GetMapping("/reportList")
    public String reportList(Model model){
        List<Board> list = boardService.getReortList();
        model.addAttribute("list", list);
        return "admin/reportList";
    }

    @GetMapping("/askList")
    public String askList(Model model){
        List<Board> list = boardService.getAskList();
        model.addAttribute("list", list);
        return "admin/askList";
    }

}
