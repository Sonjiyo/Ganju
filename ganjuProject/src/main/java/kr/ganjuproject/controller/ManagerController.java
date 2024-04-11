package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.auth.PrincipalDetails;
import kr.ganjuproject.entity.RoleUsers;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.dto.UserDTO;
import kr.ganjuproject.service.ManagerService;
import kr.ganjuproject.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final OrdersService ordersService;

    @GetMapping("")
    public String main(Authentication authentication, Model model){
        if(authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();

        if(principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if(user.getLoginId().equals("admin")) return "redirect:/";

            Map<String, Object> map = ordersService.getRestaurantOrderData(user.getRestaurant());
            model.addAttribute("orderCount", map.get("count"));
            model.addAttribute("orderPrice", map.get("price"));
        }

        return "manager/home";
    }

    @GetMapping("join")
    public String join(){return "manager/join";}

    @PostMapping("join")
    public @ResponseBody String insertUser(@RequestBody UserDTO userDTO) {
        Users user = new Users();
        user.setLoginId(userDTO.getLoginId());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setRole(RoleUsers.ROLE_MANAGER);
        managerService.insertUser(user);

        return "";
    }

    @GetMapping("join/{loginId}")
    public @ResponseBody String validUsernameCheck(@PathVariable(value = "loginId") String loginId){
        return managerService.isVaildLoginId(loginId) ? "ok" : "no";
    }
}
