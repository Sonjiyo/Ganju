package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.entity.Users;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(HttpSession session){
        if(session.getAttribute("log")==null) return "home/home";
        Users user = (Users) session.getAttribute("log");

        if(user.getLoginId().equals("admin")){
            return "redirect:/manager";
        }
        return "redirect:/admin";
    }
}
