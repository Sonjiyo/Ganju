package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.auth.PrincipalDetails;
import kr.ganjuproject.entity.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(HttpSession session, Authentication authentication){
        if(authentication == null) return "home/home";
        Object principal = authentication.getPrincipal();

        if(principal instanceof PrincipalDetails){
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();

            if(user.getLoginId().equals("admin")){
                return "redirect:/admin";
            } else if(user.getRestaurant() != null){
                return "redirect:/manager";
            } else{
                return "redirect:/manager/joinRestaurant";
            }
        } else {
            // 인증된 사용자가 PrincipalDetails가 아닌 다른 경우 처리
            return "redirect:/"; // 예를 들어 홈 페이지로 리다이렉트
        }
    }
}
