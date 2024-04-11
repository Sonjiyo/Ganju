package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.auth.PrincipalDetails;
import kr.ganjuproject.entity.RoleUsers;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final ManagerService managerService;
    @GetMapping("/")
    public String home(HttpSession session, Authentication authentication, Model model){
        //임의로 넣어놓은 어드민
        if(managerService.isVaildLoginId("admin")){
            var user = new Users();
            user.setLoginId("admin");
            user.setPassword("1234");
            user.setEmail("admin@example.com");
            user.setPhone("010-0000-0001");
            user.setRole(RoleUsers.ROLE_ADMIN);
            managerService.insertUser(user);
        }

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
