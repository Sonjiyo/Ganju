package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.auth.PrincipalDetails;
import kr.ganjuproject.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class HomeController {
    @GetMapping("/")
    public String home(HttpSession session, Authentication authentication, Model model){
        if(authentication == null) return "home/home";

        Object principal = authentication.getPrincipal();

        Users user = ((PrincipalDetails) principal).getUser();

        if(user.getLoginId().equals("admin")){
            return "redirect:/admin";
        } else if(user.getRestaurant() == null){
            return "redirect:restaurant/join";
        } else if(user.getRestaurant().getRecognize() == 1){
            return "redirect:/manager";
        }
        model.addAttribute("user",user);
        return "manager/joinSuccess";
    }

    @GetMapping("/auth/login")
    public @ResponseBody String login(String error, String exception){
        log.error("error ={} , excepiton={}", error, exception);
        return exception.toString();
    }
}
